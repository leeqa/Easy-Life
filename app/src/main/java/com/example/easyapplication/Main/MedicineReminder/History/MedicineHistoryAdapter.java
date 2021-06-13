package com.example.easyapplication.Main.MedicineReminder.History;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.easyapplication.Main.MedicineReminder.AddMedicine.MedicineModel;
import com.example.easyapplication.Main.MedicineReminder.views.RobotoBoldTextView;
import com.example.easyapplication.Main.MedicineReminder.views.RobotoLightTextView;
import com.example.easyapplication.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MedicineHistoryAdapter extends BaseAdapter {
    Context context;
    List<MedicineModel> data;
    private LayoutInflater layoutInflater;
    String currentDay;

    public MedicineHistoryAdapter(Context context, List<MedicineModel> data) {
        this.context = context;
        this.data =data;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Calendar calendar = Calendar.getInstance();
        currentDay = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
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
            view = layoutInflater.inflate(R.layout.medicine_row, null);
        }
        RobotoBoldTextView tv_med_time = view.findViewById(R.id.tv_med_time);
        RobotoBoldTextView tv_medicine_name = view.findViewById(R.id.tv_medicine_name);
        RobotoLightTextView tv_dose_details = view.findViewById(R.id.tv_dose_details);
        ImageView iv_alarm_delete = view.findViewById(R.id.iv_alarm_delete);
        ImageView iv_medicine_action = view.findViewById(R.id.iv_medicine_action);

        MedicineModel model = data.get(i);
        tv_med_time.setText(model.time + " " + currentDay);
        tv_medicine_name.setText(model.medicineName);
        tv_dose_details.setText(model.quantity + " " + model.type);
        String s = model.status;
        if (s.equals("Taken")) {
            iv_medicine_action.setVisibility(View.VISIBLE);
            iv_alarm_delete.setVisibility(View.GONE);
        } else{
            iv_alarm_delete.setVisibility(View.VISIBLE);
            iv_medicine_action.setVisibility(View.GONE);
        }
        return view;
    }
}
