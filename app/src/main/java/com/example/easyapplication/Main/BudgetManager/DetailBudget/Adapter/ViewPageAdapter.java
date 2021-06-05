package com.example.easyapplication.Main.BudgetManager.DetailBudget.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.easyapplication.Main.BudgetManager.DetailBudget.ExpenseFragment.ExpenseFragment;
import com.example.easyapplication.Main.BudgetManager.DetailBudget.RavenueFragment.RavenueFragment;

public class ViewPageAdapter extends FragmentPagerAdapter {
    ExpenseFragment expenseFragment;
    RavenueFragment ravenueFragment;

    public ViewPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
        inIt();
    }
    private void inIt() {
        expenseFragment = new ExpenseFragment();
        ravenueFragment = new RavenueFragment();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return expenseFragment;
            case 1:
                return ravenueFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
            default:
                return "Expense";
            case 1:
                return "Revenue";

        }
    }
}
