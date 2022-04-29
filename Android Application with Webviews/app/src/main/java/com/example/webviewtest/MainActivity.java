package com.example.webviewtest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {
    private Button lockbutton;
    private Button cambutton;
    private Button alarmbutton;
    private Button bluetoothbtn;
    private Button requestsbtn;
    private Button Data;
    private Button facebutton;
    private Button pickerbutton;
    NotificationThread t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start_feed();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel= new NotificationChannel("My Notification","My Notification",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager =getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        t1 = new NotificationThread();
        t1.start();

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

        facebutton = (Button) findViewById(R.id.facebutton);
        facebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFace();
            }
        });

        pickerbutton = (Button) findViewById(R.id.pickbutton);
        pickerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPicker();
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
        Intent intent = new Intent(this, Bluetooth.class);
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

    public void openFace() {
        Intent intent = new Intent(this, FaceCapture.class);
        startActivity(intent);
    }

    public void openPicker() {
        Intent intent = new Intent(this, PickImageActivity.class);
        startActivity(intent);
    }

    class NotificationThread extends Thread {
        private String input = "A stranger has been detected.";

        @Override
        public void run() {
            while (true) {
                SystemClock.sleep(1000);
                httpRequest();
                System.out.println("yo");
            }
        }

        public void httpRequest() {
            String url = "http://192.168.1.5:5000/get_notification";
            RequestQueue mRQueue;
            StringRequest mSReq;
            mRQueue = Volley.newRequestQueue(MainActivity.this);
            mSReq = new StringRequest(Request.Method.GET, url, response -> {
                System.out.println(response);
                if(response.equals("Unknown")) {
                    String message="There is a stranzger at your door";
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this,"My Notification");
                    builder.setContentTitle("Stranger Danger");
                    builder.setContentText(message);
                    builder.setSmallIcon(R.drawable.ic_bulb);
                    builder.setAutoCancel(true);
                    NotificationManagerCompat managerCompat=NotificationManagerCompat.from(MainActivity.this);
                    managerCompat.notify(1,builder.build());
                }

            }, error -> {});
            mRQueue.add(mSReq);
        }
    }

    private void start_feed() {
        // Tell volley to use a SocketFactory from our SSLContext

        System.out.println("Starting feed");

        String url = "http://192.168.1.5:5000/start_feed";
        RequestQueue mRQueue;
        StringRequest mSReq;
        mRQueue = Volley.newRequestQueue(MainActivity.this);
        mSReq = new StringRequest(Request.Method.GET, url, response -> {}, error -> {});

        mRQueue.add(mSReq);

    }
}