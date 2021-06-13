package com.example.easyapplication.Main.BudgetManager.DetailBudget.RavenueFragment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.easyapplication.Main.BudgetManager.DetailBudget.AddRavenueToBudget.RevenueModel;
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
import java.util.List;

public class RavenueRowAdapter extends BaseAdapter {
    TextView ravenue_date, ravenue_name, ravenue_amount, txtDetail;
    ImageButton ravenue_delete;
    ProgressBar progressBar;
    Context context;
    List<RevenueModel> data;
    LayoutInflater layoutInflater;

    public RavenueRowAdapter(Context context, List<RevenueModel> data) {
        this.context = context;
        this.data = data;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.ravenue_fragment_row, null);
        }
        ravenue_date = view.findViewById(R.id.ravenue_date);
        ravenue_name = view.findViewById(R.id.ravenue_name);
        ravenue_amount = view.findViewById(R.id.ravenue_amount);
        txtDetail= view.findViewById(R.id.txtDetail);
        progressBar=view.findViewById(R.id.ravenue_progressbar);

        RevenueModel model = data.get(i);
        ravenue_date.setText(model.getDate());
        ravenue_amount.setText("Rs"+model.getAmount());
        txtDetail.setText(model.getNote());
        String amount = model.getAmount();
        ravenue_delete = view.findViewById(R.id.ravenue_delete);
        ravenue_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ravenue_delete.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                String id = data.get(i).id;
                deleteFromList( id, amount);
            }
        });
        return view;
    }
    private void deleteFromList(String id,String amount){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference easyApp = FirebaseDatabase.getInstance().getReference("App Members")
                .child(auth.getCurrentUser().getUid());
        easyApp.child("Budget Reminder").child(DetailedBudgetActivity.selectedBudgetMonth)
                .child("Revenue").child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                setRemainingBudget(amount);
            }
        });
    }

    private void setRemainingBudget(String amount){
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
                Intent intent = new Intent("hexCoders.easyApplication.ravenue");
                context.sendBroadcast(intent);
                Intent intent1 = new Intent("hexCoders.easyApplication.budget_current_amount");
                context.sendBroadcast(intent1);
                ravenue_delete.setEnabled(true);
                sharedPreferencesGetSet.setCurrentBudget(newAmount, context);
                progressBar.setVisibility(View.GONE);
            }
        });

    }
    private String getRemainingAmount(String currentBudgetAmount, String amount) {
        int current = Integer.parseInt(currentBudgetAmount);
        int expense = Integer.parseInt(amount);
        int remaining = current - expense;
        return String.valueOf(remaining);

    }
}
