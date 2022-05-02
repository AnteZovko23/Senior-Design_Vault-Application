package com.example.webviewtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import com.example.webviewtest.databinding.ActivityUpdateProfileBinding;
import com.example.webviewtest.databinding.ActivityVerifyPhoneBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class updateProfile extends AppCompatActivity {

    private @NonNull
    ActivityUpdateProfileBinding binding;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private DocumentReference userDoc;

    String newEmail = "", newPhone = "", newName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userDoc = db.collection("user_Info").document(firebaseUser.getDisplayName());


        // logout button
        binding.submitChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do phone verification stuff
                newEmail = binding.newEmail.getText().toString().trim();
                newName = binding.newName.getText().toString().trim();
                newPhone = binding.newPhone.getText().toString().trim();

                verifyEntries();

                // go to profile activity
                startActivity(new Intent(updateProfile.this, profileActivity.class));
                finish();
            }
        });
    }

        private void verifyEntries()
        {
            //validate data
            if (!TextUtils.isEmpty(newEmail)) {
                if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                    //email format is invalid, don't proceed further
                    binding.newEmail.setError("Invalid email format");
                } else {
                    firebaseUser.updateEmail(newEmail);
                }
            }

            if (!TextUtils.isEmpty(newPhone)) {
                if (!Patterns.PHONE.matcher(newEmail).matches()) {
                    //email format is invalid, don't proceed further
                    binding.newEmail.setError("Invalid phone number format");
                } else {
                    /*
                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(firebaseAuth)
                                    .setPhoneNumber(newPhone)       // Phone number to verify
                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity(this)                 // Activity (for callback binding)
                                    .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                    PhoneAuthCredential phoneAuthCredential =
                    firebaseUser.updatePhoneNumber(newPhone);
                    */
                    Log.d("Phone num", "Got to the phone stuff");
                }
            }

            if (!TextUtils.isEmpty(newName)) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(newName).build();
                firebaseUser.updateProfile(profileUpdates);

            }


        }


}