package com.example.easyapplication.Main.BudgetManager.DetailBudget.AddRavenueToBudget;

public class RevenueModel {
    public String date;
    public String amount;

    public RevenueModel(String date, String amount) {
        this.date = date;
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }


    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }
}
