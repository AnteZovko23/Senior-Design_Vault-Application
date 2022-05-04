package com.example.webviewtest;

import static com.example.webviewtest.R.id.bluetooth2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class PickImageActivity extends AppCompatActivity {
    private Button gallery;
    private ImageView imageView;
    private FirebaseStorage userStorage;
    private StorageReference storageRef;
    private FirebaseUser currUser;
    private ProgressBar spinner;
    private EditText nameInput;
    String name, storagePath, uriPath, fileName, suffix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_image);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavPick);

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

        stop_feed();

        userStorage = FirebaseStorage.getInstance("gs://the-vault-7cf31.appspot.com");

        name = nameInput.getText().toString();

        suffix = "";
        storagePath = name+"/";
        fileName = uriPath ="";

        gallery = (Button) findViewById(R.id.gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameInput.getText().toString();
                SystemClock.sleep(1000);
                if(!(name.equals(""))) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 3);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imageView = findViewById(R.id.imageView);
            imageView.setImageURI(selectedImage);
            sendPic(imageView);
        }
    }

    private void sendPic(ImageView imageView)
    {
        // gets the specific smiley png
        // implement an image chooser here
        spinner.setVisibility(View.VISIBLE);
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        //openFileChooser();

        //bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),picUri);

        //Log.d("filePath: ", filePath);
        //dbData.setText(filePath);
        //bitmap = BitmapFactory.decodeFile(filePath);
        //rotatePic(pic);


        // converts picture to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();

        /*  // old idea to encode pic as base 64 string and sending it to FireStore (not using now)
        String smileyPic = Base64.encodeToString(b, Base64.DEFAULT);
        /**/

        // uploads picture (in form of byte array)
        //  might want to add in metadata (nice-to-have development feature to help with debugging
        //  from firebase website console viewpoint)
        storageRef = userStorage.getReference(storagePath);
        setFileName();
        StorageReference smileyImgRef = storageRef.child(fileName);

        UploadTask uploadTask = smileyImgRef.putBytes(b);
        uploadTask.addOnSuccessListener(PickImageActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                System.out.println("Uploaded successfully!");
                start_face_processing();
                Handler handler = new Handler();
                Runnable r=new Runnable() {
                    public void run() {
                        spinner.setVisibility(View.GONE);
                        Toast.makeText(PickImageActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                    }
                };
                handler.postDelayed(r, 12000);
            }
        });

    }

    private void setFileName()
    {
        fileName = name+suffix+".jpg";

        // first use picture's name (if none, prompt user for name)
        storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                int found = 0;
                for (StorageReference item : listResult.getItems())
                {
                    if (item.toString() == fileName) {
                        suffix = String.valueOf(++found);

                        Log.d("item exists", item.toString());
                    }
                }

                if (found > 0)
                {
                    suffix = String.valueOf(found);
                }
            }
        });

        fileName = name+suffix+".jpg";
    }

    private void start_face_processing() {
        // Tell volley to use a SocketFactory from our SSLContext

        String url = "http://192.168.1.5:5000/add_face?name=" + name;
        RequestQueue mRQueue;
        StringRequest mSReq;
        mRQueue = Volley.newRequestQueue(PickImageActivity.this);
        mSReq = new StringRequest(Request.Method.GET, url, response -> {}, error -> {});

        mRQueue.add(mSReq);

    }

    private void stop_feed() {
        // Tell volley to use a SocketFactory from our SSLContext

        String url = "http://192.168.1.5:5000/stop_feed";
        RequestQueue mRQueue;
        StringRequest mSReq;
        mRQueue = Volley.newRequestQueue(PickImageActivity.this);
        mSReq = new StringRequest(Request.Method.GET, url, response -> {}, error -> {});

        mRQueue.add(mSReq);

    }

    private void start_feed() {
        // Tell volley to use a SocketFactory from our SSLContext

        System.out.println("Starting feed");

        String url = "http://192.168.1.5:5000/start_feed";
        RequestQueue mRQueue;
        StringRequest mSReq;
        mRQueue = Volley.newRequestQueue(PickImageActivity.this);
        mSReq = new StringRequest(Request.Method.GET, url, response -> {}, error -> {});

        mRQueue.add(mSReq);

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
    @Override
    public void onBackPressed() {
        start_feed();
        super.onBackPressed();
    }
}