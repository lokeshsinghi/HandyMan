package com.example.locale_lite;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class Main2Activity extends AppCompatActivity {

    Button locbut,passbut,reqbut,logbut,histbut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        locbut= (Button) findViewById(R.id.change_location);
        passbut= (Button) findViewById(R.id.change_password);
        reqbut= (Button) findViewById(R.id.request_sent);
        logbut= (Button) findViewById(R.id.logout);
        histbut= (Button) findViewById(R.id.service_history);

    }

}
