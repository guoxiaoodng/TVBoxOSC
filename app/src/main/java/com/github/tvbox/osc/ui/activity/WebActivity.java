package com.github.tvbox.osc.ui.activity;

import static android.webkit.WebSettings.LOAD_NO_CACHE;

import android.annotation.SuppressLint;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.tvbox.osc.R;
import com.github.tvbox.osc.base.BaseActivity;

public class WebActivity extends BaseActivity {

    private WebView webView;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_web;
    }

    @Override
    protected void init() {
        webView = findViewById(R.id.web_view);
        initWeb(webView);
        webView.loadUrl("https://www.baidu.com/");
//        webView.loadUrl("https://www.pgyer.com/iVObXX/");
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWeb(WebView webView) {
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportZoom(true);
        settings.setCacheMode(LOAD_NO_CACHE);
        settings.setAllowContentAccess(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return true;
            }

        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!webView.canGoBackOrForward(-1)) {
            super.onBackPressed();
        } else {
            webView.goBackOrForward(-1);
        }
    }
}
