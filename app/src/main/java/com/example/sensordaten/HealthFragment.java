package com.example.sensordaten;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class HealthFragment extends Fragment {

    EditText edGroeße;
    EditText edGewicht;

    EditText edStepCount;
    EditText edWishStep;
    Button btSave;

    SharedPreferences sharedPreferences;

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getContext().getSharedPreferences("SettingsHealthApp", Context.MODE_PRIVATE);

        edGewicht = view.findViewById(R.id.edGewicht);
        edGroeße = view.findViewById(R.id.edGroeße);
        edStepCount = view.findViewById(R.id.edStepCount);
        edWishStep = view.findViewById(R.id.edWishSteps);


        String gespeicherteDaten = sharedPreferences.getString("greoße", "");
        edGroeße.setText(gespeicherteDaten);

        gespeicherteDaten = sharedPreferences.getString("gewicht", "");
        edGewicht.setText(gespeicherteDaten);

        gespeicherteDaten = sharedPreferences.getString("StepCounter", "");
        edStepCount.setText(gespeicherteDaten);

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