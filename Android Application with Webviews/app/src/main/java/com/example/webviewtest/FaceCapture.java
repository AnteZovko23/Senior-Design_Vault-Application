package com.example.webviewtest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FaceCapture extends AppCompatActivity {

    // Define the pic id
    private static final int pic_id = 123;
    private FirebaseStorage userStorage;
    private FirebaseUser currUser;
    private DocumentReference userDoc;
    private CollectionReference users;

    // Define the button and imageview type variable
    Button camera_open_id;
    ImageView click_image_id;
    String name, storagePath, uriPath, fileName, suffix;
    Uri picUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_capture);

        // By ID we can get each component
        // which id is assigned in XML file
        // get Buttons and imageview.
        camera_open_id = (Button)findViewById(R.id.camera_button);
        click_image_id = (ImageView)findViewById(R.id.click_image);
        userStorage = FirebaseStorage.getInstance("gs://the-vault-7cf31.appspot.com");



        name = currUser.getDisplayName();
        suffix = "";
        userDoc = users.document(name);
        storagePath = name+"/";
        fileName = uriPath ="";

        // Camera_open button is for open the camera
        // and add the setOnClickListener in this button
        camera_open_id.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {

                // Create the camera_intent ACTION_IMAGE_CAPTURE
                // it will open the camera for capture the image
                Intent camera_intent
                        = new Intent(MediaStore
                        .ACTION_IMAGE_CAPTURE);

                // Start the activity with camera_intent,
                // and request pic id
                startActivityForResult(camera_intent, pic_id);
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

            // BitMap is data structure of image file
            // which stores the image in memory
            Bitmap photo = (Bitmap) data.getExtras()
                    .get("data");

            // Set the image in imageview for display
            click_image_id.setImageBitmap(photo);
            sendPic(photo);
        }
    }

    private void setFileName()
    {
        // first use picture's name (if none, prompt user for name)

        //
        fileName = name+suffix+".jpg";
        Log.d("current User: ", name);
    }

    private void sendPic(Bitmap bitmap)
    {
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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, baos);
        byte[] b = baos.toByteArray();

        /*  // old idea to encode pic as base 64 string and sending it to FireStore (not using now)
        String smileyPic = Base64.encodeToString(b, Base64.DEFAULT);
        /**/

        // uploads picture (in form of byte array)
        //  might want to add in metadata (nice-to-have development feature to help with debugging
        //  from firebase website console viewpoint)
        StorageReference storageRef = userStorage.getReference(storagePath);
        setFileName();
        StorageReference smileyImgRef = storageRef.child(fileName);

        UploadTask uploadTask = smileyImgRef.putBytes(b);
        uploadTask.addOnSuccessListener(FaceCapture.this, new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
            }
        });


}}