package com.example.easyapplication.Main.MedicineReminder.AddMedicine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.easyapplication.Main.MedicineReminder.Alarm.AlarmReceiver;
import com.example.easyapplication.Main.MedicineReminder.Home.MedicineHome;
import com.example.easyapplication.Main.MedicineReminder.views.DayViewCheckBox;
import com.example.easyapplication.Main.MedicineReminder.views.RobotoBoldTextView;
import com.example.easyapplication.R;
import com.example.easyapplication.Utilities.SharedPreferencesGetSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.Unbinder;

import static java.security.AccessController.getContext;

public class AddMedicine extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_ADD_TASK = 1;

    public static final String SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY";

    public static final String EXTRA_TASK_ID = "task_extra_id";
    public static final String EXTRA_TASK_NAME = "task_extra_name";

//    private AddMedicinePresenter mAddMedicinePresenter;

    private ActionBar mActionBar;

    public static final String ARGUMENT_EDIT_MEDICINE_ID = "ARGUMENT_EDIT_MEDICINE_ID";

    public static final String ARGUMENT_EDIT_MEDICINE_NAME = "ARGUMENT_EDIT_MEDICINE_NAME";
    EditText editMedName;
    AppCompatCheckBox everyDay;
    DayViewCheckBox dvSunday;
    DayViewCheckBox dvMonday;
    DayViewCheckBox dvTuesday;
    DayViewCheckBox dvWednesday;
    DayViewCheckBox dvThursday;
    DayViewCheckBox dvFriday;
    DayViewCheckBox dvSaturday;
    String medicineName, time, quantity, type, status;
    String days = "";
    LinearLayout checkboxLayout;
    RobotoBoldTextView tvMedicineTime;
    EditText tvDoseQuantity;
    AppCompatSpinner spinnerDoseUnits;
    private List<String> doseUnitList;
    private boolean[] dayOfWeekList = new boolean[7];
    private int hour, minute;
    Unbinder unbinder;
    LottieAnimationView fab_edit_task_done;
    FirebaseAuth auth;
    Calendar calendar;
    String AM_PM;


//    private AddMedicineContract.Presenter mPresenter;

    private View rootView;

    private String doseUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        //Setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setTitle(getString(R.string.new_medicine));
        tvMedicineTime = findViewById(R.id.tv_medicine_time);
        spinnerDoseUnits = findViewById(R.id.spinner_dose_units);
        dvMonday = findViewById(R.id.dv_monday);
        dvTuesday = findViewById(R.id.dv_tuesday);
        dvWednesday = findViewById(R.id.dv_wednesday);
        dvThursday = findViewById(R.id.dv_thursday);
        dvFriday = findViewById(R.id.dv_friday);
        dvSaturday = findViewById(R.id.dv_saturday);
        dvSunday = findViewById(R.id.dv_sunday);
        everyDay = findViewById(R.id.every_day);
        fab_edit_task_done = findViewById(R.id.fab_edit_task_done);
        editMedName = findViewById(R.id.edit_med_name);
        tvDoseQuantity = findViewById(R.id.tv_dose_quantity);
        auth = FirebaseAuth.getInstance();
        setCurrentTime();
        setSpinnerDoseUnits();
        tvMedicineTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });
        dvMonday.setOnClickListener(this);
        dvTuesday.setOnClickListener(this);
        dvWednesday.setOnClickListener(this);
        dvThursday.setOnClickListener(this);
        dvFriday.setOnClickListener(this);
        dvSaturday.setOnClickListener(this);
        dvSunday.setOnClickListener(this);
        everyDay.setOnClickListener(this);
        fab_edit_task_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadMedicineDataOnFirebase();
            }

        });

    }

    private void showTimePicker() {
//        Calendar mCurrentTime = Calendar.getInstance();
//        hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
//        minute = mCurrentTime.get(Calendar.MINUTE);
        calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        if (hour < 12) {
            AM_PM = "AM";
        } else {
            AM_PM = "PM";
        }
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(AddMedicine.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                tvMedicineTime.setText(String.format(Locale.getDefault(), "%d:%d", selectedHour, selectedMinute));
            }
        }, hour, minute, false);//No 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void setCurrentTime() {
//        Calendar mCurrentTime = Calendar.getInstance();
//        hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
//        minute = mCurrentTime.get(Calendar.MINUTE);
//        tvMedicineTime.setText(String.format(Locale.getDefault(), "%d:%d", hour, minute));
        calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        if (hour < 12) {
            AM_PM = "AM";
        } else {
            AM_PM = "PM";
        }
        tvMedicineTime.setText(String.format(Locale.getDefault(), "%d:%d", hour, minute));
    }

    private void setSpinnerDoseUnits() {
        doseUnitList = Arrays.asList(getResources().getStringArray(R.array.medications_shape_array));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Objects.requireNonNull(getApplicationContext()), android.R.layout.simple_dropdown_item_1line, doseUnitList);
        spinnerDoseUnits.setAdapter(adapter);
    }

    @OnItemSelected(R.id.spinner_dose_units)
    void onSpinnerItemSelected(int position) {
        if (doseUnitList == null || doseUnitList.isEmpty()) {
            return;
        }

        doseUnit = doseUnitList.get(position);
    }

    private void UploadMedicineDataOnFirebase() {
        medicineName = editMedName.getText().toString();
        getDaysString();
        time = tvMedicineTime.getText().toString() + AM_PM;
        quantity = tvDoseQuantity.getText().toString();
        type = spinnerDoseUnits.getSelectedItem().toString();
        status = "Pending";
        if (medicineName == null || days == null || time == null) {
            Toast.makeText(this, "Please Enter All The Fields", Toast.LENGTH_SHORT).show();
        } else {
            MedicineModel medicineModel = new MedicineModel(medicineName, days, time, quantity, type, status);
//            SharedPreferencesGetSet sharedPreferencesGetSet = new SharedPreferencesGetSet();
//            String currentUserID = sharedPreferencesGetSet.getUserIDSP(getApplicationContext());
            DatabaseReference easyApp = FirebaseDatabase.getInstance().getReference("App Members")
                    .child(auth.getCurrentUser().getUid());
            //to create id random id
            String id = easyApp.push().getKey();
            easyApp.child("Medicine Reminder").child(id).setValue(medicineModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete()) {
                        setUpAlarm(id);
                        Toast.makeText(AddMedicine.this, "Data Uploaded", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddMedicine.this, MedicineHome.class);
                        startActivity(intent);
                        AddMedicine.this.finish();
                    } else {
                        Toast.makeText(AddMedicine.this, "Can't Upload,Try Again", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void setUpAlarm(String id) {
        if (dvSunday.isChecked()) {
            setAlarm(Calendar.SUNDAY, id);
        }
        if (dvMonday.isChecked()) {
            setAlarm(Calendar.MONDAY, id);
        }
        if (dvTuesday.isChecked()) {
            setAlarm(Calendar.TUESDAY, id);
        }
        if (dvWednesday.isChecked()) {
            setAlarm(Calendar.WEDNESDAY, id);
        }
        if (dvThursday.isChecked()) {
            setAlarm(Calendar.THURSDAY, id);
        }
        if (dvFriday.isChecked()) {
            setAlarm(Calendar.FRIDAY, id);
        }
        if (dvSaturday.isChecked()) {
            setAlarm(Calendar.SATURDAY, id);
        }
    }

    private void getDaysString() {
        for (int i = 0; i < 7; i++) {
            if (dayOfWeekList[i]) {
                if (i == 0) {
                    days = days + "Sun";
                } else if (i == 1) {
                    days = days + "Mon";
                } else if (i == 2) {
                    days = days + "Tue";
                } else if (i == 3) {
                    days = days + "Wed";
                } else if (i == 4) {
                    days = days + "Thu";
                } else if (i == 5) {
                    days = days + "Fri";
                } else if (i == 6) {
                    days = days + "Sat";
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        /** Checking which checkbox was clicked */
        switch (view.getId()) {
            case R.id.dv_sunday:
                if (checked) {
                    dayOfWeekList[0] = true;
                } else {
                    dayOfWeekList[0] = false;
                    everyDay.setChecked(false);
                }
                break;
            case R.id.dv_monday:
                if (checked) {
                    dayOfWeekList[1] = true;
                } else {
                    dayOfWeekList[1] = false;
                    everyDay.setChecked(false);
                }
                break;
            case R.id.dv_tuesday:
                if (checked) {
                    dayOfWeekList[2] = true;
                } else {
                    dayOfWeekList[2] = false;
                    everyDay.setChecked(false);
                }
                break;
            case R.id.dv_wednesday:
                if (checked) {
                    dayOfWeekList[3] = true;
                } else {
                    dayOfWeekList[3] = false;
                    everyDay.setChecked(false);
                }
                break;
            case R.id.dv_thursday:
                if (checked) {
                    dayOfWeekList[4] = true;
                } else {
                    dayOfWeekList[4] = false;
                    everyDay.setChecked(false);
                }
                break;
            case R.id.dv_friday:
                if (checked) {
                    dayOfWeekList[5] = true;
                } else {
                    dayOfWeekList[5] = false;
                    everyDay.setChecked(false);
                }
                break;
            case R.id.dv_saturday:
                if (checked) {
                    dayOfWeekList[6] = true;
                } else {
                    dayOfWeekList[6] = false;
                    everyDay.setChecked(false);
                }
                break;
            case R.id.every_day:
                LinearLayout ll = findViewById(R.id.checkbox_layout);
                for (int i = 0; i < ll.getChildCount(); i++) {
                    View v = ll.getChildAt(i);
                    ((DayViewCheckBox) v).setChecked(checked);
                    onClick((DayViewCheckBox) v);
                }
                break;
        }

    }

    public void setAlarm(int dayOfWeek, String medicineID) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        // Check we aren't setting it in the past which would trigger it to fire instantly
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 7);
        }
        // Set this to whatever you were planning to do at the given time
        Intent intent = new Intent(AddMedicine.this, AlarmReceiver.class);
        intent.putExtra("id", medicineID);
//        Calendar cal_now = Calendar.getInstance();
//        long alarmTimeAtUTC = calendar.getTimeInMillis() - cal_now.getTimeInMillis();
//        int seconds = (int) (alarmTimeAtUTC / 1000);
//        Calendar calendar1 = Calendar.getInstance();
//        calendar1.add(Calendar.SECOND, seconds);
//        for (int i = 1; i <= dayOfWeek; i++) {
        Log.e("alarammmm",""+ medicineID);
        intent.setAction(medicineID);
            PendingIntent yourIntent = PendingIntent.getBroadcast(
                    this, dayOfWeek, intent, 0);
        Log.e( "setAlarm: ",calendar+"" );
        Log.e( "setAlarm: ",calendar.getTimeInMillis()+"" );
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), yourIntent);
//        }
    }
}