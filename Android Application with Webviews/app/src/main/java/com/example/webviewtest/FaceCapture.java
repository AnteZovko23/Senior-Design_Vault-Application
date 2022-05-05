package com.example.webviewtest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;

public class FaceCapture extends AppCompatActivity {

    // Define the pic id
    private static final int pic_id = 123;
    private FirebaseStorage userStorage;
    private StorageReference storageRef;
    private FirebaseUser currUser;
    private FirebaseAuth firebaseAuth;
    private DocumentReference userDoc;
    private CollectionReference users;
    private EditText nameInput;
    private ProgressBar spinner;

    // Define the button and imageview type variable
    Button camera_open_id;
    String storagePath, uriPath, fileName, suffix, name;
    Uri picUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_capture);
        nameInput = findViewById(R.id.nameInput);
        spinner = findViewById(R.id.progressBar3);
        spinner.setVisibility(View.GONE);

        stop_feed();

        // By ID we can get each component
        // which id is assigned in XML file
        // get Buttons and imageview.
        camera_open_id = (Button)findViewById(R.id.camera_button);
        userStorage = FirebaseStorage.getInstance("gs://the-vault-7cf31.appspot.com");
        firebaseAuth = FirebaseAuth.getInstance();
        currUser = firebaseAuth.getCurrentUser();

        name = nameInput.getText().toString();

        suffix = "";
        storagePath = name+"/";
        fileName = uriPath ="";

        // Camera_open button is for open the camera
        // and add the setOnClickListener in this button
        camera_open_id.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                name = nameInput.getText().toString();
                SystemClock.sleep(1000);
                if(!(name.equals(""))) {
                    // Create the camera_intent ACTION_IMAGE_CAPTURE
                    // it will open the camera for capture the image
                    Intent camera_intent
                            = new Intent(MediaStore
                            .ACTION_IMAGE_CAPTURE);

                    // Start the activity with camera_intent,
                    // and request pic id
                    startActivityForResult(camera_intent, pic_id);
                }
            }
        });
    }

    // This method will help to retrieve the image
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        // Match the request 'pic id with requestCode
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {

            try {
                // BitMap is data structure of image file
                // which stores the image in memory
                Bitmap photo = (Bitmap) data.getExtras()
                        .get("data");

                // Set the image in imageview for display
                sendPic(photo);
            }
            catch (Exception NullPointerException)
            {
                Log.d("Camera debug", "No picture taken.");
            }
        }
    }

    private void setFileName()
    {
        fileName = name+suffix+".jpg";

        // first use picture's name (if none, prompt user for name)
        storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                System.out.println("Uploaded successfully!");
                start_face_processing();
                Handler handler = new Handler();
                Runnable r=new Runnable() {
                    public void run() {
                        spinner.setVisibility(View.GONE);
                        Toast.makeText(FaceCapture.this, "Success!", Toast.LENGTH_SHORT).show();
                    }
                };
                handler.postDelayed(r, 12000);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("could not list storage reference");
                Log.d("no List", "could not list storage reference");
            }
        });

        fileName = name+suffix+".jpg";

    }

    private void sendPic(Bitmap bitmap) {
        spinner.setVisibility(View.VISIBLE);
        // gets the specific smiley png
        // implement an image chooser here

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
        uploadTask.addOnSuccessListener(FaceCapture.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload successful");
                start_face_processing();
            }

        });
    }

    private void start_face_processing() {
        // Tell volley to use a SocketFactory from our SSLContext

        String url = "http://192.168.1.5:5000/add_face?name=" + name;
        RequestQueue mRQueue;
        StringRequest mSReq;
        mRQueue = Volley.newRequestQueue(FaceCapture.this);
        mSReq = new StringRequest(Request.Method.GET, url, response -> {}, error -> {});

        mRQueue.add(mSReq);

    }

    private void stop_feed() {
        // Tell volley to use a SocketFactory from our SSLContext

        String url = "http://192.168.1.5:5000/stop_feed";
        RequestQueue mRQueue;
        StringRequest mSReq;
        mRQueue = Volley.newRequestQueue(FaceCapture.this);
        mSReq = new StringRequest(Request.Method.GET, url, response -> {}, error -> {});

        mRQueue.add(mSReq);

    }

    private void start_feed() {
        // Tell volley to use a SocketFactory from our SSLContext

        System.out.println("Starting feed");

        String url = "http://192.168.1.5:5000/start_feed";
        RequestQueue mRQueue;
        StringRequest mSReq;
        mRQueue = Volley.newRequestQueue(FaceCapture.this);
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
    public void print(String str) {
        System.out.println(str);
    }
    @Override
    public void onBackPressed() {
        start_feed();
        super.onBackPressed();
    }
}