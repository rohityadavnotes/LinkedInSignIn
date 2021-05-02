package com.social.sign.in.webview;

import android.webkit.WebView;

public interface WebChromeClientCallback {
    void onProgress(WebView webView, int newProgress);
}