package com.example.sensordaten;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;


public class SettingsFragment extends Fragment {

    SettingsKlass settingsKlass = SettingsKlass.getInstance();
    Switch sRG;
    Spinner spAcc;
    Spinner spGyro;
    Spinner spKlass;

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spAcc = view.findViewById(R.id.spAcc);
        spGyro = view.findViewById(R.id.spGyro);
        spKlass = view.findViewById(R.id.spKlasse);

        ArrayList<String> items = new ArrayList<>();
        items.add("FASTEST");
        items.add("GAME");
        items.add("UI");
        items.add("NORMAL");
        /*
        items.add("SENSOR_DELAY_FASTEST");
        items.add("SENSOR_DELAY_GAME");
        items.add("SENSOR_DELAY_UI");
        items.add("SENSOR_DELAY_NORMAL");
        * */

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAcc.setAdapter(adapter);
        spGyro.setAdapter(adapter);

        ArrayList<String> itemsKlass = new ArrayList<>();
        itemsKlass.add("U");
        itemsKlass.add("S");
        itemsKlass.add("L");
        itemsKlass.add("R");

        ArrayAdapter<String> adapterKlass = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, itemsKlass);
        adapterKlass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKlass.setAdapter(adapterKlass);

        spAcc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settingsKlass.setSpeedAcc(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                settingsKlass.setSpeedAcc(3);
            }
        });
        spGyro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settingsKlass.setSpeedGyro(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                settingsKlass.setSpeedGyro(3);
            }
        });
        spKlass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settingsKlass.setKlassenType(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                settingsKlass.setKlassenType(0);
            }
        });

        spGyro.setSelection(3);
        spAcc.setSelection(3);
        spKlass.setSelection(0);

        sRG = view.findViewById(R.id.sRG);
        sRG.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    settingsKlass.switchRG();
                }else{
                    settingsKlass.switchRG();
                }
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.settingsfragment, container, false);
    }
}