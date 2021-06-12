package com.example.easyapplication.Main.BudgetManager.DetailBudget.Launcher;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.easyapplication.Main.BudgetManager.DetailBudget.Adapter.ViewPageAdapter;
import com.example.easyapplication.Main.BudgetManager.DetailBudget.AddExpenseToBudget.AddExpenseActivity;
import com.example.easyapplication.Main.BudgetManager.DetailBudget.AddRavenueToBudget.AddRavenueActivity;
import com.example.easyapplication.Main.BudgetManager.Home.BudgetHomeActivity;
import com.example.easyapplication.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;

public class DetailedBudgetActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView detail_budget_bck, detail_budget_add, iv_left_arrow, iv_right_arrow;
    TextView ravenue_current_amount, budget_month;
    int index = 0;
    int currentMonth;
    ArrayList<String> months = new ArrayList<>();
    public static String selectedBudgetMonth = null;
//    ArrayList<String> monthlyAmount = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_budget);

        currentMonth = index = Calendar.getInstance().get(Calendar.MONTH);

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
        budget_month = findViewById(R.id.budget_month);

        iv_left_arrow = findViewById(R.id.iv_left_arrow);
        iv_right_arrow = findViewById(R.id.iv_right_arrow);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        AddList();

        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#1976d2"));
        tabLayout.setTabTextColors(Color.parseColor("#A39FA3"), Color.parseColor("#1976d2"));
        viewPager = findViewById(R.id.viewPager);

//        viewPager.setAdapter(new ViewPageAdapter(getSupportFragmentManager()));
//        fragmentChanging();

        budget_month.setText(getBudgetMonth(currentMonth));

        tabLayout.setupWithViewPager(viewPager);
        detail_budget_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(index != currentMonth){
                    Toast.makeText(DetailedBudgetActivity.this, "Please select current month", Toast.LENGTH_SHORT).show();
                    return;
                }
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

    private void AddList() {

        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");

        iv_right_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (index<currentMonth && index>=0){

                    index++;
//                    ravenue_current_amount.setText("Rs"+monthlyAmount.get(index));
                    budget_month.setText(getBudgetMonth(index));
                }
            }
        });

        iv_left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index<=currentMonth && index>=0){

                    if (index>0){
                        index--;
                    }
//                    ravenue_current_amount.setText("Rs"+monthlyAmount.get(index));
                    budget_month.setText(getBudgetMonth(index));

                }
            }
        });
    }
    private String getBudgetMonth(int index){
        selectedBudgetMonth = months.get(index)+",2021";
        loadData();
        return selectedBudgetMonth;
    }
    private void loadData(){
        FirebaseDatabase.getInstance().getReference("App Members")
                .child(FirebaseAuth.getInstance().getUid())
                .child("Budget Reminder")
                .child(DetailedBudgetActivity.selectedBudgetMonth)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                        if(snapshot.hasChild("Remaining Amount"))
                            ravenue_current_amount.setText("Rs " + snapshot.child("Remaining Amount").getValue()+"");
                        else
                            ravenue_current_amount.setText("Rs -----");

                        fragmentChanging();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }


    private void fragmentChanging() {
        PagerAdapter pagerAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(2);
    }
}