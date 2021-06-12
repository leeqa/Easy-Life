package com.example.easyapplication.Main.BudgetManager.DetailBudget.ExpenseFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class ExpenseFragment extends Fragment {
    ArrayList<String> idArray;
    ArrayList<String> datesArray;
    ArrayList<String> categoriesArray;
    ArrayList<String> amountsArray;
    Context context;
    ProgressBar expense_received_progressBar;
    TextView expense_received_textView, expense_current_amount;
    ListView expense_fragment_listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_expense, container, false);
        expense_fragment_listView = view.findViewById(R.id.expense_fragment_listView);
        expense_received_progressBar = view.findViewById(R.id.expense_received_progressBar);
        expense_received_textView = view.findViewById(R.id.expense_received_textView);
        expense_current_amount = view.findViewById(R.id.expense_current_amount);
        idArray = new ArrayList<>();
        datesArray = new ArrayList<>();
        categoriesArray = new ArrayList<>();
        amountsArray = new ArrayList<>();
        context = getContext();
        receiverFromExpenseRowAdapter();
        updateCurrentAmountReceiver();
        readData();
        return view;
    }

    private void receiverFromExpenseRowAdapter() {
        Objects.requireNonNull(getActivity()).getApplicationContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                readData();
            }
        }, new IntentFilter("hexCoders.easyApplication.expense"));
    }
    private void updateCurrentAmountReceiver() {
        Objects.requireNonNull(getActivity()).getApplicationContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferencesGetSet sharedPreferencesGetSet = new SharedPreferencesGetSet();
                expense_current_amount.setText(sharedPreferencesGetSet.getCurrentBudget(context));
            }
        }, new IntentFilter("hexCoders.easyApplication.budget_current_amount"));
    }

    private void readData() {
        idArray.clear();
        datesArray.clear();
        amountsArray.clear();
        categoriesArray.clear();
        expense_fragment_listView.setAdapter(null);
        expense_received_progressBar.setVisibility(View.VISIBLE);
        SharedPreferencesGetSet sharedPreferencesGetSet = new SharedPreferencesGetSet();
        expense_current_amount.setText(sharedPreferencesGetSet.getCurrentBudget(context));
//        String currentDate = sharedPreferencesGetSet.getCurrentBudget(context);
        Calendar calendar = Calendar.getInstance();
        String month = getMonthForInt(calendar.get(Calendar.MONTH));
//        String budgetName = sharedPreferencesGetSet.getCurrentBudgetName(context);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference easyApp = FirebaseDatabase.getInstance().getReference("App Members")
                .child(auth.getCurrentUser().getUid()).child("Budget Reminder").child(DetailedBudgetActivity.selectedBudgetMonth);
        easyApp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Expense")) {
                    DatabaseReference easyApp2 = easyApp.child("Expense");
                    easyApp2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot user : snapshot.getChildren()) {
                                String id = user.getKey();
                                String date = user.child("date").getValue().toString();
                                String categories = user.child("category").getValue().toString();
                                String amounts = user.child("amount").getValue().toString();
                                if (!idArray.contains(id)) {
                                    idArray.add(id);
                                    datesArray.add(date);
                                    categoriesArray.add(categories);
                                    amountsArray.add(amounts);
                                    if (datesArray != null && categoriesArray != null && amountsArray != null && idArray != null) {
                                        ExpenseRowAdapter expenseRowAdapter = new ExpenseRowAdapter(context, idArray, datesArray, categoriesArray, amountsArray);
                                        expense_fragment_listView.setAdapter(expenseRowAdapter);
                                        expense_received_progressBar.setVisibility(View.GONE);
                                    } else {
                                        expense_received_progressBar.setVisibility(View.GONE);
                                        expense_received_textView.setVisibility(View.VISIBLE);
                                    }
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                    expense_received_progressBar.setVisibility(View.GONE);
                    expense_received_textView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
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