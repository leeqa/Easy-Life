package com.example.easyapplication.Main.MedicineReminder.AddMedicine;

import java.lang.reflect.Array;

public class MedicineModel {
    public String medicineName, days, time, quantity, type, status;

    public MedicineModel() {
    }

    public MedicineModel(String medicineName, String days, String time, String quantity, String type, String status) {
        this.medicineName = medicineName;
        this.days = days;
        this.time = time;
        this.quantity = quantity;
        this.type = type;
        this.status = status;
    }
//    public void setMedicineName(){
//
//    }
}
