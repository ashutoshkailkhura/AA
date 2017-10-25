package com.example.android.aa.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.aa.R;


public class SplashScreen extends AppCompatActivity {

    private static int SPLACE_TIME_OUT = 3000;
    TextView logo, discription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        animateLogo();
        animateDiscription();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, UserLogIn.class));
                finish();
            }
        }, SPLACE_TIME_OUT);
    }

    private void animateLogo() {
        logo = (TextView) findViewById(R.id.splash_logo);
        logo.animate().alpha(1).setDuration(2000);
    }
    private void animateDiscription() {
        discription = (TextView) findViewById(R.id.splash_discription);
        discription.animate().scaleXBy(4).scaleYBy(4).setDuration(2000);
    }
}
