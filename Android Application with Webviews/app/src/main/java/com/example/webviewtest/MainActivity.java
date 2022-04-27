package com.example.webviewtest;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
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
    private Notification notification;
    NotificationThread t1;
    public static NotificationCompat.Builder strangerNotification;
    public static int strangerNotificationID = 101;
    public static NotificationManagerCompat notificationManager;
    private static String CHANNEL_ID = "vault_notifications";
    private static boolean strangerDetected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        private SharedPreferences sharedPreferences;
        private int userCountdownTime;
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

                if(response.equals("nothing")) {
                    sendNotification();
                }

            }, error -> {});
            mRQueue.add(mSReq);
        }

        private void sendNotification() {
            Intent notificationIntent = new Intent(MainActivity.this, MainActivity.class);
            notificationIntent.putExtra("stranger", true);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);

            strangerNotification = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                    .setContentTitle("The Vault")
                    .setContentText(input)
                    .setSmallIcon(R.drawable.smiley);

            notificationManager = NotificationManagerCompat.from(MainActivity.this);
            notificationManager.notify(strangerNotificationID, strangerNotification.build());
        }
    }
}