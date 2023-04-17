package com.example.sensordaten;

import static android.content.Context.SENSOR_SERVICE;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import java.util.LinkedList;


public class GraphFragment extends Fragment {

    SettingsKlass settingsKlass = SettingsKlass.getInstance();

    SensorManager sensorManager;
    SensorEventListener sensorEventListener;
    Switch sswitch;
    TextView tvXG;
    TextView tvYG;
    TextView tvZG;
    TextView tvXA;
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
    float gD;
    LinkedList<SensorNode> aList;
    float aD;

    GraphView graphA;
    GraphView graphG;

    LineGraphSeries<DataPoint> xyWertA;
    LineGraphSeries<DataPoint> xyWertAX;
    LineGraphSeries<DataPoint> xyWertAY;
    LineGraphSeries<DataPoint> xyWertAZ;
    LineGraphSeries<DataPoint> xyWertG;
    LineGraphSeries<DataPoint> xyWertGX;
    LineGraphSeries<DataPoint> xyWertGY;
    LineGraphSeries<DataPoint> xyWertGZ;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        settingsKlass = SettingsKlass.getInstance();

        btSave = view.findViewById(R.id.btSave);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String trenner = ";";
                    File file = new File("/storage/emulated/Download/CA_Daten_Acc_CSV" +".csv");

                    if (!file.exists()) {
                        file.createNewFile();
                    }

                    FileWriter fw = new FileWriter(file.getPath());
                    //FileWriter fw = new FileWriter("/storage/emulated/Download" + file);
                    BufferedWriter bw = new BufferedWriter(fw);
                    for (SensorNode n: aList) {
                        bw.write(n.getData(trenner));
                    }
                    bw.close();
                    aList.clear();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    String trenner = ";";
                    File file = new File("CA_Daten_Gyro_CSV" +".csv");

                    if (!file.exists()) {
                        file.createNewFile();
                    }

                    FileWriter fw = new FileWriter(file.getAbsoluteFile());

                    BufferedWriter bw = new BufferedWriter(fw);
                    for (SensorNode n: gList) {
                        bw.write(n.getData(trenner));
                    }
                    bw.close();
                    gList.clear();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //view = inflater.inflate(R.layout.fragment_blank, container,  false);

        //tvXG = findViewById(R.id.textView5);
        tvXG = view.findViewById(R.id.textView5);
        tvYG = view.findViewById(R.id.textView9);
        tvZG = view.findViewById(R.id.textView13);

        tvXA = view.findViewById(R.id.textView3);
        tvYA = view.findViewById(R.id.textView7);
        tvZA = view.findViewById(R.id.textView11);

        tvMinA = view.findViewById(R.id.textView24);
        tvMaxA = view.findViewById(R.id.textView19);
        tvDsA = view.findViewById(R.id.textView27);

        tvMinG = view.findViewById(R.id.textView26);
        tvMaxG = view.findViewById(R.id.textView21);
        tvDsG = view.findViewById(R.id.textView23);

        initGraph(view);

        sswitch = view.findViewById(R.id.swSensor);
        sswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sensorManager.registerListener(sensorEventListener,sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), settingsKlass.getSpeedGyro());
                    sensorManager.registerListener(sensorEventListener,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), settingsKlass.getSpeedAcc());
                    sensorManager.registerListener(sensorEventListener,sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), settingsKlass.getSpeedAcc());
                }else{
                    sensorManager.unregisterListener(sensorEventListener);
                }
            }
        });

        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                switch (event.sensor.getType()){
                    case Sensor.TYPE_ACCELEROMETER:{
                        tvXA.setText(event.values[0] + "");
                        tvYA.setText(event.values[1] + "");
                        tvZA.setText(event.values[2] + "");


                        aList.add(new SensorNode(event.values[0], event.values[1], event.values[2]));

                        if(aList.size() > 1000){
                            xyWertA.appendData(new DataPoint(aList.size(), aList.getLast().getMAG()), true, 1000);
                            xyWertAX.appendData(new DataPoint(aList.size(), event.values[0]), true, 1000);
                            xyWertAY.appendData(new DataPoint(aList.size(), event.values[1]), true, 1000);
                            xyWertAZ.appendData(new DataPoint(aList.size(), event.values[2]), true, 1000);
                        }else{
                            xyWertAZ.appendData(new DataPoint(aList.size(), event.values[2]), false, 1000);
                            xyWertA.appendData(new DataPoint(aList.size(), aList.getLast().getMAG()), false, 1000);
                            xyWertAX.appendData(new DataPoint(aList.size(), event.values[0]), false, 1000);
                            xyWertAY.appendData(new DataPoint(aList.size(), event.values[1]), false, 1000);
                        }

                        if(!aList.isEmpty()){
                            tvMaxA.setText(getmax(aList) + "");
                            tvMinA.setText(getmin(aList) + "");
                            tvDsA.setText(getds(aList) + "");
                        }
                    }break;
                    case Sensor.TYPE_GYROSCOPE:{

                        if(settingsKlass.getSwitchRGState()){

                            tvXG.setText((event.values[0]) + "");
                            tvYG.setText((event.values[1]) + "");
                            tvZG.setText((event.values[2]) + "");

                            gList.add(new SensorNode(event.values[0], event.values[1], event.values[2]));

                            //xyWertG.appendData(new DataPoint(gList.size(), gList.getLast().getMAG()), false, 300);
                            if(gList.size() > 300){
                                xyWertG.appendData(new DataPoint(gList.size(), gList.getLast().getMAG()), true, 300);
                                xyWertGX.appendData(new DataPoint(gList.size(), Math.round(Math.toDegrees(event.values[0]) * 100) / 100), true, 300);
                                xyWertGY.appendData(new DataPoint(gList.size(), Math.round(Math.toDegrees(event.values[1]) * 100) / 100), true, 300);
                                xyWertGZ.appendData(new DataPoint(gList.size(), Math.round(Math.toDegrees(event.values[2]) * 100) / 100), true, 300);
                            }else{
                                xyWertG.appendData(new DataPoint(gList.size(), gList.getLast().getMAG()), false, 300);
                                xyWertGX.appendData(new DataPoint(gList.size(), Math.round(Math.toDegrees(event.values[0]) * 100) / 100), false, 300);
                                xyWertGY.appendData(new DataPoint(gList.size(), Math.round(Math.toDegrees(event.values[1]) * 100) / 100), false, 300);
                                xyWertGZ.appendData(new DataPoint(gList.size(), Math.round(Math.toDegrees(event.values[2]) * 100) / 100), false, 300);
                            }

                            if(!gList.isEmpty()){
                                tvMaxG.setText(getmax(gList) + "");
                                tvMinG.setText(getmin(gList) + "");
                                tvDsG.setText(getds(gList) + "");
                            }

                        }

                    }break;
                    case Sensor.TYPE_ROTATION_VECTOR:{

                        if(!settingsKlass.getSwitchRGState()){
                            float[] rotMat = new float[16];
                            sensorManager.getRotationMatrixFromVector(rotMat, event.values);

                            float[] remap = new float[16];
                            sensorManager.remapCoordinateSystem(rotMat,SensorManager.AXIS_X, SensorManager.AXIS_Z, remap);

                            float[] out = new float[3];
                            sensorManager.getOrientation(remap, out);

                            tvXG.setText(Math.round(Math.toDegrees(out[0]) * 100) / 100 + "");
                            tvYG.setText(Math.round(Math.toDegrees(out[1]) * 100) / 100 + "");
                            tvZG.setText(Math.round(Math.toDegrees(out[2]) * 100) / 100 + "");

                            gList.add(new SensorNode((float)(Math.round(Math.toDegrees(out[0]) * 100) / 100), (float)(Math.round(Math.toDegrees(out[1]) * 100) / 100), (float)(Math.round(Math.toDegrees(out[2]) * 100) / 100)));

                            if(gList.size() > 300){
                                xyWertG.appendData(new DataPoint(gList.size(), gList.getLast().getMAG()), true, 300);
                                xyWertGX.appendData(new DataPoint(gList.size(), Math.round(Math.toDegrees(out[0]) * 100) / 100), true, 300);
                                xyWertGY.appendData(new DataPoint(gList.size(), Math.round(Math.toDegrees(out[1]) * 100) / 100), true, 300);
                                xyWertGZ.appendData(new DataPoint(gList.size(), Math.round(Math.toDegrees(out[2]) * 100) / 100), true, 300);

                            }else{
                                xyWertG.appendData(new DataPoint(gList.size(), gList.getLast().getMAG()), false, 300);
                                xyWertGX.appendData(new DataPoint(gList.size(), Math.round(Math.toDegrees(out[0]) * 100) / 100), false, 300);
                                xyWertGY.appendData(new DataPoint(gList.size(), Math.round(Math.toDegrees(out[1]) * 100) / 100), false, 300);
                                xyWertGZ.appendData(new DataPoint(gList.size(), Math.round(Math.toDegrees(out[2]) * 100) / 100), false, 300);

                            }
                        }

                    }break;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }

        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.graphfragment, container, false);
    }

    private void initGraph(View view) {
        aList = new LinkedList<SensorNode>();
        gList = new LinkedList<SensorNode>();

        graphA = view.findViewById(R.id.graphViewA);
        //graphA.getViewport().setYAxisBoundsManual(true);
        graphA.getViewport().setXAxisBoundsManual(true);
        graphA.getViewport().setMaxY(100);
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
        graphG.getViewport().setYAxisBoundsManual(false);
        graphG.getViewport().setXAxisBoundsManual(true);
        graphG.getViewport().setMaxY(300);
        graphG.getViewport().setMinY(-300);
        graphG.getViewport().setMaxX(300);
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