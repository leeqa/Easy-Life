package com.example.easyapplication.Main.BudgetManager.DetailBudget.RavenueFragment;

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

import com.example.easyapplication.Main.BudgetManager.DetailBudget.AddRavenueToBudget.RevenueModel;
import com.example.easyapplication.Main.BudgetManager.DetailBudget.ExpenseFragment.ExpenseRowAdapter;
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
import java.util.List;
import java.util.Objects;

public class RavenueFragment extends Fragment {
    Context context;
    ProgressBar ravenue_received_progressBar;
    TextView ravenue_received_textView, ravenue_current_amount;
    ListView ravenue_fragment_listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_ravenue, container, false);
        ravenue_fragment_listView = view.findViewById(R.id.ravenue_fragment_listView);
        ravenue_received_progressBar = view.findViewById(R.id.ravenue_received_progressBar);
        ravenue_received_textView = view.findViewById(R.id.ravenue_received_textView);
        ravenue_current_amount = view.findViewById(R.id.ravenue_current_amount);

        context = getContext();
        receiverFromRavenueRowAdapter();
        updateCurrentAmountReceiver();
        readData();
        return view;
    }

    private void receiverFromRavenueRowAdapter() {
        Objects.requireNonNull(getActivity()).getApplicationContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                readData();
            }
        }, new IntentFilter("hexCoders.easyApplication.ravenue"));
    }

    private void updateCurrentAmountReceiver() {
        Objects.requireNonNull(getActivity()).getApplicationContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferencesGetSet sharedPreferencesGetSet = new SharedPreferencesGetSet();
                ravenue_current_amount.setText(sharedPreferencesGetSet.getCurrentBudget(context));
            }
        }, new IntentFilter("hexCoders.easyApplication.budget_current_amount"));
    }

    private void readData() {

        ravenue_received_progressBar.setVisibility(View.VISIBLE);
        SharedPreferencesGetSet sharedPreferencesGetSet = new SharedPreferencesGetSet();
        ravenue_current_amount.setText(sharedPreferencesGetSet.getCurrentBudget(context));

        Calendar calendar = Calendar.getInstance();
        String month = getMonthForInt(calendar.get(Calendar.MONTH));
//        String budgetName = sharedPreferencesGetSet.getCurrentBudgetName(context);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference easyApp = FirebaseDatabase.getInstance().getReference("App Members")
                .child(auth.getCurrentUser().getUid()).child("Budget Reminder").child(DetailedBudgetActivity.selectedBudgetMonth);

        easyApp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Revenue")) {
//                    DatabaseReference easyApp2 = easyApp.child("Revenue");
                    List<RevenueModel> data = new ArrayList<>();
                    for (DataSnapshot revenueSnap : snapshot.child("Revenue").getChildren()) {
                        RevenueModel model = revenueSnap.getValue(RevenueModel.class);
                        if(model != null){
                            model.id = revenueSnap.getKey();
                            data.add(model);
                        }
                    }
                    if (!data.isEmpty()) {
                        RavenueRowAdapter ravenueRowAdapter = new RavenueRowAdapter(context, data);
                        ravenue_fragment_listView.setAdapter(ravenueRowAdapter);
                        ravenue_received_progressBar.setVisibility(View.GONE);
                    } else {
                        ravenue_received_progressBar.setVisibility(View.GONE);
                        ravenue_received_textView.setVisibility(View.VISIBLE);
                    }

                } else {
                    ravenue_received_progressBar.setVisibility(View.GONE);
                    ravenue_received_textView.setVisibility(View.VISIBLE);
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