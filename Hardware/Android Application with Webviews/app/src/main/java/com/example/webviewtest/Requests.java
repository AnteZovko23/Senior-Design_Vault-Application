package com.example.webviewtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Requests extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private RequestQueue mRQueue;
    private StringRequest mSReq;

    String url = "http://172.20.10.7:5000/";
    // node server - "http://localhost:8081/";
    // pi server - "http://172.20.10.13:5000/";
    // for testing - "http://172.20.10.7:5000/pi_test";
    String endpoint = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        Button GetReq;
        Button GetReq2;
        Button GetReq3;
        Button GetReq4;
        Button PostReq;
        TextView responses = findViewById(R.id.responses);

        GetReq = (Button) findViewById(R.id.GetReq);
        GetReq2 = (Button) findViewById(R.id.GetReq2);
        GetReq3 = (Button) findViewById(R.id.GetReq3);
        GetReq4 = (Button) findViewById(R.id.GetReq4);
        PostReq = (Button) findViewById(R.id.PostReq);

        GetReq.setOnClickListener(v -> GetRequest(responses));
        GetReq2.setOnClickListener(v -> GetRequest2(responses));
        GetReq3.setOnClickListener(v -> GetRequest3(responses));
        //GetReq4.setOnClickListener(v -> GetRequest4(responses));
        //PostReq.setOnClickListener(v -> PostRequest(responses));
    }

    // NEEDS usesClearTextTraffic to run (found in manifest)
    /* might need to change to allow for use with additional classes; comment out intent */
    private void GetRequest(TextView data_display) {

        endpoint = "";
        mRQueue = Volley.newRequestQueue(Requests.this);
        mSReq = new StringRequest(Request.Method.GET, url+endpoint, response -> data_display.setText(response), error -> data_display.setText("Error: " + error));

        mRQueue.add(mSReq);


    }

    private void GetRequest2(TextView data_display) {

        endpoint = "get_test1";

        mRQueue = Volley.newRequestQueue(Requests.this);
        mSReq = new StringRequest(Request.Method.GET, url+endpoint, response -> data_display.setText(response), error -> data_display.setText("Error: " + error));

        mRQueue.add(mSReq);



    }
    private void GetRequest3(TextView data_display) {

        endpoint = "get_json1"; // also needs params

        mRQueue = Volley.newRequestQueue(Requests.this);
        mSReq = new StringRequest(Request.Method.GET, (url+endpoint), response -> data_display.setText(response), error -> data_display.setText("Error: " + error));

        mRQueue.add(mSReq);


        //Intent intent = new Intent(this, Requests.class);
        //startActivity(intent);
    }
    /*
    private void GetRequest4(TextView data_display) {

        endpoint = "print_json";

        mRQueue = Volley.newRequestQueue(Requests.this);
        mSReq = new StringRequest(Request.Method.GET, (url+endpoint), response -> data_display.setText(response), error -> data_display.setText("Error: " + error));

        mRQueue.add(mSReq);


        //Intent intent = new Intent(this, Requests.class);
        //startActivity(intent);
    }
    private void PostRequest(TextView data_display) {

        endpoint = "print_json_with_param";

        mRQueue = Volley.newRequestQueue(Requests.this);
        mSReq = new StringRequest(Request.Method.GET, (url+endpoint), response -> data_display.setText(response), error -> data_display.setText("Error: " + error));

        mRQueue.add(mSReq);


        //Intent intent = new Intent(this, Requests.class);
        //startActivity(intent);
    }

     */
}