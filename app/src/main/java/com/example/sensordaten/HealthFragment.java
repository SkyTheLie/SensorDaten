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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;

public class HealthFragment extends Fragment {

    EditText edGroeße;
    EditText edGewicht;

    EditText edStepCount;
    EditText edWishStep;
    Button btSave;
    Handler handler;

    SharedPreferences sharedPreferences;
    String date;
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
                handler.postDelayed(this, delayMillis);
            }
        }, delayMillis);

        edGewicht = view.findViewById(R.id.edGewicht);
        edGroeße = view.findViewById(R.id.edGroeße);
        edStepCount = view.findViewById(R.id.edStepCount);
        edWishStep = view.findViewById(R.id.edWishSteps);


        String gespeicherteDaten = sharedPreferences.getString("greoße", "");
        edGroeße.setText(gespeicherteDaten);

        gespeicherteDaten = sharedPreferences.getString("gewicht", "");
        edGewicht.setText(gespeicherteDaten);

        gespeicherteDaten = sharedPreferences.getString("WishSteps", "");
        edWishStep.setText(gespeicherteDaten);

        edGroeße.addTextChangedListener(textwatchGroese);
        edGewicht.addTextChangedListener(textwatchGewicht);
        edStepCount.addTextChangedListener(textwatchStepCounter);
        edWishStep.addTextChangedListener(textwatchWishSteps);

/*
        btSave = view.findViewById(R.id.safeData);
        btSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("greoße", edGroeße.getText().toString());
                editor.putString("gewicht", edGewicht.getText().toString());
                editor.apply();
            }
        });
*/


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