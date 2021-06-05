package com.example.easyapplication.Main.MedicineReminder.History;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.easyapplication.Main.MedicineReminder.views.RobotoBoldTextView;
import com.example.easyapplication.Main.MedicineReminder.views.RobotoLightTextView;
import com.example.easyapplication.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MedicineHistoryAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> medicineNames;
    ArrayList<String> time;
    ArrayList<String> quantity;
    ArrayList<String> type;
    ArrayList<String> status;
    private LayoutInflater layoutInflater;
    String currentDay;

    public MedicineHistoryAdapter(Context context, ArrayList<String> medicineNames, ArrayList<String> time, ArrayList<String> quantity, ArrayList<String> type, ArrayList<String> status) {
        this.context = context;
        this.medicineNames = medicineNames;
        this.time = time;
        this.quantity = quantity;
        this.type = type;
        this.status = status;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Calendar calendar = Calendar.getInstance();
        currentDay = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.medicine_row, null);
            RobotoBoldTextView tv_med_time = view.findViewById(R.id.tv_med_time);
            RobotoBoldTextView tv_medicine_name = view.findViewById(R.id.tv_medicine_name);
            RobotoLightTextView tv_dose_details = view.findViewById(R.id.tv_dose_details);
            ImageView iv_alarm_delete = view.findViewById(R.id.iv_alarm_delete);
            ImageView iv_medicine_action = view.findViewById(R.id.iv_medicine_action);
            tv_med_time.setText(time.get(i) + " " + currentDay);
            tv_medicine_name.setText(medicineNames.get(i));
            tv_dose_details.setText(quantity.get(i) + " " + type.get(i));
            String s = status.get(i);
            if (s.equals("Taken")) {
                iv_medicine_action.setVisibility(View.VISIBLE);
            } else if (s.equals("Ignored")) {
                iv_alarm_delete.setVisibility(View.VISIBLE);
            }
        }
        return view;
    }
}
