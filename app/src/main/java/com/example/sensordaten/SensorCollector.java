package com.example.sensordaten;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class SensorCollector extends Service {
    private SensorCollector sensorCollector;
    File pathdownload;
    SettingsKlass settingsKlass = SettingsKlass.getInstance();
    SharedPreferences sharedPreferences;

    SensorManager sensorManager;
    SensorEventListener sensorEventListener ;

    //LinkedList<SensorNode> gList;
    //LinkedList<SensorNode> aList;

    LinkedList<SensorNode> aList = new LinkedList<SensorNode>();
    LinkedList<SensorNode> gList = new LinkedList<SensorNode>();
    NotificationManager notificationManager;
    NotificationChannel channel;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private final IBinder binder = new LocalBinder();
    public class LocalBinder extends Binder {
        SensorCollector getService() {
            return SensorCollector.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                switch (event.sensor.getType()){
                    case Sensor.TYPE_ACCELEROMETER:{
                        //Log.d("debug",event.values[0] + "\n");
                        aList.add(new SensorNode(event.values[0], event.values[1], event.values[2], System.currentTimeMillis(), sharedPreferences.getString("KlasseSetting", "0")));
                        //Log.d("debug",aList.getLast().getX() + "   " + aList.getLast().getY() + "   " + aList.getLast().getZ() + "  \n");
                    }break;
                    case Sensor.TYPE_GYROSCOPE:{
                        gList.add(new SensorNode(event.values[0], event.values[1], event.values[2], System.currentTimeMillis(), sharedPreferences.getString("KlasseSetting", "0")));
                        //Log.d("debug",gList.getLast().getX() + "   " + gList.getLast().getY() + "   " + gList.getLast().getZ() + "  \n");
                    }break;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "My Channel Name";
            String channelDescription = "My Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            channel = new NotificationChannel("1", channelName, importance);
            channel.setDescription(channelDescription);

            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        settingsKlass = SettingsKlass.getInstance();
        sharedPreferences = getSharedPreferences("SettingsHealthApp",Context.MODE_PRIVATE);

        pathdownload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager == null) {

        }else {
            //Log.d("debug","RegisterListener------------------"+ "\n");
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), Integer.parseInt(sharedPreferences.getString("GyroSetting", "3")));
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), Integer.parseInt(sharedPreferences.getString("AccSetting", "3")));
        }

        // Initialize your service here
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManagerStop();
        stopSelf();
        sensorManager.unregisterListener(sensorEventListener);
        // Unregister sensor listener and clean up here
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Start collecting gyroscope data here
        // Initialize sensor, register listener, etc.
        Log.d("debug","onStartCommand------------------"+ "\n");
        Notification notification = new NotificationCompat.Builder(this, "1")
                .setContentTitle("Gyroscope Data Collection")
                .setContentText("Collecting gyroscope data...")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();

        // Starten Sie den Dienst im Vordergrund mit der Benachrichtigung
        startForeground(1, notification);

        //sensorManagerStart();
        Log.d("debug","sensorManagerStart------------------"+ "\n");


        return START_STICKY; // Or other appropriate return value
    }



    public void safeIntoFiles(){
        String trenner = ",";
        //String klasstype = sharedPreferences.getString("KlasseSetting", "0");
        try {
            boolean fileIsnew = false;
            //pfadDownload = (requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)).getPath();
            //Log.d("debug",pfadDownload);


            //File file = new File(pfadDownload +"CA_Daten_Acc_CSV" +".csv");
            File file = new File(pathdownload, "CA_Daten_Acc_CSV" +".txt");

            if (!file.exists()) {
                file.createNewFile();
                fileIsnew = true;
            }

            FileWriter fw = new FileWriter(file.getPath(), true);
            //FileWriter fw = new FileWriter("/storage/emulated/Download" + file);
            BufferedWriter bw = new BufferedWriter(fw);
            if(fileIsnew){
                // bw.write("x;y;z;yyyy-MM-dd:HH-mm-ss-SSS\n");
                //bw.write("x;y;z;yyyy-MM-dd:HH:mm:ss\n");
                bw.write("id" + trenner + "x" + trenner + "y" + trenner + "z" + trenner + "mag" + trenner + "klasstype" +"\n");
            }
            for (SensorNode n: aList) {
                bw.write(n.getDate() + trenner + n.getData(trenner) + trenner + n.getMAG() + trenner + n.getKlassenType() +"\n");
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            boolean fileIsnew = false;
            //File file = new File(pfadDownload+"CA_Daten_Gyro_CSV" +".csv");
            File file = new File(pathdownload, "CA_Daten_Gyro_CSV" +".txt");

            if (!file.exists()) {
                file.createNewFile();
                fileIsnew = true;
            }

            FileWriter fw = new FileWriter(file.getPath(), true);
            //FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);

            BufferedWriter bw = new BufferedWriter(fw);

            if(fileIsnew){
                // bw.write("x;y;z;yyyy-MM-dd:HH-mm-ss-SSS\n");
                //bw.write("x;y;z;yyyy-MM-dd:HH:mm:ss\n");
                bw.write("id" + trenner + "x" + trenner + "y" + trenner + "z" + trenner + "mag" + trenner + "klasstype" +"\n");
            }
            for (SensorNode n: gList) {
                bw.write(n.getDate() + trenner + n.getData(trenner) + trenner + n.getMAG() + trenner + n.getKlassenType() +"\n");
            }
            bw.close();
            //gList.clear();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            boolean fileIsnew = false;
            //pfadDownload = (requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)).getPath();
            //Log.d("debug",pfadDownload);


            //File file = new File(pfadDownload +"CA_Daten_Acc_CSV" +".csv");
            File file = new File(pathdownload, "CA_Daten_Acc+gyro_CSV" +".txt");

            if (!file.exists()) {
                file.createNewFile();
                fileIsnew = true;
            }

            FileWriter fw = new FileWriter(file.getPath(), true);
            //FileWriter fw = new FileWriter("/storage/emulated/Download" + file);
            BufferedWriter bw = new BufferedWriter(fw);
            if(fileIsnew){
                bw.write("id" + trenner + "xa" + trenner + "ya" + trenner + "za" + trenner + "aMag" + trenner + "xg" + trenner + "yg" + trenner + "zg" + trenner + "gMag" + trenner + "klasstype" + "\n");
            }
            for(int i = 0; i < aList.size() && i < gList.size(); i++){
                //Log.d("debug",i + "   " + aList.size() + "   " + gList.size());
                bw.write(aList.get(i).getDate() + trenner + aList.get(i).getData(trenner) + trenner +aList.get(i).getMAG() + trenner + gList.get(i).getData(trenner) + trenner + gList.get(i).getMAG() + trenner + gList.get(i).getKlassenType() + "\n");
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            boolean fileIsnew = false;
            File file = new File(pathdownload, "gMag_List" +".txt");

            if (!file.exists()) {
                file.createNewFile();
                fileIsnew = true;
            }

            FileWriter fw = new FileWriter(file.getPath(), true);
            //FileWriter fw = new FileWriter("/storage/emulated/Download" + file);
            BufferedWriter bw = new BufferedWriter(fw);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            if(fileIsnew){
                editor.putString("countId", 0 + "");
                editor.apply();
                bw.write("id" + trenner + "date" + trenner + "aMag" + trenner + "klasenType\n");
            }

            sharedPreferences = getSharedPreferences("SettingsHealthApp", Context.MODE_PRIVATE);
            long datehelper = 0;

            int count = 0;
            int countId = Integer.parseInt(sharedPreferences.getString("countId", "0"));

            float helper = 0;
            for(SensorNode s: aList){
                if (count == 5){
                    helper = helper / 5;
                    datehelper = datehelper / 5;
                    bw.write(countId + trenner + datehelper + trenner + helper + trenner + sharedPreferences.getString("KlasseSetting", "0") + "\n");
                    count = 0;
                    datehelper = 0;
                    countId++;
                }
                helper += s.getMAG();
                count++;
                datehelper += s.getDate();
            }

            editor.putString("countId", countId + "");
            editor.apply();
            bw.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void sensorManagerStart(){
        sensorManager.registerListener(sensorEventListener,sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), settingsKlass.getSpeedGyro());
        sensorManager.registerListener(sensorEventListener,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), settingsKlass.getSpeedAcc());

        Log.d("debug","sensorManagerStart------------------"+ "\n");

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Log.d("debug","forIf------------------"+ "\n");
        if (gyroscopeSensor != null) {
            sensorManager.registerListener(sensorEventListener, gyroscopeSensor, settingsKlass.getSpeedGyro());
        } else {
            Log.d("debug", "Gyroscope sensor is not available on this device.");
        }

        if (accelerometerSensor != null) {
            sensorManager.registerListener(sensorEventListener, accelerometerSensor, settingsKlass.getSpeedAcc());
        } else {
            Log.d("debug", "Accelerometer sensor is not available on this device.");
        }

        //sensorEventListener

    }

    public void sensorManagerStop(){
        sensorManager.unregisterListener(sensorEventListener);
    }

    public LinkedList<SensorNode> getgList(){
        //Log.d("debug", this.gList.size() + "GetMethode...................");
        return this.gList;
    }
    public LinkedList<SensorNode> getaList(){
        return this.aList;
    }
}
