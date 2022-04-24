package com.example.webviewtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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


public class Camera extends AppCompatActivity {
    private Button camera1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        camera1 = (Button) findViewById(R.id.camera1);
        camera1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_video_frames();
                openCamera1();
            }
        });
    }

    private void get_video_frames() {
        // Tell volley to use a SocketFactory from our SSLContext

        String url = "http://192.168.1.5:5000/start_feed";
        RequestQueue mRQueue;
        StringRequest mSReq;
        mRQueue = Volley.newRequestQueue(Camera.this);
//        try {
//            HttpsURLConnection.setDefaultSSLSocketFactory(Certificate_Handling.getSocketFactory(this));
//
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
        mSReq = new StringRequest(Request.Method.GET, url, response -> {}, error -> {});

        mRQueue.add(mSReq);

    }
    public void openCamera1() {
        Intent intent = new Intent(this, Camera1.class);
        startActivity(intent);
    }


}