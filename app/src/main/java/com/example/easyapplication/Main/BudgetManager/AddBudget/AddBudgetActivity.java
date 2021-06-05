package com.example.easyapplication.Main.BudgetManager.AddBudget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.easyapplication.Main.BudgetManager.Home.BudgetHomeActivity;
import com.example.easyapplication.R;
import com.example.easyapplication.Utilities.SharedPreferencesGetSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class AddBudgetActivity extends AppCompatActivity {
    ImageView add_budget_back;
    EditText add_budget_edit_txt;
    Button add_budget_btn;
    FirebaseAuth auth;
    ProgressBar activity_add_budget_progressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget);
        add_budget_back = findViewById(R.id.add_budget_back);
        add_budget_edit_txt = findViewById(R.id.add_budget_edit_txt);
        add_budget_btn = findViewById(R.id.add_budget_btn);
        activity_add_budget_progressbar = findViewById(R.id.activity_add_budget_progressbar);
        Calendar calendar = Calendar.getInstance();
        String month = getMonthForInt(calendar.get(Calendar.MONTH));
        String budgetName = month + "," + calendar.get(Calendar.YEAR);
        auth = FirebaseAuth.getInstance();
        add_budget_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_budget_btn.setClickable(false);
                activity_add_budget_progressbar.setVisibility(View.VISIBLE);
                String budget = add_budget_edit_txt.getText().toString();
                if (!add_budget_edit_txt.getText().toString().equals("")) {
                    DatabaseReference easyApp = FirebaseDatabase.getInstance().getReference("App Members")
                            .child(auth.getCurrentUser().getUid());
                    easyApp.child("Budget Reminder").child(budgetName).child("Total").setValue(budget).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            SharedPreferencesGetSet sharedPreferencesGetSet = new SharedPreferencesGetSet();
                            sharedPreferencesGetSet.setCurrentBudget(budget, getApplicationContext());
                            sharedPreferencesGetSet.setCurrentTotalBudget(budget, getApplicationContext());
                            sharedPreferencesGetSet.setCurrentBudgetName(budgetName, getApplicationContext());
                            activity_add_budget_progressbar.setVisibility(View.GONE);
                            add_budget_btn.setClickable(true);
                            startActivity(new Intent(AddBudgetActivity.this, BudgetHomeActivity.class));
                            finish();
                        }
                    });
                    easyApp.child("Budget Reminder").child(budgetName).child("Remaining Amount").setValue(budget);
                } else {
                    Toast.makeText(AddBudgetActivity.this, "Please Enter The Budget", Toast.LENGTH_SHORT).show();
                }

            }
        });
        add_budget_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddBudgetActivity.this, BudgetHomeActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddBudgetActivity.this, BudgetHomeActivity.class));
        finish();
    }

    public String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }
}