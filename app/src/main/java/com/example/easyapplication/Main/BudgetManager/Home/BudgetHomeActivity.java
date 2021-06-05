package com.example.easyapplication.Main.BudgetManager.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyapplication.Main.BudgetManager.AddBudget.AddBudgetActivity;
import com.example.easyapplication.Main.BudgetManager.DetailBudget.Launcher.DetailedBudgetActivity;
import com.example.easyapplication.R;
import com.example.easyapplication.Utilities.SharedPreferencesGetSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class BudgetHomeActivity extends AppCompatActivity {
    ProgressBar budgetProgressbar;
    TextView budgetText, budget_current_month, budget_total_amount_pkr, budget_total_amount;
    //    String budget;
    String budgetOnline, totalBudgetOnline;
    //    String remainingBudget;
    SharedPreferencesGetSet sharedPreferencesGetSet;
    ProgressBar activity_budget_home_progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_home);
        sharedPreferencesGetSet = new SharedPreferencesGetSet();
        budget_total_amount_pkr = findViewById(R.id.budget_total_amount_pkr);
        budget_total_amount = findViewById(R.id.budget_total_amount);
        budgetProgressbar = findViewById(R.id.budget_progressbar);
        budgetProgressbar.setEnabled(false);
        activity_budget_home_progressbar = findViewById(R.id.activity_budget_home_progressbar);
        activity_budget_home_progressbar.setVisibility(View.VISIBLE);
        Calendar calendar = Calendar.getInstance();
        String month = getMonthForInt(calendar.get(Calendar.MONTH));
        String budgetName = month + "," + calendar.get(Calendar.YEAR);
        getRemainingBudget();
//        budget = sharedPreferencesGetSet.getCurrentBudget(getApplicationContext());
//        remainingBudget=sharedPreferencesGetSet.getCurrentTotalBudget(getApplicationContext());
        budgetText = findViewById(R.id.budget_txt);
        budget_current_month = findViewById(R.id.budget_current_month);
        budget_current_month.setText(budgetName);
        budgetProgressbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (budgetText.getText().toString().equals("Add")) {
                    Intent intent = new Intent(BudgetHomeActivity.this, AddBudgetActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(BudgetHomeActivity.this, DetailedBudgetActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void getRemainingBudget() {
        SharedPreferencesGetSet sharedPreferencesGetSet = new SharedPreferencesGetSet();
        String budgetName = sharedPreferencesGetSet.getCurrentBudgetName(getApplicationContext());
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference easyApp = FirebaseDatabase.getInstance().getReference("App Members")
                .child(auth.getCurrentUser().getUid()).child("Budget Reminder").child(budgetName);
        easyApp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                budgetOnline = (String) snapshot.child("Remaining Amount").getValue();
                totalBudgetOnline = (String)snapshot.child("Total").getValue();
                //        if (budget.equals("")) {
                if (budgetOnline == null) {
                    activity_budget_home_progressbar.setVisibility(View.GONE);
                    budgetText.setText("Add");
                    budgetProgressbar.setEnabled(true);
                } else {
                    activity_budget_home_progressbar.setVisibility(View.GONE);
//            budgetText.setText(budget);
                    budgetText.setText(budgetOnline);
                    budgetProgressbar.setEnabled(true);
                }
                if (totalBudgetOnline == null) {
                    budget_total_amount_pkr.setVisibility(View.GONE);
                    budget_total_amount.setVisibility(View.GONE);
                } else {
                    budget_total_amount_pkr.setVisibility(View.VISIBLE);
                    budget_total_amount.setVisibility(View.VISIBLE);
                    budget_total_amount_pkr.setText(totalBudgetOnline);

                }
            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        });
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

    @Override
    protected void onResume() {
        getRemainingBudget();
        super.onResume();
    }
}