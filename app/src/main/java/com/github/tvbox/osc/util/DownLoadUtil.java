package com.github.tvbox.osc.util;

import android.content.Context;
import android.os.Environment;

import com.azhon.appupdate.utils.ApkUtil;
import com.azhon.appupdate.utils.Constant;
import com.azhon.appupdate.utils.FileUtil;
import com.azhon.appupdate.utils.LogUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author GreatKing
 */
public class DownLoadUtil {

    /**
     * todo  后面优化 下载速度  起线程池  下载
     *
     * @param url           url
     * @param mOkHttpClient mOkHttpClient
     */
    public static void downloadStart(Context context, String url, OkHttpClient mOkHttpClient) {
        try {
            new Thread(() -> {
                try {
                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    Response response = mOkHttpClient.newCall(request).execute();
                    if (response.code() == 200) {
                        InputStream is = response.body().byteStream();
                        //为了方便就不动态申请权限了,直接将文件放到CacheDir()中
                        File file = FileUtil.createFile(context.getExternalCacheDir().getPath(), "tv.apk");
                        FileOutputStream fos = new FileOutputStream(file);
                        BufferedInputStream bis = new BufferedInputStream(is);
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = bis.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                        }
                        fos.flush();
                        fos.close();
                        bis.close();
                        is.close();
                        ApkUtil.installApk(context, Constant.AUTHORITIES, file);
                    } else {
                        ToastUtils.s(context, "下载地址无法访问");
                    }
                } catch (IOException e) {
                    LogUtil.e("======", e.toString());
                } catch (Exception e) {
                    LogUtil.e("======", e.toString());
                }
            }).start();
        } catch (Exception e) {
            LogUtil.e("======", e.toString());
        }
    }
}
