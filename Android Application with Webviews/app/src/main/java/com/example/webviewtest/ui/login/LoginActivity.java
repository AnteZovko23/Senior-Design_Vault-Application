package com.example.webviewtest.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.webviewtest.MainActivity;
import com.example.webviewtest.R;
import com.example.webviewtest.fireBaseWork;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity
{
    private EditText user_name, pass_word;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user_name=findViewById(R.id.email);
        pass_word=findViewById(R.id.password);
        Button btn_login = findViewById(R.id.btn_login);
        Button btn_sign = findViewById(R.id.btn_signup);
        mAuth=FirebaseAuth.getInstance();
        btn_login.setOnClickListener(v ->
        {
            String email = user_name.getText().toString().trim();
            String password=pass_word.getText().toString().trim();

        if(email.isEmpty())
        {
            user_name.setError("Email is empty");
            user_name.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            user_name.setError("Enter the valid email");
            user_name.requestFocus();
            return;
        }

        if(password.isEmpty())
        {
            pass_word.setError("Password is empty");
            pass_word.requestFocus();
            return;
        }

        if(password.length()<6)
        {
            pass_word.setError("The Length of your password is less than 6 characters");
            pass_word.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
            Toast.makeText(LoginActivity.this, "Please Check your Login Credentials", Toast.LENGTH_SHORT).show();

        });
        });

        btn_sign.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, Register.class)));
    }
}
