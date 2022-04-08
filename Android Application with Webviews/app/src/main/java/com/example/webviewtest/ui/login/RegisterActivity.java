package com.example.webviewtest.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.app.ActionBar;

import com.example.webviewtest.R;
import com.example.webviewtest.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity
{
    private ActivityLoginBinding binding;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    //action bar
    private ActionBar actionBar;

    private Button signUp;
    private String email = "", password = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {

             super.onCreate(savedInstanceState);
             binding = ActivityLoginBinding.inflate(getLayoutInflater());
             setContentView(binding.getRoot());

             // init firebase auth
             firebaseAuth = FirebaseAuth.getInstance();

             //configure actionbar, title, back button
             actionBar = getSupportActionBar();
             actionBar.setTitle("SignUp");
             actionBar.setDisplayHomeAsUpEnabled(true);
             actionBar.setDisplayShowHomeEnabled(true);


             //configure progress dialog
             progressDialog = new ProgressDialog(this);
             progressDialog.setTitle("Please wait");
             progressDialog.setMessage("Creating your account...");
             progressDialog.setCanceledOnTouchOutside(false);

             // declare buttons and views/editTexts
             signUp = findViewById(R.id.SignUp);

             binding.signUp.setOnClickListener(new View.OnClickListener()
             {
                 @Override
                 public void onClick(View v)
                 {
                     validateData();
                 }
             });
     }

     @Override
     public boolean onSupportNavigateUp()
     {
         onBackPressed(); //got at previous activity when back button of actionbar clicked
         startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
         finish();
         return true;
     }

    private void validateData()
    {
        // get data
        email = binding.email.getText().toString().trim();
        password = binding.password.getText().toString().trim();

        // validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            //email format is invalid, don't proceed further
            binding.email.setError("Invalid email format");
        }
        else if (TextUtils.isEmpty(password))
        {
            // no password is entered
            binding.password.setError("Enter password");
        }
        else if (password.length()<6)
        {
            // password length less than 6
            binding.password.setError("Password must be at least 6 characters long");
        }
        else
        {
            // valid data, now continue firebase signup
            firebaseSignUp();
        }
    }

    private void firebaseSignUp()
    {
        // show progress
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //signup success
                        progressDialog.dismiss();
                        //get user Info
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        String email = firebaseUser.getEmail();
                        Toast.makeText(RegisterActivity.this, "Account created\n", Toast.LENGTH_SHORT).show();

                        //open profile activity (change to relevant activity)
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //signup failed
                        Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
