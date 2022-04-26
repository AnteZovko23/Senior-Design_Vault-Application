package com.example.webviewtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.webviewtest.databinding.ActivityProfileBinding;
import com.example.webviewtest.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class profileActivity extends AppCompatActivity {

    private @NonNull ActivityProfileBinding binding;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
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

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        startActivity(new Intent(profileActivity.this, MainActivity.class));
        finish();
        return true;
    }
}