package com.example.webviewtest;

import static com.example.webviewtest.R.id.bluetooth2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.webviewtest.databinding.ActivityProfileBinding;
import com.example.webviewtest.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import io.grpc.Context;

public class profileActivity extends AppCompatActivity {

    private @NonNull ActivityProfileBinding binding;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private DocumentReference userDoc;
    private FirebaseStorage userStorage;
    private StorageReference storageRef;
    private StorageReference storageRef2;

    String phoneNumber = "Phone Number: ", name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        name = firebaseUser.getDisplayName();
        db = FirebaseFirestore.getInstance();
        userDoc = db.collection("user_Info").document(name);
        userStorage = FirebaseStorage.getInstance("gs://the-vault-7cf31.appspot.com");
        storageRef = userStorage.getReference(name+"/");
        storageRef2 = userStorage.getReference();

        binding.PhoneNumber.setVisibility(View.GONE);
        checkUser();

        binding.userImage.setVisibility(View.VISIBLE);
        setViews();

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


        // logout button
        binding.logoutBtn.setOnClickListener(new View.OnClickListener()
        {
           @Override
           public void onClick(View v)
           {
               firebaseAuth.signOut();
               checkUser();
           }
        });

        // logout button
        binding.changeInfo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(profileActivity.this, updateProfile.class));
                finish();

            }
        });

    }

    private void setViews()
    {
        checkPhone();

        binding.DisName.setText("Display Name:");
        binding.DisEmail.setText("Email:");
        binding.UserDName.setText(name);
        binding.emailInfo.setText(firebaseUser.getEmail());

        // set picture
        StorageReference userPic = storageRef.child(name+".jpg");
        StorageReference userPic2 = storageRef2.child(name+".jpg");

        final long maxSize = 1024*1024*20; // approximately 20 megabytes

        // userPic2 looks for image in the highest directory (similar to root)
        userPic2.getBytes(maxSize).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap finalPic = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                binding.userImage.setImageBitmap(finalPic);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                binding.userImage.setVisibility(View.GONE);
            }
        });
        // userPic looks for the picture in the current user's folder (called the same as the user name)
        /*
        userPic.getBytes(maxSize).addOnSuccessListener(new OnSuccessListener<byte[]>()
        {
            @Override
            public void onSuccess(byte[] ba)
            {
                Bitmap finalPic = BitmapFactory.decodeByteArray(ba, 0, ba.length);
                binding.userImage.setImageBitmap(finalPic);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                binding.userImage.setVisibility(View.GONE);
            }
        });
        /**/

    }

    private void checkPhone()
    {
        binding.PhoneNumber.setText(phoneNumber);
        if (firebaseUser.getPhoneNumber() != null)
            binding.PhoneNumber.setText("Phone Number:\n"+firebaseUser.getPhoneNumber());
        else {
            userDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot doc = task.getResult();
                                                            if (doc != null && doc.exists()) {
                                                                fireBaseWork fb = fireBaseWork.getInstance();
                                                                phoneNumber = fb.decodeData("phone_number", doc.getString("phone_number"));
                                                            }


                                                        } else {
                                                            Log.d("FireBase: ", "failed");
                                                        }
                                                    }

                                                }
            );
            binding.PhoneNumber.setText(phoneNumber);
        }
    }

    private void checkUser()
    {
        // ensure we have a user logged in
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null)
        {
            //user not logged in, move to login screen
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }
        else
        {
            //user logged in, get info
            //get email, maybe password, user image, phone number, etc.
            // set to relevant textViews
        }
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
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        startActivity(new Intent(profileActivity.this, MainActivity.class));
        finish();
        return true;
    }
}