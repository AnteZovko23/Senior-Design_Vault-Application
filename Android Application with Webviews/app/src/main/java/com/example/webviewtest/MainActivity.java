package com.example.webviewtest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private Button lockbutton;
    private Button cambutton;
    private Button alarmbutton;
    private Button bluetoothbtn;
    private Button requestsbtn;
    private Button Data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lockbutton = (Button) findViewById(R.id.lockbutton);
        lockbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLock();
            }
        });

        cambutton = (Button) findViewById(R.id.cambutton);
        cambutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });

        alarmbutton = (Button) findViewById(R.id.alarmbutton);
        alarmbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlarm();
            }
        });

        bluetoothbtn = (Button) findViewById(R.id.bluetoothbtn);
        bluetoothbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBluetooth();
            }
        });

        requestsbtn = (Button) findViewById(R.id.requestsbtn);
        requestsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRequests();
            }
        });

        Data = (Button) findViewById(R.id.Data);
        Data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openData();
            }
        });
    }

    public void openLock() {
        Intent intent = new Intent(this, Lock.class);
        startActivity(intent);
    }

    public void openCamera() {
        Intent intent = new Intent(this, Camera.class);
        startActivity(intent);
    }

    public void openAlarm() {
        Intent intent = new Intent(this, Alarm.class);
        startActivity(intent);
    }

    public void openBluetooth() {
        Intent intent = new Intent(this, BluetoothActivity.class);
        startActivity(intent);
    }

    public void openRequests() {
        Intent intent = new Intent(this, Requests.class);
        startActivity(intent);
    }

    public void openData() {
        Intent intent = new Intent(this, DBdata_bytearray.class);
        startActivity(intent);
    }
}