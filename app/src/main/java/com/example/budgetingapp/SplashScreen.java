package com.example.budgetingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    private static  int splash = 3000;
    Animation animation;
    private ImageView imageView;
    private TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        animation = AnimationUtils.loadAnimation(this,R.anim.animation);
        imageView = findViewById(R.id.imageView);
        appName = findViewById(R.id.appName);

    }
}