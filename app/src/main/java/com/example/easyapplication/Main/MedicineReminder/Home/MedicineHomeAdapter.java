package com.example.easyapplication.Main.MedicineReminder.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.easyapplication.Main.MedicineReminder.views.RobotoBoldTextView;
import com.example.easyapplication.Main.MedicineReminder.views.RobotoLightTextView;
import com.example.easyapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MedicineHomeAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> medicinesId;
    ArrayList<String> medicineNames;
    ArrayList<String> time;
    ArrayList<String> quantity;
    ArrayList<String> type;
    private LayoutInflater layoutInflater;

    public MedicineHomeAdapter(Context context, ArrayList<String> medicinesId, ArrayList<String> medicineNames, ArrayList<String> time, ArrayList<String> quantity, ArrayList<String> type) {
        this.context = context;
        this.medicinesId = medicinesId;
        this.medicineNames = medicineNames;
        this.time = time;
        this.quantity = quantity;
        this.type = type;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return medicineNames.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.medicine_row, null);
        }
        RobotoBoldTextView tv_med_time = view.findViewById(R.id.tv_med_time);
        RobotoBoldTextView tv_medicine_name = view.findViewById(R.id.tv_medicine_name);
        RobotoLightTextView tv_dose_details = view.findViewById(R.id.tv_dose_details);
        ImageView iv_alarm_delete = view.findViewById(R.id.iv_alarm_delete);
        tv_med_time.setText(time.get(i));
        tv_medicine_name.setText(medicineNames.get(i));
        tv_dose_details.setText(quantity.get(i) + " " + type.get(i));
        iv_alarm_delete.setVisibility(View.VISIBLE);
        iv_alarm_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteItemFromList(medicinesId.get(i));
            }
        });
        return view;
    }

    public void DeleteItemFromList(String id) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().getReference("App Members").child(auth.getCurrentUser().getUid()).child("Medicine Reminder")
                .child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent("hexCoders.easyApplication");
                context.sendBroadcast(intent);
                Toast.makeText(context, "Alarm Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
