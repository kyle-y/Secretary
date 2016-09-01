package com.example.yxb.secretary.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.example.yxb.secretary.R;
import com.tencent.smtt.sdk.QbSdk;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.activity
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/23
 * MODIFY_BY:
 */
public class WebViewActivity extends Activity{
    com.tencent.smtt.sdk.WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        QbSdk.allowThirdPartyAppDownload(true);
        webView = (com.tencent.smtt.sdk.WebView) findViewById(R.id.webView);


        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("DATA");
        String url = bundle.getString("URL");
        webView.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.KEYCODE_BACK){
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
