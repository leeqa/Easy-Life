package com.example.easyapplication.Main.BudgetManager.DetailBudget.ExpenseFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.easyapplication.Main.BudgetManager.DetailBudget.AddRavenueToBudget.AddRavenueActivity;
import com.example.easyapplication.Main.BudgetManager.DetailBudget.Launcher.DetailedBudgetActivity;
import com.example.easyapplication.R;
import com.example.easyapplication.Utilities.SharedPreferencesGetSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ExpenseRowAdapter extends BaseAdapter {
    ProgressBar progressBar;
    TextView expense_date;
    ImageView category_icon;
    TextView expense_name;
    TextView expense_amount;
    ImageButton expense_delete;
    ArrayList<String> idArray;
    ArrayList<String> dates;
    ArrayList<String> categories;
    ArrayList<String> amounts;
    Context context;
    String budgetName;
    private LayoutInflater layoutInflater;


    public ExpenseRowAdapter(String budgetName, Context context, ArrayList<String> idArray, ArrayList<String> dates, ArrayList<String> categories, ArrayList<String> amounts) {
        this.context = context;
        this.idArray = idArray;
        this.dates = dates;
        this.categories = categories;
        this.amounts = amounts;
        this.budgetName = budgetName;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.expense_fragment_row, null);
            progressBar = view.findViewById(R.id.expense_progressbar);
            expense_date = view.findViewById(R.id.expense_date);
            expense_name = view.findViewById(R.id.expense_name);
            expense_amount = view.findViewById(R.id.expense_amount);
            category_icon = view.findViewById(R.id.category_icon);
            expense_date.setText(dates.get(i));

            expense_name.setText(categories.get(i));

            if (categories.get(i).equals("General")) {
                category_icon.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_general));
            } else if (categories.get(i).equals("Clothes")) {
                category_icon.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_clothes));
            } else if (categories.get(i).equals("Entertainment")) {
                category_icon.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_entertainment));
            } else if (categories.get(i).equals("Food")) {
                category_icon.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_food));
            } else if (categories.get(i).equals("Health")) {
                category_icon.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_health));
            } else if (categories.get(i).equals("Household")) {
                category_icon.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_household));
            } else if (categories.get(i).equals("Hygiene")) {
                category_icon.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_hygiene));
            } else if (categories.get(i).equals("Pets")) {
                category_icon.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_pets));
            } else if (categories.get(i).equals("Presents")) {
                category_icon.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_presents));
            } else if (categories.get(i).equals("Sports")) {
                category_icon.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_sports));
            } else if (categories.get(i).equals("Transportation")) {
                category_icon.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_transport));
            } else {
                category_icon.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_general));
            }

            expense_amount.setText("Rs"+amounts.get(i));
            expense_delete = view.findViewById(R.id.expense_delete);
            String amount = amounts.get(i);
            expense_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expense_delete.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    String id = idArray.get(i);
                    deleteFromList(id, amount);
                }

            });
        }
        return view;
    }

    private void deleteFromList(String id, String amount) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference easyApp = FirebaseDatabase.getInstance().getReference("App Members")
                .child(auth.getCurrentUser().getUid());
        easyApp.child("Budget Reminder").child(budgetName).child("Expense").child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                setRemainingBudget(amount);
            }
        });
    }

    public void setRemainingBudget(String amount) {
        SharedPreferencesGetSet sharedPreferencesGetSet = new SharedPreferencesGetSet();
        String currentBudgetAmount = sharedPreferencesGetSet.getCurrentBudget(context);
        String newAmount = getRemainingAmount(currentBudgetAmount, amount);
        String budgetName = sharedPreferencesGetSet.getCurrentBudgetName(context);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference easyApp = FirebaseDatabase.getInstance().getReference("App Members")
                .child(auth.getCurrentUser().getUid());
        easyApp.child("Budget Reminder").child(budgetName).child("Remaining Amount").setValue(newAmount).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent("hexCoders.easyApplication.expense");
                context.sendBroadcast(intent);
                expense_delete.setEnabled(true);
                sharedPreferencesGetSet.setCurrentBudget(newAmount, context);
                progressBar.setVisibility(View.GONE);
                Intent intent1 = new Intent("hexCoders.easyApplication.budget_current_amount");
                context.sendBroadcast(intent1);
            }
        });
    }

    private String getRemainingAmount(String currentBudgetAmount, String amount) {
        int current = Integer.parseInt(currentBudgetAmount);
        int revenue = Integer.parseInt(amount);
        int remaining = current + revenue;
        return String.valueOf(remaining);

    }
}
