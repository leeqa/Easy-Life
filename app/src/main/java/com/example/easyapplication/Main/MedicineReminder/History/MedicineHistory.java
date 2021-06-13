package com.example.easyapplication.Main.MedicineReminder.History;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyapplication.Main.MedicineReminder.AddMedicine.MedicineModel;
import com.example.easyapplication.Main.MedicineReminder.Home.MedicineHomeAdapter;
import com.example.easyapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MedicineHistory extends AppCompatActivity {

    private String CURRENT_FILTERING_TYPE = "All";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    List<MedicineModel> medicineModels = new ArrayList<>();

    @BindView(R.id.medicine_history_list_view)
    ListView medicine_history_list_view;
    @BindView(R.id.medicine_history_txt)
    TextView textView;
    @BindView(R.id.medicine_history_progress_bar)
    ProgressBar progressBar;
    String currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_history);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Calendar calendar = Calendar.getInstance();
        currentDay = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_clear);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        checkAndShowData(CURRENT_FILTERING_TYPE);
    }

    private void checkAndShowData(String CURRENT_FILTERING_TYPE) {
//        medicine_history_list_view.setAdapter(null);
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("App Members").child(auth.getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("Medicine Reminder")) {
                    getAllDataFrmFBAndShow(CURRENT_FILTERING_TYPE);
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

    private void getAllDataFrmFBAndShow(String CURRENT_FILTERING_TYPE) {
//        medicine_history_list_view.setAdapter(null);
        Log.e( "getAllDataFrm: ", CURRENT_FILTERING_TYPE);
        DatabaseReference easyApp;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        easyApp = FirebaseDatabase.getInstance().getReference("App Members")
                .child(auth.getCurrentUser().getUid())
                .child("Medicine Reminder");
        easyApp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                medicineModels.clear();
                Log.e("onDataChange: ",snapshot+"" );

                if (CURRENT_FILTERING_TYPE.equals("All")) {
                    for (DataSnapshot medicine : snapshot.getChildren()) {
                        String days = String.valueOf(medicine.child("days").getValue());
//                        if (days.contains(currentDay.substring(0, 3)))
                        {
                            MedicineModel model = medicine.getValue(MedicineModel.class);
                            medicineModels.add(model);
                        }
                    }
                } else{
                    for (DataSnapshot medicine : snapshot.getChildren()) {
                        String days = String.valueOf(medicine.child("days").getValue());
//                        if (days.contains(currentDay.substring(0, 3)))
                        {
                            if (String.valueOf(medicine.child("status").getValue()).equals(CURRENT_FILTERING_TYPE)) {
                                MedicineModel model = medicine.getValue(MedicineModel.class);
                                medicineModels.add(model);
                            }
                        }
                    }
                }

                MedicineHistoryAdapter medicineHistoryAdapter = new MedicineHistoryAdapter(MedicineHistory.this, medicineModels);
                medicine_history_list_view.setAdapter(medicineHistoryAdapter);
                progressBar.setVisibility(View.GONE);
                if(medicineModels.isEmpty()){
                    textView.setVisibility(View.VISIBLE);
                }
                else {
                    textView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.all) {
            CURRENT_FILTERING_TYPE = "All";
            checkAndShowData(CURRENT_FILTERING_TYPE);
        } else if (item.getItemId() == R.id.taken) {
            CURRENT_FILTERING_TYPE = "Taken";
            checkAndShowData(CURRENT_FILTERING_TYPE);
        } else if (item.getItemId() == R.id.ignored) {
            CURRENT_FILTERING_TYPE = "Ignored";
            checkAndShowData(CURRENT_FILTERING_TYPE);
        }

        return super.onOptionsItemSelected(item);
    }
}