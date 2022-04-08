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
import com.example.webviewtest.databinding.ActivityRegisterBinding;
import com.example.webviewtest.databinding.ActivitySignUpBinding;
import com.example.webviewtest.fireBaseWork;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class SignUpActivity extends AppCompatActivity
{
    private @NonNull ActivitySignUpBinding binding;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private fireBaseWork db;
    private FirebaseStorage storage;

    //action bar
    private ActionBar actionBar;

    private Button signUp;
    private EditText emailTxt, passwordTxt, displayNameTxt;
    private String email = "", password = "", displayName = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        db = fireBaseWork.getInstance();
        storage = FirebaseStorage.getInstance("gs://the-vault-7cf31.appspot.com");

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
        /*
             emailTxt = findViewById(R.id.email);
             passwordTxt = findViewById(R.id.password1);
        /**/
        //displayNameTxt = findViewById(R.id.person);
        //signUp = findViewById(R.id.SignUp);

        binding.SignUp.setOnClickListener(new View.OnClickListener()
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
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        finish();
        return true;
    }

    private void validateData()
    {
        // get data
        email = binding.email.getText().toString().trim();
        password = binding.password1.getText().toString().trim();
        displayName = binding.person.getText().toString().trim();

        // validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            //email format is invalid, don't proceed further
            binding.email.setError("Invalid email format");
        }
        else if (TextUtils.isEmpty(password))
        {
            // no password is entered
            binding.password1.setError("Enter password");
        }
        else if (password.length()<6)
        {
            // password length less than 6
            binding.password1.setError("Password must be at least 6 characters long");
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
                        db.newUser(displayName);
                        storage.getReference(displayName+"/pictures"); // firstname lastname -> pictures (store subject name_number)

                        String email = firebaseUser.getEmail();
                        Toast.makeText(SignUpActivity.this, "Account created\n", Toast.LENGTH_SHORT).show();

                        //open profile activity (change to relevant activity)
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //signup failed
                        Toast.makeText(SignUpActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
