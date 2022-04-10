package com.example.webviewtest.ui.login;



import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.webviewtest.Camera1;
import com.example.webviewtest.MainActivity;
import com.example.webviewtest.R;

import com.example.webviewtest.data.localUsers;

import com.example.webviewtest.databinding.ActivityLoginBinding;
import com.example.webviewtest.fireBaseWork;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;


public class LoginActivity extends AppCompatActivity {

    fireBaseWork dbMan = fireBaseWork.getInstance();

    private ActivityLoginBinding binding;
    //actionbar
    private ActionBar actionBar;
    //progress dialog
    private ProgressDialog progressDialog;

    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient googleSignInClient;
    private FirebaseStorage userStorage;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;

    private FirebaseStorage storage;

    private static final String TAG = "GOOGLE_SIGN_IN_TAG";

    private Button signIn, signUp;
    private String email = "", password = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();


        //configure progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Logging into your account...");
        progressDialog.setCanceledOnTouchOutside(false);


        // declare buttons and views/editTexts
        signIn = findViewById(R.id.login);
        signUp = findViewById(R.id.signUp);

        //configure actionbar, title, back button
        actionBar = getSupportActionBar();
        actionBar.setTitle("Login");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //configure Google sign in
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // hardcoded web_client_id; code was .requestIdToken(getString(R.string.default_web_client_id))
                //.requestIdToken(("945302376649-vi8je73koov4bs717npduv0ttq0c1neu.apps.googleusercontent.com"))
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);



        //signUp button
        binding.signUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                progressDialog.dismiss();
                finish();
            }
        });

        //googleSignIn button (click to begin google sign in)
        binding.gSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // begin google sign in
                Log.d(TAG, "onClick: begin Google SignIn");
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
                //sActivityResultLauncher.launch(intent);
            }
        });

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validate data
                validateData();
            }
        });

    }


    private void validateData()
    {
        //get data
        email = binding.email.getText().toString().trim();
        password = binding.password.getText().toString().trim();

        //validate data
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
        else
        {
            // valid data, now continue firebase login
            firebaseLogIn();
        }
    }

    private void firebaseLogIn()
    {
        //show progress
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //login successful
                        //get user info
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        String email = firebaseUser.getEmail();
                        Toast.makeText(LoginActivity.this, "Logged In\n"+email, Toast.LENGTH_SHORT).show();

                        //open main activity
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        progressDialog.dismiss();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //login failed, get and show error message
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN)
        {
            Log.d(TAG, "onActivityResult: Google SignIn intent result");
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try
            {
                // google sign in success
                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                firebaseAuthWithGoogleAccount(account);
            }
            catch (Exception e)
            {
                //failed google sign in
                Log.d(TAG, "onActivityResult: "+e.getMessage());
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
                            // need salt variable to pass to ensure non-conflicting things
                            dbMan.newUser(firebaseUser.getDisplayName());
                            storage.getReference(firebaseUser.getDisplayName()+"/"); // firstname lastname -> pictures (store subject name_number)

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
