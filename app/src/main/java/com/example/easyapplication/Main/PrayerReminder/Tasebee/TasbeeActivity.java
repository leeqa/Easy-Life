package com.example.easyapplication.Main.PrayerReminder.Tasebee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.easyapplication.R;

public class TasbeeActivity extends AppCompatActivity {
    Button addBtn, clearBtn;
    TextView tasbee_txt;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasbee);
        addBtn = findViewById(R.id.add_btn);
        clearBtn = findViewById(R.id.clear_btn);
        tasbee_txt = findViewById(R.id.tasbee_txt);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                tasbee_txt.setText(String.valueOf(i));
            }
        });
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=0;
                tasbee_txt.setText(String.valueOf(i));
            }
        });

    }
}