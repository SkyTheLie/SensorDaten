package com.example.sensordaten;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.io.File;

public class SensorCollector extends Service {


    String pfadDownload = "//sdcard//Download//";
    SettingsKlass settingsKlass = SettingsKlass.getInstance();
    SharedPreferences sharedPreferences;

    SensorManager sensorManager;
    SensorEventListener sensorEventListener;


    @Override
    public void onCreate() {
        super.onCreate();
        settingsKlass = SettingsKlass.getInstance();
        sharedPreferences = getSharedPreferences("SettingsHealthApp",Context.MODE_PRIVATE);

        File pathdownload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);


        // Initialize your service here
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Start collecting gyroscope data here
        // Initialize sensor, register listener, etc.
        return START_STICKY; // Or other appropriate return value
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
