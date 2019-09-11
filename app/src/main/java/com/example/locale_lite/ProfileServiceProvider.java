package com.example.locale_lite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ProfileServiceProvider extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_service_provider);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

