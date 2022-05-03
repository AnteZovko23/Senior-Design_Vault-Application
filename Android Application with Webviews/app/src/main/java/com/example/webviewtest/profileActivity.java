package com.example.webviewtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.webviewtest.databinding.ActivityProfileBinding;
import com.example.webviewtest.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class profileActivity extends AppCompatActivity {

    private @NonNull ActivityProfileBinding binding;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private DocumentReference userDoc;

    String phoneNumber = "Phone Number: ", name = "Display Name";

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
        binding.PhoneNumber.setVisibility(View.GONE);
        checkUser();

        setViews();

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

        binding.UserDName.setText("Display Name:\n"+name);
        binding.emailInfo.setText("Email:\n"+firebaseUser.getEmail());
        if(firebaseUser.getPhotoUrl() != null)
            binding.userImage.setImageURI(firebaseUser.getPhotoUrl());
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