package com.example.easyapplication.Main.PrayerReminder.Alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.easyapplication.Main.MedicineReminder.Alarm.AlarmActivity;
import com.example.easyapplication.R;

public class PrayerAlarmActivity extends AppCompatActivity {
    ImageView cancel;
    MediaPlayer mMediaPlayer;
    private PowerManager.WakeLock wake = null;
    LottieAnimationView animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer_alarm);

        animation=findViewById(R.id.prayerAnimation);


        animation.animate().translationY(50).setDuration(1000).setStartDelay(1000);


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
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.azan);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
        cancel=findViewById(R.id.prayer_alarm_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity();

    }

    @Override
    protected void onPause() {
        super.onPause();
        finishActivity();
    }

    public void finishActivity() {
        mMediaPlayer.release();
        PrayerAlarmActivity.this.finish();
    }
}