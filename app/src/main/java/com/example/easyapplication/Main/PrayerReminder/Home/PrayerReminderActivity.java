package com.example.easyapplication.Main.PrayerReminder.Home;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.batoulapps.adhan.CalculationMethod;
import com.batoulapps.adhan.CalculationParameters;
import com.batoulapps.adhan.Coordinates;
import com.batoulapps.adhan.PrayerTimes;
import com.batoulapps.adhan.data.DateComponents;
import com.example.easyapplication.Main.Home.MainActivity;
import com.example.easyapplication.Main.PrayerReminder.Alarm.PrayerAlarmReceiver;
import com.example.easyapplication.Main.PrayerReminder.utility.SingleShotLocationProvider;
import com.example.easyapplication.Main.PrayerReminder.Tasebee.TasbeeActivity;
import com.example.easyapplication.R;
import com.example.easyapplication.Utilities.SharedPreferencesGetSet;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class PrayerReminderActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION_PERMISSION = 12;
    float latitude, longitude;
    ProgressBar progressBar;
    FusedLocationProviderClient fusedLocationClient;
    String fajrTime, zuharTime, asarTime, magribTime, aishaTime;
    RelativeLayout fajr_layout, zuhar_layout, asar_layout, magrib_layout, aisha_layout;
    TextView fajr_time, zuhar_time, asar_time, magrib_time, aisha_time;
    ImageView tasbee,BackButton;
    int fajrID = 1;
    int zuharID = 2;
    int asarID = 3;
    int magribID = 4;
    int aishaID = 5;
    boolean firstTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer_reminder);
        firstTime = true;
        progressBar = findViewById(R.id.prayer_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        BackButton = findViewById(R.id.backbutton);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrayerReminderActivity.this, MainActivity.class);
                finish();
            }
        });
        tasbee = findViewById(R.id.counter_image);
        tasbee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrayerReminderActivity.this, TasbeeActivity.class);
                startActivity(intent);
            }
        });
        fajr_layout = findViewById(R.id.fajr_layout);
        zuhar_layout = findViewById(R.id.zuhar_layout);
        asar_layout = findViewById(R.id.asar_layout);
        magrib_layout = findViewById(R.id.magrib_layout);
        aisha_layout = findViewById(R.id.aisha_layout);
        fajr_time = findViewById(R.id.fajr_time);
        zuhar_time = findViewById(R.id.zuhar_time);
        asar_time = findViewById(R.id.asar_time);
        magrib_time = findViewById(R.id.magrib_time);
        aisha_time = findViewById(R.id.aisha_time);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        enableMyLocation();
        statusCheck();
        foo(getApplicationContext());
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void enableMyLocation() {

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == (PackageManager.PERMISSION_DENIED)) {
            String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION};
            requestPermissions(permission, REQUEST_LOCATION_PERMISSION);
        } else {
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                    break;
                }
        }
    }


    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void foo(Context context) {
        SingleShotLocationProvider.requestSingleUpdate(context,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override
                    public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        progressBar.setVisibility(View.GONE);
                        getPrayersTimes(location);
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        firstTime = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!firstTime)
            foo(getApplicationContext());
    }

    public void getPrayersTimes(SingleShotLocationProvider.GPSCoordinates location) {

        SharedPreferencesGetSet sharedPreferencesGetSet = new SharedPreferencesGetSet();
        latitude = location.latitude;
        longitude = location.longitude;
        final Coordinates coordinates = new Coordinates(latitude, longitude);
        final DateComponents dateComponents = DateComponents.from(new Date());
        final CalculationParameters parameters =
                CalculationMethod.MUSLIM_WORLD_LEAGUE.getParameters();
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Karachi"));
        PrayerTimes prayerTimes = new PrayerTimes(coordinates, dateComponents, parameters);
        setUpAlarms(prayerTimes);
        fajrTime = formatter.format(prayerTimes.fajr);
        fajr_layout.setVisibility(View.VISIBLE);
        fajr_time.setText(fajrTime);
        sharedPreferencesGetSet.setFajrTime(fajrTime, getApplicationContext());
        zuharTime = formatter.format(prayerTimes.dhuhr);
        zuhar_layout.setVisibility(View.VISIBLE);
        zuhar_time.setText(zuharTime);
        sharedPreferencesGetSet.setZuharTime(zuharTime, getApplicationContext());
        asarTime = formatter.format(prayerTimes.asr);
        asar_layout.setVisibility(View.VISIBLE);
        asar_time.setText(asarTime);
        sharedPreferencesGetSet.setAsarTime(asarTime, getApplicationContext());
        magribTime = formatter.format(prayerTimes.maghrib);
        magrib_layout.setVisibility(View.VISIBLE);
        magrib_time.setText(magribTime);
        sharedPreferencesGetSet.setMagribTime(magribTime, getApplicationContext());
        aishaTime = formatter.format(prayerTimes.isha);
        aisha_layout.setVisibility(View.VISIBLE);
        aisha_time.setText(aishaTime);
        sharedPreferencesGetSet.setAishaTime(aishaTime, getApplicationContext());


    }

    private void setUpAlarms(PrayerTimes prayerTimes) {
        SharedPreferencesGetSet sharedPreferencesGetSet = new SharedPreferencesGetSet();
        String prevTime = sharedPreferencesGetSet.getFajrTime(getApplicationContext());
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Karachi"));
        String newTime = formatter.format(prayerTimes.fajr);
        Log.e("secondsssss",""+newTime);

        if (prevTime == null || !prevTime.equals(newTime)) {
        setAlarmForFajr(prayerTimes.fajr);
        setAlarmForZuhar(prayerTimes.dhuhr);
        setAlarmForAsar(prayerTimes.asr);
        setAlarmForMagrib(prayerTimes.maghrib);
        setAlarmForAisha(prayerTimes.isha);
        }
    }

    public void setAlarmForFajr(Date date) {
        long alarmMilli;
        Calendar alarmCalender = Calendar.getInstance();
        alarmCalender.setTime(date);
        // Check we aren't setting it in the past which would trigger it to fire instantly
        if (alarmCalender.getTimeInMillis() < System.currentTimeMillis()) {
            alarmCalender.add(Calendar.DAY_OF_YEAR, 1);
        }
        Calendar cal_now = Calendar.getInstance();
        alarmMilli = alarmCalender.getTimeInMillis() - cal_now.getTimeInMillis();
        int seconds = (int) (alarmMilli / 1000);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, seconds);
        Intent intent = new Intent(PrayerReminderActivity.this, PrayerAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, fajrID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void setAlarmForZuhar(Date date) {
        Calendar alarmCalender = Calendar.getInstance();
        alarmCalender.setTime(date);
        // Check we aren't setting it in the past which would trigger it to fire instantly
        if (alarmCalender.getTimeInMillis() < System.currentTimeMillis()) {
            alarmCalender.add(Calendar.DAY_OF_YEAR, 1);
        }
        Calendar cal_now = Calendar.getInstance();
        long alarmMilli = alarmCalender.getTimeInMillis() - cal_now.getTimeInMillis();
        int seconds = (int) (alarmMilli / 1000);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, seconds);
        Intent intent = new Intent(PrayerReminderActivity.this, PrayerAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, zuharID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void setAlarmForAsar(Date date) {
        Calendar alarmCalender = Calendar.getInstance();
        alarmCalender.setTime(date);
        // Check we aren't setting it in the past which would trigger it to fire instantly
        if (alarmCalender.getTimeInMillis() < System.currentTimeMillis()) {
            alarmCalender.add(Calendar.DAY_OF_YEAR, 1);
        }
        Calendar cal_now = Calendar.getInstance();
        long alarmMilli = alarmCalender.getTimeInMillis() - cal_now.getTimeInMillis();
        int seconds = (int) (alarmMilli / 1000);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, seconds);
        Intent intent = new Intent(PrayerReminderActivity.this, PrayerAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, asarID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void setAlarmForMagrib(Date date) {
        Calendar alarmCalender = Calendar.getInstance();
        alarmCalender.setTime(date);
        // Check we aren't setting it in the past which would trigger it to fire instantly
        if (alarmCalender.getTimeInMillis() < System.currentTimeMillis()) {
            alarmCalender.add(Calendar.DAY_OF_YEAR, 1);
        }
        Calendar cal_now = Calendar.getInstance();
        long alarmMilli = alarmCalender.getTimeInMillis() - cal_now.getTimeInMillis();
        int seconds = (int) (alarmMilli / 1000);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, seconds);
        Log.e("secondsssss",""+seconds);
        Intent intent = new Intent(PrayerReminderActivity.this, PrayerAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, magribID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void setAlarmForAisha(Date date) {
        Calendar alarmCalender = Calendar.getInstance();
        alarmCalender.setTime(date);
        // Check we aren't setting it in the past which would trigger it to fire instantly
        if (alarmCalender.getTimeInMillis() < System.currentTimeMillis()) {
            alarmCalender.add(Calendar.DAY_OF_YEAR, 1);
        }
        Calendar cal_now = Calendar.getInstance();
        long alarmMilli = alarmCalender.getTimeInMillis() - cal_now.getTimeInMillis();
        int seconds = (int) (alarmMilli / 1000);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, seconds);
        Intent intent = new Intent(PrayerReminderActivity.this, PrayerAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, aishaID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}