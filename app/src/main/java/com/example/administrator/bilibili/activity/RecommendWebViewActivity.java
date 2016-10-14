package com.example.administrator.bilibili.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.administrator.bilibili.R;

public class RecommendWebViewActivity extends AppCompatActivity
{

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.e("自定义标签", "类名==RecommendWebViewActivity" + "方法名==onCreate=====:" + "");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_web_view);
        mWebView = (WebView) findViewById(R.id.recommend_webview);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
            {
                return false;
            }
        });

    }

}
