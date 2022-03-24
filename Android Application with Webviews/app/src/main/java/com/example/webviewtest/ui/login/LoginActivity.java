package com.example.webviewtest.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.webviewtest.MainActivity;
import com.example.webviewtest.R;
import com.example.webviewtest.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class LoginActivity extends AppCompatActivity {

    private EditText user_name, pass_word;
    FirebaseAuth mAuth;
    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;

    private static final String TAG = "GOOGLE_SIGN_IN_TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        /**/
        // configure in-house username, etc.
        setContentView(R.layout.activity_login);
        user_name = findViewById(R.id.username);
        pass_word = findViewById(R.id.password);
        Button btn_login = findViewById(R.id.login);
        Button btn_sign = findViewById(R.id.signup);
        mAuth = FirebaseAuth.getInstance();
        btn_login.setOnClickListener(v ->
        {
            String email = user_name.getText().toString().trim();
            String password = pass_word.getText().toString().trim();

            if (email.isEmpty()) {
                user_name.setError("Email is empty");
                user_name.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                user_name.setError("Enter the valid email");
                user_name.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                pass_word.setError("Password is empty");
                pass_word.requestFocus();
                return;
            }

            if (password.length() < 6) {
                pass_word.setError("The Length of your password is less than 6 characters");
                pass_word.requestFocus();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "Please Check your Login Credentials", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        btn_sign.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, Register.class)));


        //configure Google sign in
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()

                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        //googleSignIn button (click to begin google sign in)
        binding.gSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // begin google sign in
                Log.d(TAG, "onClick: begin Google SignIn");
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
                //sActivityResultLauncher.launch(intent);
            }
        });
        //signup.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, Register.class)));

/*
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory()).get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if(loginFormState == null){
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if(loginFormState.getUsernameError() != null){
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if(loginResult == null){
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if(loginResult.getError() != null){
                    showLoginFailed(loginResult.getError());
                }
                if(loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);
                finish();
            }

            private void showLoginFailed(@StringRes Integer errorString) {
                Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
            }

            private void updateUiWithUser(LoggedInUserView model) {
                String welcome = getString(R.string.welcome) + model.getDisplayName();
                // TODO: initiate successful logged in experience
                Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
            }
        });
>>>>>>> Stashed changes

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
<<<<<<< Updated upstream
=======
        /**/

    }
        @Override
        protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == RC_SIGN_IN) {
                Log.d(TAG, "onActivityResult: Google SignIn intent result");
                Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // google sign in success
                    GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                    firebaseAuthWithGoogleAccount(account);
                } catch (Exception e) {
                    //failed google sign in
                    Log.d(TAG, "onActivityResult: " + e.getMessage());
                }
            }
        }



    private void firebaseAuthWithGoogleAccount(GoogleSignInAccount account)
    {
        Log.d(TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener(new OnSuccessListener<AuthResult>()
            {
                @Override
                public void onSuccess(AuthResult authResult)
                {
                    //login success
                    Log.d(TAG, "onSuccess: logged in");

                    //get logged in user
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    //get user info
                    String uid = firebaseUser.getUid();
                    String email = firebaseUser.getEmail();

                    Log.d(TAG, "onSuccess: Email: "+email);
                    Log.d(TAG, "onSuccess: UID: "+uid);

                    //check if user is new or existing
                    if (authResult.getAdditionalUserInfo().isNewUser())
                    {
                        //user is new - Account Created
                        Log.d(TAG, "onSuccess: Account Created...\n"+email);
                        Toast.makeText(LoginActivity.this, "Account Created...\n"+email, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Log.d(TAG, "onSuccess: Existing user...\n"+email);
                        Toast.makeText(LoginActivity.this, "Existing user...\n"+email, Toast.LENGTH_SHORT).show();
                    }

                    //start profile/main activity
                    openMainActivity();
                }
            })
            .addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    //login failure
                    Log.d(TAG, "onFailure: "+e.getMessage());
                }
            });
    }



    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}
