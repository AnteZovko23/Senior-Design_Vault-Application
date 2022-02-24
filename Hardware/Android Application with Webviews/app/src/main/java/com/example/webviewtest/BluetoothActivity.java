package com.example.webviewtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;


public class BluetoothActivity extends AppCompatActivity {
    private Button btnPaired;
    private ListView deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        btnPaired = (Button) findViewById(R.id.button);
        deviceList = (ListView) findViewById(R.id.deviceList);
    }
}