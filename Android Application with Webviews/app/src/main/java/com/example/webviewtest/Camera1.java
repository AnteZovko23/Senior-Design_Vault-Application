package com.example.webviewtest;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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

        webView1.loadUrl("http://138.47.155.113:5000/");
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