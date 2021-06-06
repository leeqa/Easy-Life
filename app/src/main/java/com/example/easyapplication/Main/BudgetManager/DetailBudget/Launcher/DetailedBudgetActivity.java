package com.example.easyapplication.Main.BudgetManager.DetailBudget.Launcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyapplication.Main.BudgetManager.DetailBudget.Adapter.ViewPageAdapter;
import com.example.easyapplication.Main.BudgetManager.DetailBudget.AddExpenseToBudget.AddExpenseActivity;
import com.example.easyapplication.Main.BudgetManager.DetailBudget.AddRavenueToBudget.AddRavenueActivity;
import com.example.easyapplication.Main.BudgetManager.Home.BudgetHomeActivity;
import com.example.easyapplication.Main.Home.MainActivity;
import com.example.easyapplication.R;
import com.example.easyapplication.Utilities.SharedPreferencesGetSet;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class DetailedBudgetActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView detail_budget_bck, detail_budget_add;
    TextView ravenue_current_amount, budget_month;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_budget);

        //Status bar color change as per activity design
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorAccentLight));
        }



        detail_budget_bck = findViewById(R.id.detail_budget_bck);
        detail_budget_bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailedBudgetActivity.this, BudgetHomeActivity.class));
                finish();
            }
        });
        detail_budget_add = findViewById(R.id.detail_budget_add);
        ravenue_current_amount = findViewById(R.id.ravenue_current_amount);
        ravenue_current_amount.setText("Rs"+BudgetHomeActivity.BUDGETONLINE);
        budget_month = findViewById(R.id.budget_month);
        budget_month.setText(BudgetHomeActivity.BUDGETNAME);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#1976d2"));
        tabLayout.setTabTextColors(Color.parseColor("#A39FA3"), Color.parseColor("#1976d2"));
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new ViewPageAdapter(getSupportFragmentManager()));
        fragmentChanging();


        tabLayout.setupWithViewPager(viewPager);
        detail_budget_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewPager.getCurrentItem() == 0) {
                    //Expense Add
                    Intent intent = new Intent(DetailedBudgetActivity.this, AddExpenseActivity.class);
                    startActivity(intent);
                    finish();
                } else if (viewPager.getCurrentItem() == 1) {
                    //Ravenue Add
                    Intent intent = new Intent(DetailedBudgetActivity.this, AddRavenueActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DetailedBudgetActivity.this, BudgetHomeActivity.class));
        finish();
    }


    private void fragmentChanging() {
        PagerAdapter pagerAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(2);
    }
}