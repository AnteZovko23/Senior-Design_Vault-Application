package com.example.webviewtest;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
}