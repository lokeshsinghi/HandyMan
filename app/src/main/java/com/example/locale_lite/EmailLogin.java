package com.example.locale_lite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmailLogin extends AppCompatActivity {

    TextView useMobile, signup;
    TextView reset;
    EditText email, pwd;
    Button login;
    ProgressBar pbar;
    private FirebaseAuth mAuth;
    LinearLayout mainLayout;


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
        reset = findViewById(R.id.forgot);
        mainLayout = (LinearLayout) findViewById(R.id.container);

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
                finish();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmailLogin.this, ResetPassword.class);
                startActivity(intent);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
                final String emailid = email.getText().toString();
                final String pword = pwd.getText().toString();
                if (email.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
                    email.setError("Enter valid Email Id!");
                else if (pwd.getText().toString().isEmpty())
                    pwd.setError("Enter password");
                else {
                    pbar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(emailid, pword)
                            .addOnCompleteListener(EmailLogin.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    pbar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                        final String userid = firebaseUser.getUid();
                                        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("Customers");
                                        database.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                int count = 0;
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    Customers c = snapshot.getValue(Customers.class);
                                                    if (c.getId().equals(userid)) {
                                                        count = 1;
                                                        break;
                                                    }
                                                }

                                                if (count == 1) {
                                                    Toast.makeText(EmailLogin.this, "Logged in", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(EmailLogin.this, Main2Activity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    DatabaseReference database1 = FirebaseDatabase.getInstance().getReference("ServiceProviders");
                                                    database1.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            int cont = 0;
                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                ServiceProviders sp = dataSnapshot1.getValue(ServiceProviders.class);
                                                                if (sp.getId().equals(userid) && sp.getPending().equals(true)) {
                                                                    cont = 1;
                                                                    break;
                                                                }
                                                            }
                                                            if (cont == 1) {
                                                                Toast.makeText(EmailLogin.this, "Logged in", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(EmailLogin.this, sp_homepage.class);
                                                                startActivity(intent);
                                                                finish();
                                                            } else {
                                                                Toast.makeText(EmailLogin.this, "Not verified yet", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(EmailLogin.this, Pending.class);
                                                                startActivity(intent);
                                                                finish();

                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });


                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                    } else {
                                        Toast.makeText(EmailLogin.this, "Incorrect credentials or User not registered", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }

        });
    }
}
