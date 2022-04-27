package com.example.webviewtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;

public class PickImageActivity extends AppCompatActivity {
    private Button gallery;
    private ImageView imageView;
    private FirebaseStorage userStorage;
    private FirebaseUser currUser;
    private ProgressBar spinner;

    String name, storagePath, uriPath, fileName, suffix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_image);

        userStorage = FirebaseStorage.getInstance("gs://the-vault-7cf31.appspot.com");

        try {
            name = currUser.getDisplayName();
        }

        catch(Exception NullPointerException) {
            name = "Ante";
        }
        suffix = "";
        storagePath = name+"/";
        fileName = uriPath ="";
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        gallery = (Button) findViewById(R.id.gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
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
            spinner.setVisibility(View.VISIBLE);
        }
    }

    private void sendPic(ImageView imageView)
    {
        // gets the specific smiley png
        // implement an image chooser here
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
        StorageReference storageRef = userStorage.getReference(storagePath);
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
                spinner.setVisibility(View.GONE);
                Toast.makeText(PickImageActivity.this, "Success!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void setFileName()
    {
        // first use picture's name (if none, prompt user for name)

        //
        fileName = name+suffix+".jpg";
    }

    private void start_face_processing() {
        // Tell volley to use a SocketFactory from our SSLContext

        String url = "http://192.168.1.5:5000/add_face?name=" + name;
        RequestQueue mRQueue;
        StringRequest mSReq;
        mRQueue = Volley.newRequestQueue(PickImageActivity.this);
//        try {
//            HttpsURLConnection.setDefaultSSLSocketFactory(Certificate_Handling.getSocketFactory(this));
//
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
        mSReq = new StringRequest(Request.Method.GET, url, response -> {}, error -> {});

        mRQueue.add(mSReq);

    }

}