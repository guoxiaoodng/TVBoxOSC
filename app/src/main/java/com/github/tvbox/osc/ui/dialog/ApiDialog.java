package com.github.tvbox.osc.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.GsonUtils;
import com.github.tvbox.osc.R;
import com.github.tvbox.osc.bean.IdNameAddressBean;
import com.github.tvbox.osc.event.RefreshEvent;
import com.github.tvbox.osc.server.ControlManager;
import com.github.tvbox.osc.ui.adapter.ApiHistoryDialogAdapter;
import com.github.tvbox.osc.ui.tv.QRCodeGen;
import com.github.tvbox.osc.util.FileUtils;
import com.github.tvbox.osc.util.HawkConfig;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * 描述
 *
 * @author pj567
 * @since 2020/12/27
 */
public class ApiDialog extends BaseDialog {
    private final ImageView ivQRCode;
    private final TextView tvAddress;
    private final TextView inputApi;
    private final EditText etAddress;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent event) {
        if (event.type == RefreshEvent.TYPE_API_URL_CHANGE) {
            inputApi.setText((String) event.obj);
        }
    }

    public ApiDialog(@NonNull @NotNull Context context) {
        super(context);
        setContentView(R.layout.dialog_api);
        setCanceledOnTouchOutside(false);
        ivQRCode = findViewById(R.id.ivQRCode);
        tvAddress = findViewById(R.id.tvAddress);
        inputApi = findViewById(R.id.input);
        inputApi.setText(Hawk.get(HawkConfig.API_URL, ""));
        etAddress = findViewById(R.id.et_address);

        String urls = FileUtils.readTextFile(context);
        String[] arr = urls.split(";");
        List<IdNameAddressBean> addressList = new ArrayList<>();
        for (String s : arr) {
            IdNameAddressBean bean = GsonUtils.fromJson(s, IdNameAddressBean.class);
            addressList.add(bean);
        }
        String current = Hawk.get(HawkConfig.API_URL, "");

        int idx = 0;
        for (int i = 0; i < addressList.size(); i++) {
            if (!TextUtils.isEmpty(current) && current.equals(addressList.get(i).getAddress())) {
                idx = i;
                break;
            }
        }

        int finalIdx = idx;
        inputApi.setOnClickListener(v -> {
            ApiHistoryDialog dialog = new ApiHistoryDialog(getContext());
            dialog.setTip("选择线路");
            dialog.setAdapter(new ApiHistoryDialogAdapter.SelectDialogInterface() {
                @Override
                public void click(IdNameAddressBean value) {
                    inputApi.setText(value.getAddress());
                    inputApi.setTag(value.getName());
                    listener.onchange(value.getAddress());
                    dialog.dismiss();
                }

                @Override
                public void del(IdNameAddressBean value, ArrayList<IdNameAddressBean> data) {
                    Hawk.put(HawkConfig.API_HISTORY, data);
                }
            }, addressList, finalIdx);
            dialog.show();
        });

        findViewById(R.id.inputSubmit).setOnClickListener(v -> {
            String newApi = inputApi.getText().toString().trim();
            String newTag = inputApi.getTag().toString();
            ArrayList<IdNameAddressBean> history = Hawk.get(HawkConfig.API_HISTORY, new ArrayList<>());
            history.add(0, new IdNameAddressBean(newTag, newApi));
            Hawk.put(HawkConfig.API_HISTORY, history);
            listener.onchange(newApi);
            dismiss();
        });

        findViewById(R.id.tv_submit).setOnClickListener(v -> {
            String newApi = etAddress.getText().toString().trim();
            ArrayList<IdNameAddressBean> history = Hawk.get(HawkConfig.API_HISTORY, new ArrayList<>());
            history.add(0, new IdNameAddressBean("自定义", newApi));
            Hawk.put(HawkConfig.API_HISTORY, history);
            listener.onchange(newApi);
            dismiss();
        });
        findViewById(R.id.apiHistory).setOnClickListener(v -> {
            ArrayList<IdNameAddressBean> history = Hawk.get(HawkConfig.API_HISTORY, new ArrayList<>());
            if (history.isEmpty()) {
                return;
            }
            List<String> hisList = new ArrayList<>();
            for (int i = 0; i < history.size(); i++) {
                hisList.add(history.get(i).getAddress());
            }
            String current1 = Hawk.get(HawkConfig.API_URL, "");
            int idx1 = 0;
            if (hisList.contains(current1)) {
                idx1 = hisList.indexOf(current1);
            }
            ApiHistoryDialog dialog = new ApiHistoryDialog(getContext());
            dialog.setTip("历史配置列表");
            dialog.setAdapter(new ApiHistoryDialogAdapter.SelectDialogInterface() {
                @Override
                public void click(IdNameAddressBean value) {
                    inputApi.setText(value.getAddress());
                    listener.onchange(value.getAddress());
                    dialog.dismiss();
                }

                @Override
                public void del(IdNameAddressBean value, ArrayList<IdNameAddressBean> data) {
                    Hawk.put(HawkConfig.API_HISTORY, data);
                }
            }, history, idx1);
            dialog.show();
        });
        findViewById(R.id.storagePermission).setOnClickListener(v -> {
            if (XXPermissions.isGranted(getContext(), Permission.Group.STORAGE)) {
                Toast.makeText(getContext(), "已获得存储权限", Toast.LENGTH_SHORT).show();
            } else {
                XXPermissions.with(getContext())
                        .permission(Permission.Group.STORAGE)
                        .request(new OnPermissionCallback() {
                            @Override
                            public void onGranted(List<String> permissions, boolean all) {
                                if (all) {
                                    Toast.makeText(getContext(), "已获得存储权限", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onDenied(List<String> permissions, boolean never) {
                                if (never) {
                                    Toast.makeText(getContext(), "获取存储权限失败,请在系统设置中开启", Toast.LENGTH_SHORT).show();
                                    XXPermissions.startPermissionActivity((Activity) getContext(), permissions);
                                } else {
                                    Toast.makeText(getContext(), "获取存储权限失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        refreshQRCode();
    }

    private void refreshQRCode() {
        String address = ControlManager.get().getAddress(false);
        tvAddress.setText(String.format("手机/电脑扫描上方二维码或者直接浏览器访问地址\n%s", address));
        ivQRCode.setImageBitmap(QRCodeGen.generateBitmap(address, AutoSizeUtils.mm2px(getContext(), 300), AutoSizeUtils.mm2px(getContext(), 300)));
    }

    public void setOnListener(OnListener listener) {
        this.listener = listener;
    }

    OnListener listener = null;

    public interface OnListener {
        void onchange(String api);
    }
}
