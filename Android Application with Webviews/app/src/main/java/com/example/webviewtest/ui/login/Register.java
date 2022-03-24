package com.example.webviewtest.ui.login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.webviewtest.R;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity
{
     Button btn2_signup;
     EditText user_name, pass_word;
     FirebaseAuth mAuth;

     @Override
    protected void onCreate(Bundle savedInstance) {

         super.onCreate(savedInstance);
         setContentView(R.layout.activity_register);
         user_name=findViewById(R.id.username);
         pass_word=findViewById(R.id.password);
         btn2_signup=findViewById(R.id.sign);
         mAuth=FirebaseAuth.getInstance();
         btn2_signup.setOnClickListener(v -> {
             String email = user_name.getText().toString().trim();
             String password = pass_word.getText().toString().trim();

             if(email.isEmpty()){
                 user_name.setError("Email is empty, Please enter a valid email address.");
                 user_name.requestFocus();
                 return;
             }
             if(password.isEmpty()){
                 pass_word.setError("Enter your preferred password.");
                 pass_word.requestFocus();
                 return;
             }
             if(password.length() > 6){
                 pass_word.setError("Your password is too short. Please enter one that is more than 6 characters!");
                 pass_word.requestFocus();
                 return;
             }
             mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                 if(task.isSuccessful()){
                     Toast.makeText(Register.this, "You are now registered!", Toast.LENGTH_SHORT).show();
                 }
                 else{
                     Toast.makeText(Register.this, "You are not Registered, Please try again.", Toast.LENGTH_SHORT).show();
                 }
             });
         });


     }
}
