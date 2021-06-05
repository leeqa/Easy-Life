package com.example.easyapplication.Main.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.easyapplication.LogIn.UserLogin;
import com.example.easyapplication.Main.BudgetManager.Home.BudgetHomeActivity;
import com.example.easyapplication.Main.MedicineReminder.Home.MedicineHome;
import com.example.easyapplication.Main.PrayerReminder.Home.PrayerReminderActivity;
import com.example.easyapplication.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    LinearLayout sign_out_btn, medicine_reminder_btn,prayer_reminder_btn,budget_manager_btn;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sign_out_btn = findViewById(R.id.sign_out_btn);
        medicine_reminder_btn = findViewById(R.id.medicine_reminder_btn);
        auth = FirebaseAuth.getInstance();
        sign_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auth != null) {
                    auth.signOut();
                    Intent Home = new Intent(MainActivity.this, UserLogin.class);
                    startActivity(Home);
                    finishAffinity();
                    finish();
                }

            }
        });
        medicine_reminder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MedicineHome.class);
                startActivity(intent);
            }
        });
        prayer_reminder_btn=findViewById(R.id.prayer_reminder_btn);
        prayer_reminder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PrayerReminderActivity.class);
                startActivity(intent);
            }
        });
        budget_manager_btn=findViewById(R.id.budget_manager_btn);
        budget_manager_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BudgetHomeActivity.class);
                startActivity(intent);
            }
        });
    }
}