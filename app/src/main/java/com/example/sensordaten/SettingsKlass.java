package com.example.sensordaten;

import android.app.Application;

public class SettingsKlass extends Application {
    private static SettingsKlass sInstance  = null;

    private boolean switchRG = false;
    private int speedAcc = 3;
    private int speedGyro = 3;
/*
    private SettingsKlass(){
        switchRG = false;
    }
*/
    @Override
    public void onCreate() {
        super.onCreate();
        // Setup singleton instance
        sInstance  = this;
    }

    /*
    public SettingsKlass getInstance(){
        if(this.isntance == null){
            isntance = new SettingsKlass();
        }
        return isntance;
    }
*/
    public static SettingsKlass getInstance(){
        if(sInstance == null){
            sInstance = new SettingsKlass();
        }
        return sInstance;
    }

    public void switchRG(){
        this.switchRG = !this.switchRG;
    }

    public boolean getSwitchRGState(){
        return this.switchRG;
    }

    public void setSpeedGyro(int s) {
        this.speedGyro = s;
    }
    public void setSpeedAcc(int s) {
        this.speedAcc = s;
    }
    public int getSpeedGyro() {
        return this.speedGyro;
    }
    public int getSpeedAcc() {
        return this.speedAcc;
    }
}
