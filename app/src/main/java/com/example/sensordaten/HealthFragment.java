package com.example.sensordaten;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;

public class HealthFragment extends Fragment {

    EditText edGroeße;
    EditText edGewicht;
    EditText edAlter;
    EditText edBurnedKalorien;
    EditText edStepCount;
    EditText edWishStep;
    Button btSave;
    Handler handler;

    SharedPreferences sharedPreferences;
    String date;
    int selectGeschlecht;
    Spinner spGeschlecht;
    SimpleDateFormat df = new SimpleDateFormat("dd", Locale.getDefault());

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handler = new Handler();
        final int delayMillis = 200;

        sharedPreferences = getContext().getSharedPreferences("SettingsHealthApp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("StepCounter", String.valueOf(0));
        editor.apply();
        date = df.format(Calendar.getInstance().getTime());
        editor = sharedPreferences.edit();
        editor.putInt("CurrentDate", Integer.parseInt(date));
        editor.apply();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateHeathViewWithData();
                checkDate();
                updateKalorien();
                handler.postDelayed(this, delayMillis);
            }
        }, delayMillis);

        edGewicht = view.findViewById(R.id.edGewicht);
        edGroeße = view.findViewById(R.id.edGroeße);
        edStepCount = view.findViewById(R.id.edStepCount);
        edWishStep = view.findViewById(R.id.edWishSteps);
        edAlter = view.findViewById(R.id.edAlter);
        edBurnedKalorien = view.findViewById(R.id.edBurnedKalorien);

        spGeschlecht = view.findViewById(R.id.spGeschlecht);
        ArrayList<String> items = new ArrayList<>();
        items.add("Männlich");
        items.add("Weiblich");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGeschlecht.setAdapter(adapter);

        if(sharedPreferences.getString("geschlecht", "0").equals("Männlich")){
            spGeschlecht.setSelection(0);
        }else if (sharedPreferences.getString("geschlecht", "0").equals("Weiblich")){
            spGeschlecht.setSelection(1);
        }


        spGeschlecht.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(position == 0){
                    editor.putString("geschlecht", "Männlich");
                }else{
                    editor.putString("geschlecht", "Weiblich");
                }
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        String gespeicherteDaten = sharedPreferences.getString("greoße", "");
        edGroeße.setText(gespeicherteDaten);

        gespeicherteDaten = sharedPreferences.getString("alter", "");
        edAlter.setText(gespeicherteDaten);

        gespeicherteDaten = sharedPreferences.getString("gewicht", "");
        edGewicht.setText(gespeicherteDaten);

        gespeicherteDaten = sharedPreferences.getString("WishSteps", "");
        edWishStep.setText(gespeicherteDaten);

        edGroeße.addTextChangedListener(textwatchGroese);
        edGewicht.addTextChangedListener(textwatchGewicht);
        edStepCount.addTextChangedListener(textwatchStepCounter);
        edWishStep.addTextChangedListener(textwatchWishSteps);
        edAlter.addTextChangedListener(textwatchAlter);

    }

    private void updateKalorien() {
        float schritte;
        float größe;
        float alter;
        float gewicht;
        float BMR;
        float schrittreichweite;


        if(edStepCount.getText().length() != 0 && edGroeße.getText().length() != 0 && edAlter.getText().length() != 0 && edGewicht.getText().length() != 0){
            schritte = Float.parseFloat(String.valueOf(edStepCount.getText()));
            größe = Float.parseFloat(String.valueOf(edGroeße.getText()));
            alter = Float.parseFloat(String.valueOf(edAlter.getText()));
            gewicht = Float.parseFloat(String.valueOf(edGewicht.getText()));

            if(spGeschlecht.getSelectedItemPosition() == 0){
                BMR = (10 * gewicht) + (6.25f * größe) - (5 * alter) + 5;
            }else{
                BMR = (10 * gewicht) + (6.25f * größe) - (5 * alter)  - 161;
            }
           // float kcal = ((BMR / 24) * (3.9f / 60) * gewicht) / schritte;

            schrittreichweite = (größe / 100) * 0.5f;

            float kcal = (schrittreichweite / 1000f) * schritte * gewicht * 0.9f;
            edBurnedKalorien.setText(kcal + "");

            //1648,75
        }

    }

    private void checkDate() {
        int toCheck = sharedPreferences.getInt("CurrentDate",32);
        if (toCheck == 32) {
            ;
        } else {
            if (toCheck != Integer.parseInt(df.format(Calendar.getInstance().getTime()))) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("CurrentDate", Integer.parseInt(df.format(Calendar.getInstance().getTime())));
                editor.apply();
                Log.d("Day", String.valueOf(sharedPreferences.getInt("CurrentDate",32)));
                editor = sharedPreferences.edit();
                editor.putString("StepCounter", "0");
                editor.apply();
            }

        }
    }

    private void updateHeathViewWithData() {
        String gespeicherteDaten = sharedPreferences.getString("StepCounter", "");
        edStepCount.setText(gespeicherteDaten);
    }

    private TextWatcher textwatchGewicht = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            safeSettings("gewicht", s.toString());

        }
    };

    private TextWatcher textwatchGroese = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            safeSettings("greoße", s.toString());

        }
    };

    private TextWatcher textwatchStepCounter = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            safeSettings("StepCounter", s.toString());

        }
    };

    private TextWatcher textwatchWishSteps = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            safeSettings("WishSteps", s.toString());

        }
    };

    private TextWatcher textwatchAlter = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            safeSettings("alter", s.toString());

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.healthfragment, container, false);
    }

    public void safeSettings(String b, String t){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(b, t);
        editor.apply();
    }
}