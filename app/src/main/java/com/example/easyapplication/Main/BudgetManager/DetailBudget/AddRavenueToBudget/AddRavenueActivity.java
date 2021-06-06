package com.example.easyapplication.Main.BudgetManager.DetailBudget.AddRavenueToBudget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.easyapplication.Main.BudgetManager.DetailBudget.AddExpenseToBudget.AddExpenseActivity;
import com.example.easyapplication.Main.BudgetManager.DetailBudget.AddExpenseToBudget.ExpenseModel;
import com.example.easyapplication.Main.BudgetManager.DetailBudget.Launcher.DetailedBudgetActivity;
import com.example.easyapplication.R;
import com.example.easyapplication.Utilities.SharedPreferencesGetSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class AddRavenueActivity extends AppCompatActivity {
    ImageView add_revenue_bck;
    TextView add_revenue_current_date_txt;
    EditText add_revenue_edit_txt;
    Button add_revenue_btn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ravenue);

        //Status bar color change as per activity design
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        Calendar calendar = Calendar.getInstance();
        String month = getMonthForInt(calendar.get(Calendar.MONTH));
        String currentDate = calendar.get(Calendar.DAY_OF_MONTH) + " " + month + ", " + calendar.get(Calendar.YEAR);
        SharedPreferencesGetSet sharedPreferencesGetSet = new SharedPreferencesGetSet();
        String budgetName = sharedPreferencesGetSet.getCurrentBudgetName(getApplicationContext());
        add_revenue_bck = findViewById(R.id.add_revenue_bck);
        add_revenue_current_date_txt = findViewById(R.id.add_revenue_current_date_txt);
        add_revenue_current_date_txt.setText(currentDate);
        add_revenue_edit_txt = findViewById(R.id.add_revenue_edit_txt);
        add_revenue_btn = findViewById(R.id.add_revenue_btn);
        progressBar = findViewById(R.id.add_revenue_progressbar);
        add_revenue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String date = add_revenue_current_date_txt.getText().toString();
                String amount = add_revenue_edit_txt.getText().toString();
                RevenueModel revenueModel = new RevenueModel(date, amount);
                FirebaseAuth auth = FirebaseAuth.getInstance();
                DatabaseReference easyApp = FirebaseDatabase.getInstance().getReference("App Members")
                        .child(auth.getCurrentUser().getUid());
                //to create id random id
                String id = easyApp.push().getKey();
                easyApp.child("Budget Reminder").child(budgetName).child("Revenue").child(id).setValue(revenueModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        setRemainingBudget(amount);
                    }
                });

            }
        });
        add_revenue_bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddRavenueActivity.this, DetailedBudgetActivity.class));
                finish();
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

    public void setRemainingBudget(String amount) {
        SharedPreferencesGetSet sharedPreferencesGetSet = new SharedPreferencesGetSet();
        String currentBudgetAmount = sharedPreferencesGetSet.getCurrentBudget(getApplicationContext());
        String newAmount = getRemainingAmount(currentBudgetAmount, amount);
        String budgetName = sharedPreferencesGetSet.getCurrentBudgetName(getApplicationContext());
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference easyApp = FirebaseDatabase.getInstance().getReference("App Members")
                .child(auth.getCurrentUser().getUid());
        easyApp.child("Budget Reminder").child(budgetName).child("Remaining Amount").setValue(newAmount).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                sharedPreferencesGetSet.setCurrentBudget(newAmount, getApplicationContext());
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(AddRavenueActivity.this, DetailedBudgetActivity.class));
                finish();
            }
        });
    }

    private String getRemainingAmount(String currentBudgetAmount, String amount) {
        int current = Integer.parseInt(currentBudgetAmount);
        int revenue = Integer.parseInt(amount);
        int remaining = current + revenue;
        return String.valueOf(remaining);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddRavenueActivity.this, DetailedBudgetActivity.class));
        finish();
    }
}