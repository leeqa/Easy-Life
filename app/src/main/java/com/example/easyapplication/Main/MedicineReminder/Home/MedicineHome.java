package com.example.easyapplication.Main.MedicineReminder.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.easyapplication.Main.MedicineReminder.AddMedicine.AddMedicine;
import com.example.easyapplication.Main.MedicineReminder.History.MedicineHistory;
import com.example.easyapplication.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MedicineHome extends AppCompatActivity {



    @BindView(R.id.compactcalendar_view)
    CompactCalendarView mCompactCalendarView;

    @BindView(R.id.date_picker_text_view)
    TextView datePickerTextView;

    @BindView(R.id.date_picker_button)
    RelativeLayout datePickerButton;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.contentFrame)
    RelativeLayout contentFrame;

    @BindView(R.id.fab_add_task)
    LottieAnimationView fabAddTask;

    @BindView(R.id.activity_medicine_home)
    CoordinatorLayout activity_medicine_home;
//            RelativeLayout activity_medicine_home;

    @BindView(R.id.date_picker_arrow)
    ImageView arrow;

    @BindView(R.id.medicine_home_text_view)
    TextView textView;

    @BindView(R.id.medicine_home_progress_bar)
    ProgressBar progressBar;


//    private MedicinePresenter presenter;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", /*Locale.getDefault()*/Locale.ENGLISH);

    private boolean isExpanded = false;

    ArrayList<String> medicinesId;
    ArrayList<String> medicineNames;
    ArrayList<String> time;
    ArrayList<String> quantity;
    ArrayList<String> type;
    String currentDay;
    @BindView(R.id.medicine_home_list_view)
    ListView medicineHomeListView;


    @Override
    protected void onResume() {
        super.onResume();
        checkAndShowData(currentDay);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        receiverFromMedicineHomeAdapter();
        Calendar calendar = Calendar.getInstance();
        currentDay = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        mCompactCalendarView.setLocale(TimeZone.getDefault(), /*Locale.getDefault()*/Locale.ENGLISH);

        mCompactCalendarView.setShouldDrawDaysHeader(true);

        mCompactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                setSubtitle(dateFormat.format(dateClicked));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateClicked);

                int day = calendar.get(Calendar.DAY_OF_WEEK);

                if (isExpanded) {
                    ViewCompat.animate(arrow).rotation(0).start();
                } else {
                    ViewCompat.animate(arrow).rotation(180).start();
                }
                isExpanded = !isExpanded;
                appBarLayout.setExpanded(isExpanded, true);
                currentDay = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
//                presenter.reload(day);
                checkAndShowData(currentDay);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                setSubtitle(dateFormat.format(firstDayOfNewMonth));

            }
        });
        setCurrentDate(new Date());
//        MedicineFragment medicineFragment = (MedicineFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
//        if (medicineFragment == null) {
//            medicineFragment = MedicineFragment.newInstance();
//            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), medicineFragment, R.id.contentFrame);
//        }
//
//        //Create MedicinePresenter
//        presenter = new MedicinePresenter(Injection.provideMedicineRepository(MedicineActivity.this), medicineFragment);
    }

    private void receiverFromMedicineHomeAdapter() {
        getApplicationContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkAndShowData(currentDay);
            }
        }, new IntentFilter("hexCoders.easyApplication"));
    }

    private void checkAndShowData(String currentDay) {
        medicineHomeListView.setAdapter(null);
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("App Members").child(auth.getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Medicine Reminder")) {
                    readDataAndDisplay(currentDay);
                } else {
                    progressBar.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readDataAndDisplay(String currentDay) {
        progressBar.setVisibility(View.VISIBLE);
        medicinesId = new ArrayList<>();
        medicineNames = new ArrayList<>();
        time = new ArrayList<>();
        quantity = new ArrayList<>();
        type = new ArrayList<>();
        DatabaseReference easyApp;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        easyApp = FirebaseDatabase.getInstance().getReference("App Members").child(auth.getCurrentUser().getUid()).child("Medicine Reminder");
        easyApp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot medicines : snapshot.getChildren()) {
                    String id = medicines.getKey();
                    String days = String.valueOf(medicines.child("days").getValue());
                    if (days.contains(currentDay.substring(0, 3))) {
                        if (String.valueOf(medicines.child("status").getValue()).equals("Pending")) {
                            medicinesId.add(id);
                            medicineNames.add(String.valueOf(medicines.child("medicineName").getValue()));
                            time.add(String.valueOf(medicines.child("time").getValue()));
                            quantity.add(String.valueOf(medicines.child("quantity").getValue()));
                            type.add(String.valueOf(medicines.child("type").getValue()));
                            MedicineHomeAdapter medicineHomeAdapter = new MedicineHomeAdapter(getApplicationContext(), medicinesId, medicineNames, time, quantity, type);
                            medicineHomeListView.setAdapter(medicineHomeAdapter);
                            progressBar.setVisibility(View.GONE);
                            textView.setVisibility(View.GONE);
                        }
//                        else {
//                            progressBar.setVisibility(View.GONE);
//                            textView.setVisibility(View.VISIBLE);
//                        }
                    }
//                    else {
//                        progressBar.setVisibility(View.GONE);
//                        textView.setVisibility(View.VISIBLE);
//                    }
                }
                if (medicineNames.size() <= 0) {
                    progressBar.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.medicine_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_stats) {
            Intent intent = new Intent(this, MedicineHistory.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setCurrentDate(Date date) {
        setSubtitle(dateFormat.format(date));
        mCompactCalendarView.setCurrentDate(date);
    }

    public void setSubtitle(String subtitle) {
        datePickerTextView.setText(subtitle);
    }

    @OnClick(R.id.date_picker_button)
    void onDatePickerButtonClicked() {
        if (isExpanded) {
            ViewCompat.animate(arrow).rotation(0).start();
        } else {
            ViewCompat.animate(arrow).rotation(180).start();
        }

        isExpanded = !isExpanded;
        appBarLayout.setExpanded(isExpanded, true);
    }

    @OnClick(R.id.fab_add_task)
    void openMedicineAddActivity() {
        Intent intent = new Intent(MedicineHome.this, AddMedicine.class);
        startActivity(intent);
    }
}