package com.example.sensordaten;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
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
    float gD;
    LinkedList<SensorNode> aList;
    float aD;

    LinkedList<Float> gListAvg;
    int countGListAvg = 0;
    float[] avgHelper = new float[5];

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
        sharedPreferences = getContext().getSharedPreferences("SettingsHealthApp", Context.MODE_PRIVATE);


        btSave = view.findViewById(R.id.btSave);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss-SSS", Locale.getDefault());
               // String currentDateandTime = sdf.format(new Date());
                //pfadDownload = (requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)).getPath();
                //pfadDownload = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).getPath();
                File pathdownload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                //pathdownload = new File(pathdownload, "SubDir");
                //pathdownload.mkdir();

                Log.d("debug",pathdownload.toString() );

                String trenner = ",";
                String klasstype = "";
                switch (settingsKlass.getKlassenType()){
                    case 0:{
                        klasstype = "U";
                    }break;
                    case 1:{
                        klasstype = "S";
                    }break;
                    case 2:{
                        klasstype = "L";
                    }break;
                    case 3:{
                        klasstype = "R";
                    }break;
                }

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

                    if(fileIsnew){
                        bw.write("id" + trenner + "date" + trenner + "gMag" + trenner + "klasenType");
                    }

                    sharedPreferences = getContext().getSharedPreferences("SettingsHealthApp", Context.MODE_PRIVATE);
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

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("countId", countId + "");
                    editor.apply();
                    bw.close();

                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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

                String klasstype = sharedPreferences.getString("KlasseSetting", "0");

                switch (event.sensor.getType()){
                    case Sensor.TYPE_ACCELEROMETER:{
                        tvXA.setText(event.values[0] + "");
                        tvYA.setText(event.values[1] + "");
                        tvZA.setText(event.values[2] + "");

                        //currentDateandTime = sdf.format(new Date());
                        //aList.add(new SensorNode(event.values[0], event.values[1], event.values[2], currentDateandTime));
                        aList.add(new SensorNode(event.values[0], event.values[1], event.values[2], System.currentTimeMillis(), klasstype));

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

                            //currentDateandTime = sdf.format(new Date());
                            //gList.add(new SensorNode(event.values[0], event.values[1], event.values[2], currentDateandTime));
                            gList.add(new SensorNode(event.values[0], event.values[1], event.values[2], System.currentTimeMillis(), klasstype));

                            /*
                            avgHelper[countGListAvg] = (float) Math.sqrt((event.values[0])*(event.values[0]) + (event.values[1])*(event.values[1]) + (event.values[2])*(event.values[2]));
                            countGListAvg++;

                            if(countGListAvg == 5){
                                float helper = 0;
                                for (float f: avgHelper ){
                                    helper +=f;
                                }
                                gListAvg.add(helper/5);
                            }
*/

                            //xyWertG.appendData(new DataPoint(gList.size(), gList.getLast().getMAG()), false, 300);
                            if(gList.size() > 300){
                                xyWertG.appendData(new DataPoint(gList.size(), gList.getLast().getMAG()), true, 300);
                                xyWertGX.appendData(new DataPoint(gList.size(), Math.rint(Math.toDegrees(event.values[0]) * 100) / 100), true, 300);
                                xyWertGY.appendData(new DataPoint(gList.size(), Math.rint(Math.toDegrees(event.values[1]) * 100) / 100), true, 300);
                                xyWertGZ.appendData(new DataPoint(gList.size(), Math.rint(Math.toDegrees(event.values[2]) * 100) / 100), true, 300);
                            }else{
                                xyWertG.appendData(new DataPoint(gList.size(), gList.getLast().getMAG()), false, 300);
                                xyWertGX.appendData(new DataPoint(gList.size(), Math.rint(Math.toDegrees(event.values[0]) * 100) / 100), false, 300);
                                xyWertGY.appendData(new DataPoint(gList.size(), Math.rint(Math.toDegrees(event.values[1]) * 100) / 100), false, 300);
                                xyWertGZ.appendData(new DataPoint(gList.size(), Math.rint(Math.toDegrees(event.values[2]) * 100) / 100), false, 300);
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

                            tvXG.setText(Math.rint(Math.toDegrees(out[0]) * 100) / 100 + "");
                            tvYG.setText(Math.rint(Math.toDegrees(out[1]) * 100) / 100 + "");
                            tvZG.setText(Math.rint(Math.toDegrees(out[2]) * 100) / 100 + "");

                            //currentDateandTime = sdf.format(new Date());
                            //gList.add(new SensorNode((float)Math.toDegrees(out[0]), (float)Math.toDegrees(out[1]), (float)Math.toDegrees(out[2]), currentDateandTime));
                            gList.add(new SensorNode((float)Math.toDegrees(out[0]), (float)Math.toDegrees(out[1]), (float)Math.toDegrees(out[2]), System.currentTimeMillis(), klasstype));

                            if(gList.size() > 300){
                                xyWertG.appendData(new DataPoint(gList.size(), gList.getLast().getMAG()), true, 300);
                                xyWertGX.appendData(new DataPoint(gList.size(), Math.rint(Math.toDegrees(out[0]) * 100) / 100), true, 300);
                                xyWertGY.appendData(new DataPoint(gList.size(), Math.rint(Math.toDegrees(out[1]) * 100) / 100), true, 300);
                                xyWertGZ.appendData(new DataPoint(gList.size(), Math.rint(Math.toDegrees(out[2]) * 100) / 100), true, 300);

                            }else{
                                xyWertG.appendData(new DataPoint(gList.size(), gList.getLast().getMAG()), false, 300);
                                xyWertGX.appendData(new DataPoint(gList.size(), Math.rint(Math.toDegrees(out[0]) * 100) / 100), false, 300);
                                xyWertGY.appendData(new DataPoint(gList.size(), Math.rint(Math.toDegrees(out[1]) * 100) / 100), false, 300);
                                xyWertGZ.appendData(new DataPoint(gList.size(), Math.rint(Math.toDegrees(out[2]) * 100) / 100), false, 300);

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