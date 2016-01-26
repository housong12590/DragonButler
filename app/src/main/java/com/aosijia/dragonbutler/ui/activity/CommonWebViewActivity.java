package com.aosijia.dragonbutler.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.aosijia.dragonbutler.R;


/**
 * 通用webview
 */
public class CommonWebViewActivity extends BaseActivity {

    public static final String EXTRA_KEY_URL = "url";
    public static final String EXTRA_KEY_TITLE = "title";
    public static final String EXTRA_KEY_DATA = "data";

    private String mTitle = "龙管家";
    private String mUrl;
    private String mData;

    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(EXTRA_KEY_URL)) {
                mUrl = bundle.getString(EXTRA_KEY_URL);
            }
            if (bundle.containsKey(EXTRA_KEY_TITLE)) {
                mTitle = bundle.getString(EXTRA_KEY_TITLE);
            }
            if (bundle.containsKey(EXTRA_KEY_DATA)) {
                mData = bundle.getString(EXTRA_KEY_DATA);
            }
        }
        setImmersionStatus();
        setContentView(R.layout.activity_webview);
        initView();
    }

    private void initView() {
        setTitle(mTitle,null,NO_RES_ID);
        pb = (ProgressBar) findViewById(R.id.pb);
        final WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                pb.setProgress(newProgress);
                if (newProgress == 100) {
                    pb.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }
        });
        if (mUrl != null) {
            webView.loadUrl(mUrl);
//            webView.loadUrl("http://www.baidu.com/");
        }

        if (!TextUtils.isEmpty(mData)) {
            webView.loadData(mData, "text/html; charset=UTF-8", null);
        }

    }
}
