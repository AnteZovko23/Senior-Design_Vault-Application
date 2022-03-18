package com.example.webviewtest.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.annotation.Nullable;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import android.app.LoaderManager.LoaderCallbacks;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.webviewtest.Camera1;
import com.example.webviewtest.MainActivity;
import com.example.webviewtest.R;
import com.example.webviewtest.ui.login.LoginViewModel;
import com.example.webviewtest.ui.login.LoginViewModelFactory;
import com.example.webviewtest.databinding.ActivityLoginBinding;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

   }