package com.example.locale_lite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EmailLogin extends AppCompatActivity {

    TextView useMobile, signup;
    EditText email, pwd;
    Button login;
    ProgressBar pbar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_login);

        email = (EditText) findViewById(R.id.editEmail);
        pwd = (EditText) findViewById(R.id.editPassword);
        login = (Button) findViewById(R.id.login);
        signup = (TextView) findViewById(R.id.signup);
        useMobile = (TextView) findViewById(R.id.usemobile);
        pbar = (ProgressBar) findViewById(R.id.pBar);
        mAuth = FirebaseAuth.getInstance();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmailLogin.this, CreateAccount.class);
                startActivity(intent);
            }
        });
        useMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmailLogin.this, Login.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailid = email.getText().toString();
                final String pword = pwd.getText().toString();
                if (email.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
                    email.setError("Enter valid Email Id!");
                else if(pwd.getText().toString().isEmpty())
                    pwd.setError("Enter password");
                else    {
                    pbar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(emailid,pword)
                            .addOnCompleteListener(EmailLogin.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    pbar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(EmailLogin.this,"Logged in",Toast.LENGTH_SHORT).show();
                                      Intent intent = new Intent(EmailLogin.this, Main2Activity.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(EmailLogin.this,"Incorrect credentials or User not registered",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }}

        });
    }
}
