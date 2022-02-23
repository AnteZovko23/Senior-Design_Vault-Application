package com.example.webviewtest;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Camera1 extends AppCompatActivity {
    private WebView webView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_camera1);

        webView1 = findViewById(R.id.webView1);
        webView1.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });

        WebSettings webSettings = webView1.getSettings();
        webSettings.setJavaScriptEnabled(true);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                webView1.loadUrl("http://172.20.10.7:5000/video_stream");
            }
        }, 500);   //5 seconds

    }

    @Override
    public void onBackPressed() {
        if(webView1.canGoBack()) {
            webView1.goBack();
        }
        else {
            super.onBackPressed();
        }
    }
}