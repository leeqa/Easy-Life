package com.example.easyapplication.Main.BudgetManager.DetailBudget.AddExpenseToBudget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyapplication.Main.BudgetManager.DetailBudget.AddRavenueToBudget.AddRavenueActivity;
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

public class AddExpenseActivity extends AppCompatActivity {
    ImageView add_expense_bck;
    TextView add_expense_current_date_txt;
    Spinner add_expense_spinner;
    EditText add_expense_edit_txt;
    Button add_expense_btn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        //Status bar color change as per activity design
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        setSpinnerAdapter();
        Calendar calendar = Calendar.getInstance();
        String month = getMonthForInt(calendar.get(Calendar.MONTH));
        String currentDate = calendar.get(Calendar.DAY_OF_MONTH) + " " + month + ", " + calendar.get(Calendar.YEAR);
        SharedPreferencesGetSet sharedPreferencesGetSet = new SharedPreferencesGetSet();
        String budgetName = sharedPreferencesGetSet.getCurrentBudgetName(getApplicationContext());
        progressBar = findViewById(R.id.add_expense_progressbar);
        add_expense_bck = findViewById(R.id.add_expense_bck);
        add_expense_current_date_txt = findViewById(R.id.add_expense_current_date_txt);
        add_expense_current_date_txt.setText(currentDate);
        add_expense_edit_txt = findViewById(R.id.add_expense_edit_txt);
        add_expense_btn = findViewById(R.id.add_expense_btn);
        add_expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String category = (String) add_expense_spinner.getSelectedItem();
                String date = add_expense_current_date_txt.getText().toString();
                String amount = add_expense_edit_txt.getText().toString();
                ExpenseModel expenseModel = new ExpenseModel(date, category, amount);
                FirebaseAuth auth = FirebaseAuth.getInstance();
                DatabaseReference easyApp = FirebaseDatabase.getInstance().getReference("App Members")
                        .child(auth.getCurrentUser().getUid());
                //to create id random id
                String id = easyApp.push().getKey();
                easyApp.child("Budget Reminder").child(budgetName).child("Expense").child(id).setValue(expenseModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        setRemainingBudget(amount);
                    }
                });

            }
        });
        add_expense_bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddExpenseActivity.this, DetailedBudgetActivity.class));
                finish();
            }
        });
    }

    private void setRemainingBudget(String amount) {
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
                startActivity(new Intent(AddExpenseActivity.this, DetailedBudgetActivity.class));
                finish();
            }
        });
    }

    private String getRemainingAmount(String currentBudgetAmount, String amount) {
        int current = Integer.parseInt(currentBudgetAmount);
        int expense = Integer.parseInt(amount);
        int remaining = current - expense;
        return String.valueOf(remaining);

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
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddExpenseActivity.this, DetailedBudgetActivity.class));
        finish();
    }

    private void setSpinnerAdapter() {
        add_expense_spinner = findViewById(R.id.add_expense_spinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.expense_categories,
                R.layout.spinner_item);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        add_expense_spinner.setAdapter(arrayAdapter);
    }
}