package com.github.tvbox.osc.ui.dialog;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.github.tvbox.osc.R;

import org.jetbrains.annotations.NotNull;

public class AboutDialog extends BaseDialog {

    public AboutDialog(@NonNull @NotNull Context context) {
        super(context);
        setContentView(R.layout.dialog_about);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.tv_update).setOnClickListener(v -> {
           if (listener != null) {
               listener.onUpdate();
           }
        });
    }

    public void setOnListener(OnUpdateListener listener) {
        this.listener = listener;
    }

    OnUpdateListener listener = null;

    public interface OnUpdateListener {
        void onUpdate();
    }
}