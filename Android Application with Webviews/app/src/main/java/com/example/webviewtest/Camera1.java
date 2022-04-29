package com.example.webviewtest;

import androidx.appcompat.app.AppCompatActivity;

import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;

public class Camera1 extends AppCompatActivity {
    private WebView webView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera1);

        webView1 = findViewById(R.id.webView1);
        webView1.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
//            @Override
//            public void onReceivedSslError(final WebView view, final SslErrorHandler handler, final SslError error) {
//                handler.proceed();
//            }
        });



        WebSettings webSettings = webView1.getSettings();
        webSettings.setJavaScriptEnabled(true);
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {

            public void run() {

                webView1.loadUrl("http://192.168.1.5:5000/video_stream");
            }
        }, 2500);   //5 seconds

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