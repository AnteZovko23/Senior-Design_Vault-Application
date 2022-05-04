package com.example.webviewtest;

import static com.example.webviewtest.R.id.bluetooth2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    private Button lockbutton;
    private Button cambutton;
    private Button alarmbutton;
    private Button bluetoothbtn;
    private Button profilebtn;
    private Button Data;
    private Button facebutton;
    private Button pickerbutton;
    private Toolbar toolbar;
    NotificationThread t1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.pickbutton2:
                        openPicker();
                        return true;
                    case R.id.profile2:
                        openProfile();
                        return true;
                    case R.id.cameras2:
                        openCamera();
                        return true;
                    case R.id.addface2:
                        openFace();
                        return true;
                    case bluetooth2:
                        openBluetooth();
                        return true;
                }
                return false;
            }
        });

        start_feed();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel= new NotificationChannel("My Notification","My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager =getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        t1 = new NotificationThread();
        t1.start();

    }

    public void openCamera() {
        Intent intent = new Intent(this, Camera.class);
        startActivity(intent);
    }

    public void openBluetooth() {
        Intent intent = new Intent(this, Bluetooth.class);
        startActivity(intent);
    }

    public void openProfile() {
        Intent intent = new Intent(this, profileActivity.class);
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

    class NotificationThread extends Thread {
        private String input = "A stranger has been detected.";

        @Override
        public void run() {
            while (true) {
                SystemClock.sleep(1000);
                httpRequest();
                System.out.println("notifications");
            }
        }

        public void httpRequest() {
            String url = "http://192.168.1.5:5000/get_notification";
            RequestQueue mRQueue;
            StringRequest mSReq;
            mRQueue = Volley.newRequestQueue(MainActivity.this);
            mSReq = new StringRequest(Request.Method.GET, url, response -> {
                System.out.println(response);
                String message;
                if(response.equals("nothing") == false) {
                    if(response.equals("Unknown")) {
                        message = "There is a stranger at your door";
                    }
                    else {
                        message = response + " is at your door";
                    }
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this,"My Notification");
                    builder.setContentTitle("Person Detected");
                    builder.setContentText(message);
                    builder.setSmallIcon(R.drawable.ic_baseline_arrow_back_24);
                    builder.setAutoCancel(true);
                    NotificationManagerCompat managerCompat=NotificationManagerCompat.from(MainActivity.this);
                    managerCompat.notify(1,builder.build());
                }

            }, error -> {});
            mRQueue.add(mSReq);
            SystemClock.sleep(2000);
        }
    }
}
