package com.example.sensordaten;

import static android.content.Context.SENSOR_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;


public class GraphFragment extends Fragment {
    String pfadDownload = "//sdcard//Download//";
    SettingsKlass settingsKlass = SettingsKlass.getInstance();
    SharedPreferences sharedPreferences;
    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss-SSS", Locale.getDefault());
    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss", Locale.getDefault());
    //String currentDateandTime;
    SensorManager sensorManager;
    SensorEventListener sensorEventListener;
    SensorCollector sensorCollectorc;
    Switch sswitch;
    TextView tvXG;
    TextView tvYG;
    TextView tvZG;
    TextView tvXGName;
    TextView tvYGName;
    TextView tvZGName;
    TextView tvXA;
    TextView tvXAName;
    TextView tvYAName;
    TextView tvZAName;
    TextView tvYA;
    TextView tvZA;
    TextView tvMinA;
    TextView tvMaxA;
    TextView tvDsA;
    TextView tvMinG;
    TextView tvMaxG;
    TextView tvDsG;
    Button btSave;

    LinkedList<SensorNode> gList;
    int countGList = 0;
    LinkedList<SensorNode> aList;
    int countAList = 0;

    LinkedList<Float> gListAvg;
    int countGListAvg = 0;
    float[] avgHelper = new float[5];

    GraphView graphA;
    GraphView graphG;

    Handler handler;

    LineGraphSeries<DataPoint> xyWertA;
    LineGraphSeries<DataPoint> xyWertAX;
    LineGraphSeries<DataPoint> xyWertAY;
    LineGraphSeries<DataPoint> xyWertAZ;
    LineGraphSeries<DataPoint> xyWertG;
    LineGraphSeries<DataPoint> xyWertGX;
    LineGraphSeries<DataPoint> xyWertGY;
    LineGraphSeries<DataPoint> xyWertGZ;
    SensorCollector myForegroundService;
    Intent serviceIntent;
    private SensorCollector.LocalBinder binder;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (SensorCollector.LocalBinder) service;
            myForegroundService = binder.getService();
            gList = myForegroundService.getgList();
            aList = myForegroundService.getaList();
            //Log.d("debug", myForegroundService.getgList().size() + "awdsad sadas asda...................");

            // Hier kannst du mit der LinkedList arbeiten
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binder = null;
        }
    };

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);


        settingsKlass = SettingsKlass.getInstance();
        sharedPreferences = getContext().getSharedPreferences("SettingsHealthApp", Context.MODE_PRIVATE);

        //sensorCollectorc = new SensorCollector();

        //view = inflater.inflate(R.layout.fragment_blank, container,  false);

        //tvXG = findViewById(R.id.textView5);
        tvXG = view.findViewById(R.id.textView5);
        tvYG = view.findViewById(R.id.textView9);
        tvZG = view.findViewById(R.id.textView13);
        tvXGName = view.findViewById(R.id.textView4);
        tvYGName = view.findViewById(R.id.textView8);
        tvZGName = view.findViewById(R.id.textView12);
        tvXG.setTextColor(Color.BLUE);
        tvXGName.setTextColor(Color.BLUE);
        tvYG.setTextColor(Color.GREEN);
        tvYGName.setTextColor(Color.GREEN);
        tvZG.setTextColor(Color.RED);
        tvZGName.setTextColor(Color.RED);

        tvXA = view.findViewById(R.id.textView3);
        tvYA = view.findViewById(R.id.textView7);
        tvZA = view.findViewById(R.id.textView11);
        tvXAName = view.findViewById(R.id.textView2);
        tvYAName = view.findViewById(R.id.textView6);
        tvZAName = view.findViewById(R.id.textView10);
        tvXA.setTextColor(Color.BLUE);
        tvXAName.setTextColor(Color.BLUE);
        tvYA.setTextColor(Color.GREEN);
        tvYAName.setTextColor(Color.GREEN);
        tvZA.setTextColor(Color.RED);
        tvZAName.setTextColor(Color.RED);

        tvMinA = view.findViewById(R.id.textView24);
        tvMaxA = view.findViewById(R.id.textView19);
        tvDsA = view.findViewById(R.id.textView27);

        tvMinG = view.findViewById(R.id.textView26);
        tvMaxG = view.findViewById(R.id.textView21);
        tvDsG = view.findViewById(R.id.textView23);

        btSave = view.findViewById(R.id.btSave);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myForegroundService.safeIntoFiles();
            }
        });

        sswitch = view.findViewById(R.id.swSensor);
        sswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    serviceIntent = new Intent(getActivity(), SensorCollector.class);
                    getActivity().startService(serviceIntent);
                    serviceIntent = new Intent(getActivity(), SensorCollector.class);
                    getActivity().bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);


                    initGraph(view);
                    handler = new Handler(); //Das Machen
                    final int delayMillis = 200;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            updateGraphViewWithData();
                            handler.postDelayed(this, delayMillis);
                        }
                    }, delayMillis);

                }else{
                    getActivity().stopService(serviceIntent);
                    handler.removeCallbacksAndMessages(null);
                }
            }
        });
    }



    private void updateGraphViewWithData() {
        if(gList != null){
            if(countGList < gList.size()){
                for (;countGList < gList.size(); countGList++){
                    if(gList.size() > 1000){
                        xyWertG.appendData(new DataPoint(countGList, gList.get(countGList).getMAG()), true, 1000);
                        xyWertGX.appendData(new DataPoint(countGList, gList.get(countGList).getX()), true, 1000);
                        xyWertGY.appendData(new DataPoint(countGList, gList.get(countGList).getY()), true, 1000);
                        xyWertGZ.appendData(new DataPoint(countGList, gList.get(countGList).getZ()), true, 1000);
                    }else{
                        xyWertG.appendData(new DataPoint(countGList, gList.get(countGList).getMAG()), false, 1000);
                        xyWertGZ.appendData(new DataPoint(countGList, gList.get(countGList).getX()), false, 1000);
                        xyWertGX.appendData(new DataPoint(countGList, gList.get(countGList).getY()), false, 1000);
                        xyWertGY.appendData(new DataPoint(countGList, gList.get(countGList).getZ()), false, 1000);
                    }
                }
            }
        }else{
            Log.d("debug","Null-------------");
        }

        if(aList != null){
            if(countAList < aList.size()){
                for (;countAList < aList.size(); countAList++){
                    if(aList.size() > 1000){
                        xyWertA.appendData(new DataPoint(countAList, aList.get(countAList).getMAG()), true, 1000);
                        xyWertAX.appendData(new DataPoint(countAList, aList.get(countAList).getX()), true, 1000);
                        xyWertAY.appendData(new DataPoint(countAList, aList.get(countAList).getY()), true, 1000);
                        xyWertAZ.appendData(new DataPoint(countAList, aList.get(countAList).getZ()), true, 1000);
                    }else{
                        xyWertA.appendData(new DataPoint(countAList, aList.get(countAList).getMAG()), false, 1000);
                        xyWertAZ.appendData(new DataPoint(countAList, aList.get(countAList).getX()), false, 1000);
                        xyWertAX.appendData(new DataPoint(countAList, aList.get(countAList).getY()), false, 1000);
                        xyWertAY.appendData(new DataPoint(countAList, aList.get(countAList).getZ()), false, 1000);
                    }
                }
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.graphfragment, container, false);
    }

    private void initGraph(View view) {
        //aList = sensorCollectorc.getaList();
        //gList = sensorCollectorc.getgList();

        graphA = view.findViewById(R.id.graphViewA);
        //graphA.getViewport().setYAxisBoundsManual(true);
        graphA.getViewport().setXAxisBoundsManual(true);
        graphA.getViewport().setMaxY(0);
        //graphA.getViewport().setMinY(-300);
        graphA.getViewport().setMinY(0);
        graphA.getViewport().setMaxY(0);
        graphA.getViewport().setMaxX(1000);

        xyWertA = new LineGraphSeries<DataPoint>();
        xyWertAX = new LineGraphSeries<DataPoint>();
        xyWertAY = new LineGraphSeries<DataPoint>();
        xyWertAZ = new LineGraphSeries<DataPoint>();

        graphA.addSeries(xyWertA);
        graphA.addSeries(xyWertAX);
        graphA.addSeries(xyWertAY);
        graphA.addSeries(xyWertAZ);

        xyWertA.setColor(Color.MAGENTA);
        xyWertAX.setColor(Color.BLUE);
        xyWertAY.setColor(Color.GREEN);
        xyWertAZ.setColor(Color.RED);


        graphG = view.findViewById(R.id.graphViewG);
        //graphG.getViewport().setYAxisBoundsManual(false);
        graphG.getViewport().setXAxisBoundsManual(true);
        graphG.getViewport().setMaxY(0);
        graphG.getViewport().setMinY(0);
        graphG.getViewport().setMaxX(1000);
        graphG.getViewport().setMinX(0);

        xyWertG = new LineGraphSeries<DataPoint>();
        xyWertGX = new LineGraphSeries<DataPoint>();
        xyWertGY = new LineGraphSeries<DataPoint>();
        xyWertGZ = new LineGraphSeries<DataPoint>();

        graphG.addSeries(xyWertG);
        graphG.addSeries(xyWertGX);
        graphG.addSeries(xyWertGY);
        graphG.addSeries(xyWertGZ);

        xyWertG.setColor(Color.YELLOW);
        xyWertGX.setColor(Color.BLUE);
        xyWertGY.setColor(Color.GREEN);
        xyWertGZ.setColor(Color.RED);
    }

    public float getds(LinkedList<SensorNode> ll){
        float help = 0f;
        for(SensorNode sn : ll){
            help += sn.getMAG();
        }
        help = help / ll.size();
        return help;
    }

    public float getmin(LinkedList<SensorNode> ll){
        float help = Float.MAX_VALUE;
        for(SensorNode sn : ll){
            if(help > sn.getMAG()){
                help = sn.getMAG();
            }
        }

        return help;
    }
    public float getmax(LinkedList<SensorNode> ll){
        float help = Float.MIN_VALUE;
        for(SensorNode sn : ll){
            if(help < sn.getMAG()){
                help = sn.getMAG();
            }
        }
        return help;
    }


}