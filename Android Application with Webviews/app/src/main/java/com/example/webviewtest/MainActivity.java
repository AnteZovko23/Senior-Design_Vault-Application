package com.example.webviewtest;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
    private Button profilebtn;
    private Button Data;
    private Button facebutton;
    private Button pickerbutton;
    private Toolbar toolbar;
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
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        t1 = new NotificationThread();
        t1.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.appmenu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
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
            case R.id.bluetooth2:
                openBluetooth();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
