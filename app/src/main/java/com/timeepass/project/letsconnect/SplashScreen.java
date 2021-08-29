package com.timeepass.project.letsconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    ImageView imageView;
    TextView nameTv;
    long animTime = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        imageView = findViewById(R.id.splashscreenlogo);
        nameTv = findViewById(R.id.tv_splash_name);

        ObjectAnimator animatorY = ObjectAnimator.ofFloat(imageView, "y", 400f);
        ObjectAnimator animatorname = ObjectAnimator.ofFloat(nameTv, "x", 200f);

        animatorY.setDuration(animTime);
        animatorname.setDuration(animTime);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorY, animatorname);
        animatorSet.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4000);
    }
}