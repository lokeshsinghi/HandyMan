package com.example.locale_lite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class CreateAccount<findView> extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText firstName, lastName, emailId, phoneNum, password, cpassword;
    TextView signIn;
    Button next;
    Spinner cityList;
    RadioGroup gender, accType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

    firstName = (EditText)findViewById(R.id.firstname);
    lastName = (EditText) findViewById(R.id.lastname);
    emailId = (EditText) findViewById(R.id.email);
    phoneNum = (EditText)findViewById(R.id.phone);
    password = (EditText)findViewById(R.id.pwd);
    cpassword = (EditText) findViewById(R.id.confirmpwd);
    cityList = (Spinner) findViewById(R.id.citylist);
    gender = (RadioGroup) findViewById(R.id.gender);

    signIn = (TextView) findViewById(R.id.signin);
    signIn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)  {
            Intent intent = new Intent(CreateAccount.this, Login.class);
            startActivity(intent);
        }
    });

    next = (Button)findViewById(R.id.btNext);
    next.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(CreateAccount.this, ProfileServiceProvider.class);
            startActivity(intent);
        }
    });



        Spinner cityspinner = (Spinner) findViewById(R.id.citylist);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Cities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cityspinner.setAdapter(adapter);
        cityspinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
