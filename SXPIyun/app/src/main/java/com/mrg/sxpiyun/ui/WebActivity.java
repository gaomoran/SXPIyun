package com.mrg.sxpiyun.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.zxg.sxpi.ui.R;

public class WebActivity extends AppCompatActivity {

    private WebView webView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        textView = (TextView)findViewById(R.id.tv_top_title);
        textView.setText("校园动态");
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        webView = (WebView) findViewById(R.id.wv_web_web);
        loadweb(url);
    }

    public void loadweb(String url) {

        webView.loadUrl(url);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return false;
            }
        });

       //支持缩放
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                textView.setText(title);//a textview
            }
        });

    }
    public void top_backward(View v){
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK)
        {
            if(webView.canGoBack())
            {
                webView.goBack();//返回上一页面
                return true;
            }
            else
            {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
