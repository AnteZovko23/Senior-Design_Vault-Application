package com.example.webviewtest;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Layout;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.transition.Scene;
import androidx.transition.TransitionManager;

import org.w3c.dom.Text;

public class WelcomeActivity extends AppCompatActivity {
    ImageView WelcomeView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        WelcomeView = (ImageView) findViewById(R.id.WelcomeView);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideinbottom);
        WelcomeView.setAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                SystemClock.sleep(2000);
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });{
        };

    }

}
