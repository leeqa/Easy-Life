package com.example.easyapplication.Splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;

import com.example.easyapplication.LogIn.UserLogin;
import com.example.easyapplication.Main.Home.MainActivity;
import com.example.easyapplication.R;
import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {

    FirebaseAuth auth;


    private static int SPLASH_TIME_OUT=3000;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth= FirebaseAuth.getInstance();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {



                if (auth.getCurrentUser()!= null) {
                    Intent Home1= new Intent(Splash.this, MainActivity.class);
                    startActivity(Home1);
                    finish();
                }
                else {
                    Intent n1 = new Intent(Splash.this, OnBoarding.class);
                startActivity(n1);
                finish();
            }



        }


    },SPLASH_TIME_OUT);
}

}


