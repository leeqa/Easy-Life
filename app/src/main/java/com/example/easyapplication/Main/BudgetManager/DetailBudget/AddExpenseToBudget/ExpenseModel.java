package com.example.easyapplication.Main.BudgetManager.DetailBudget.AddExpenseToBudget;

public class ExpenseModel {
    public String date;
    public String category;
    public String amount;

    public ExpenseModel(String date, String category, String amount) {
        this.date = date;
        this.category = category;
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }
}
