package com.example.easyapplication.Main.MedicineReminder.Alarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.easyapplication.Main.MedicineReminder.Home.MedicineHomeAdapter;
import com.example.easyapplication.Main.MedicineReminder.views.RobotoBoldTextView;
import com.example.easyapplication.Main.MedicineReminder.views.RobotoLightTextView;
import com.example.easyapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlarmActivity extends AppCompatActivity {
    RobotoBoldTextView tv_med_time, tv_medicine_name;
    RobotoLightTextView tv_dose_details;
    ImageView iv_medicine_taken, iv_medicine_ignored;
    String id;
    String time, name, details;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    MediaPlayer mMediaPlayer;
    private PowerManager.WakeLock wake = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        PowerManager powerManager = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        wake = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "App:wakeuptag");
        wake.acquire(1*60*1000);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON|
                    WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        }
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.cuco_sound);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        id = getIntent().getStringExtra("id");
        tv_med_time = findViewById(R.id.tv_med_time);
        tv_medicine_name = findViewById(R.id.tv_medicine_name);
        tv_dose_details = findViewById(R.id.tv_dose_details);
        iv_medicine_taken = findViewById(R.id.iv_take_med);
        iv_medicine_ignored = findViewById(R.id.iv_ignore_med);
        getDataAndShowIt();
        iv_medicine_taken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMedicineStatusToTaken();
            }
        });
        iv_medicine_ignored.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMedicineStatusToIgnored();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseDatabase.getInstance().getReference("App Members").child(auth.getCurrentUser().getUid()).child("Medicine Reminder").child(id)
                    .child("status").setValue("Ignored").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(AlarmActivity.this, "Ignored", Toast.LENGTH_SHORT).show();
                    finishActivity();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity();
    }

    private void getDataAndShowIt() {
        DatabaseReference easyApp;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        easyApp = FirebaseDatabase.getInstance().getReference("App Members").child(auth.getCurrentUser().getUid()).child("Medicine Reminder").child(id);
        easyApp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot medicines : snapshot.getChildren()) {
                time = snapshot.child("time").getValue().toString();
                name = snapshot.child("medicineName").getValue().toString();
                details = snapshot.child("quantity").getValue().toString() + " " + snapshot.child("quantity").getValue().toString();
                tv_med_time.setText(time);
                tv_medicine_name.setText(name);
                tv_dose_details.setText(details);
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateMedicineStatusToTaken() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().getReference("App Members").child(auth.getCurrentUser().getUid()).child("Medicine Reminder").child(id)
                .child("status").setValue("Taken").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AlarmActivity.this, "Taken", Toast.LENGTH_SHORT).show();
                finishActivity();
            }
        });
    }

    private void updateMedicineStatusToIgnored() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().getReference("App Members").child(auth.getCurrentUser().getUid()).child("Medicine Reminder").child(id)
                .child("status").setValue("Ignored").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AlarmActivity.this, "Ignored", Toast.LENGTH_SHORT).show();
                finishActivity();
            }
        });
    }

    public void finishActivity() {
        mMediaPlayer.release();
        AlarmActivity.this.finish();
    }
}