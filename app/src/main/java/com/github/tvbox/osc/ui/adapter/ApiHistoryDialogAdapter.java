package com.github.tvbox.osc.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.github.tvbox.osc.R;
import com.github.tvbox.osc.bean.IdNameAddressBean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ApiHistoryDialogAdapter extends ListAdapter<IdNameAddressBean, ApiHistoryDialogAdapter.SelectViewHolder> {

    class SelectViewHolder extends RecyclerView.ViewHolder {

        public SelectViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }

    public interface SelectDialogInterface {
        void click(IdNameAddressBean bean);

        void del(IdNameAddressBean value, ArrayList<IdNameAddressBean> data);
    }


    private ArrayList<IdNameAddressBean> data = new ArrayList<>();

    private IdNameAddressBean select = new IdNameAddressBean();

    private SelectDialogInterface dialogInterface = null;

    public ApiHistoryDialogAdapter(SelectDialogInterface dialogInterface) {
        super(new DiffUtil.ItemCallback<IdNameAddressBean>() {
            @Override
            public boolean areItemsTheSame(@NonNull @NotNull IdNameAddressBean oldItem, @NonNull @NotNull IdNameAddressBean newItem) {
                return oldItem.equals(newItem);
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull @NotNull IdNameAddressBean oldItem, @NonNull @NotNull IdNameAddressBean newItem) {
                return oldItem.equals(newItem);
            }
        });
        this.dialogInterface = dialogInterface;
    }

    public void setData(List<IdNameAddressBean> newData, int defaultSelect) {
        data.clear();
        data.addAll(newData);
        select = data.get(defaultSelect);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public ApiHistoryDialogAdapter.SelectViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ApiHistoryDialogAdapter.SelectViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_api_history, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull ApiHistoryDialogAdapter.SelectViewHolder holder, int position) {
        IdNameAddressBean value = data.get(position);
        TextView textView = holder.itemView.findViewById(R.id.tvName);
        String id = String.valueOf(position + 1);
        if (null == value) {
            return;
        }
        textView.setText(id + "：" + value.getName());
        if (select.equals(value)) {
            textView.setTextColor(Color.RED);
        } else {
            textView.setTextColor(Color.WHITE);
        }
        holder.itemView.findViewById(R.id.tvName).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select.equals(value)) {
                    dialogInterface.click(value);
                    return;
                }
                notifyItemChanged(data.indexOf(select));
                select = value;
                notifyItemChanged(data.indexOf(value));
                dialogInterface.click(value);
            }
        });
        holder.itemView.findViewById(R.id.tvDel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select.equals(value))
                    return;
                notifyItemRemoved(data.indexOf(value));
                data.remove(value);
                dialogInterface.del(value, data);
            }
        });
    }
}
