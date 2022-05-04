package com.example.webviewtest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.webviewtest.data.localUsers;
import com.example.webviewtest.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;

public class SignUpActivity extends AppCompatActivity
{
    private @NonNull ActivitySignUpBinding binding;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private fireBaseWork db;
    private FirebaseStorage storage;
    private localUsers usersD;


    private Button SignUp;
    private EditText emailTxt, passwordTxt, displayNameTxt;
    private String email = "", password = "", displayName = "", phoneNumber = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        db = fireBaseWork.getInstance();
        storage = FirebaseStorage.getInstance("gs://the-vault-7cf31.appspot.com");

        //configure progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Creating your account...");
        progressDialog.setCanceledOnTouchOutside(false);

        // declare buttons and views/editTexts
             emailTxt = findViewById(R.id.email);
             passwordTxt = findViewById(R.id.password1);

        displayNameTxt = findViewById(R.id.person);
        SignUp = findViewById(R.id.SignUp);

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
    public void onBackPressed()
    {
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        finish();
    }

    private void validateData()
    {
        // get data
        email = binding.email.getText().toString().trim();
        password = binding.password1.getText().toString().trim();
        displayName = binding.person.getText().toString().trim();
        phoneNumber = binding.phoneN.getText().toString().trim();

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
        else    // possible PhoneNumberUtils.formatNumber(number, normalizedNumber, countryISO)
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
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(displayName).build();
                        firebaseUser.updateProfile(profileUpdates);

                        firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                    Log.d("Verify Email status", "Email sent");
                            }});

                        db.newUser(displayName, phoneNumber);
                        storage.getReference(displayName+"/"); // firstname lastname -> pictures (store subject name_number)


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
