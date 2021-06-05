package com.example.easyapplication.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesGetSet {
    public void setUserIDSP(String id, Context context) {
        SharedPreferences UserIDSP = context.getSharedPreferences("UserIDSP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = UserIDSP.edit();
        editor.putString("UserIDSP", "Purchased");
        editor.apply();
    }

    public String getUserIDSP(Context context) {
        SharedPreferences UserIDSP = context.getSharedPreferences("UserIDSP", Context.MODE_PRIVATE);
        return UserIDSP.getString("UserIDSP", " ");
    }

    public void setFajrTime(String time, Context context) {
        SharedPreferences FajrTimeSP = context.getSharedPreferences("FajrTimeSP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = FajrTimeSP.edit();
        editor.putString("FajrTimeSP", time);
        editor.apply();
    }

    public String getFajrTime(Context context) {
        SharedPreferences FajrTimeSP = context.getSharedPreferences("FajrTimeSP", Context.MODE_PRIVATE);
        return FajrTimeSP.getString("FajrTimeSP", "");
    }

    public void setZuharTime(String time, Context context) {
        SharedPreferences ZuharTimeSP = context.getSharedPreferences("ZuharTimeSP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ZuharTimeSP.edit();
        editor.putString("ZuharTimeSP", time);
        editor.apply();
    }

    public String getZuharTime(Context context) {
        SharedPreferences ZuharTimeSP = context.getSharedPreferences("ZuharTimeSP", Context.MODE_PRIVATE);
        return ZuharTimeSP.getString("ZuharTimeSP", "");
    }

    public void setAsarTime(String time, Context context) {
        SharedPreferences AsarTimeSP = context.getSharedPreferences("AsarTimeSP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = AsarTimeSP.edit();
        editor.putString("AsarTimeSP", time);
        editor.apply();
    }

    public String getAsarTime(Context context) {
        SharedPreferences AsarTimeSP = context.getSharedPreferences("AsarTimeSP", Context.MODE_PRIVATE);
        return AsarTimeSP.getString("AsarTimeSP", "");
    }

    public void setMagribTime(String time, Context context) {
        SharedPreferences MagribTimeSP = context.getSharedPreferences("MagribTimeSP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = MagribTimeSP.edit();
        editor.putString("MagribTimeSP", time);
        editor.apply();
    }

    public String getMagribTime(Context context) {
        SharedPreferences MagribTimeSP = context.getSharedPreferences("MagribTimeSP", Context.MODE_PRIVATE);
        return MagribTimeSP.getString("MagribTimeSP", "");
    }

    public void setAishaTime(String time, Context context) {
        SharedPreferences AishaTimeSP = context.getSharedPreferences("AishaTimeSP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = AishaTimeSP.edit();
        editor.putString("AishaTimeSP", time);
        editor.apply();
    }

    public String getAishaTime(Context context) {
        SharedPreferences AishaTimeSP = context.getSharedPreferences("AishaTimeSP", Context.MODE_PRIVATE);
        return AishaTimeSP.getString("AishaTimeSP", "");
    }

    public void setCurrentBudget(String budget, Context context) {
        SharedPreferences CurrentBudgetSP = context.getSharedPreferences("CurrentBudgetSP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = CurrentBudgetSP.edit();
        editor.putString("CurrentBudgetSP", budget);
        editor.apply();
    }

    public String getCurrentBudget(Context context) {
        SharedPreferences CurrentBudgetSP = context.getSharedPreferences("CurrentBudgetSP", Context.MODE_PRIVATE);
        return CurrentBudgetSP.getString("CurrentBudgetSP", "");
    }

    public void setCurrentTotalBudget(String budget, Context context) {
        SharedPreferences CurrentTotalBudgetSP = context.getSharedPreferences("CurrentTotalBudgetSP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = CurrentTotalBudgetSP.edit();
        editor.putString("CurrentTotalBudgetSP", budget);
        editor.apply();
    }

    public String getCurrentTotalBudget(Context context) {
        SharedPreferences CurrentTotalBudgetSP = context.getSharedPreferences("CurrentTotalBudgetSP", Context.MODE_PRIVATE);
        return CurrentTotalBudgetSP.getString("CurrentTotalBudgetSP", "");
    }

    public void setCurrentBudgetName(String budget, Context context) {
        SharedPreferences CurrentBudgetNameSP = context.getSharedPreferences("CurrentBudgetNameSP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = CurrentBudgetNameSP.edit();
        editor.putString("CurrentBudgetNameSP", budget);
        editor.apply();
    }

    public String getCurrentBudgetName(Context context) {
        SharedPreferences CurrentBudgetNameSP = context.getSharedPreferences("CurrentBudgetNameSP", Context.MODE_PRIVATE);
        return CurrentBudgetNameSP.getString("CurrentBudgetNameSP", "");
    }

}