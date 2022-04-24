package com.example.webviewtest;

import static org.junit.Assert.*;

import android.content.Intent;
import android.provider.MediaStore;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;

public class FaceCaptureTest {


    @Rule
    public ActivityTestRule<FaceCapture> mActivityTestRule = new ActivityTestRule<>(FaceCapture.class);

    @Test
    public void validPicture()
    {
        int pic_id = 123;
        FaceCapture.startActivityForResult(new Intent(MediaStore
                .ACTION_IMAGE_CAPTURE), pic_id);
    }

    @Test
    public void testRequest()
    {
        String url = "https://192.168.1.4:5000/add_face?name=" + "Ante";
        RequestQueue mRQueue;
        StringRequest mSReq;
        mRQueue = Volley.newRequestQueue(FaceCapture.class);
        try {
            HttpsURLConnection.setDefaultSSLSocketFactory(Certificate_Handling.getSocketFactory(FaceCapture.class));

        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        mSReq = new StringRequest(Request.Method.GET, url, response -> {}, error -> {});

        mRQueue.add(mSReq);

    }
    }

}