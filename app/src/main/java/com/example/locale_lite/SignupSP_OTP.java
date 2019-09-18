package com.example.locale_lite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class SignupSP_OTP extends AppCompatActivity {

    private String otp;
    private FirebaseAuth mAuth;
    private ProgressBar pbar;
    private EditText editText;
    TextView resendotp;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_sp__otp);

        mAuth = FirebaseAuth.getInstance();
        pbar = findViewById(R.id.loading);
        editText = findViewById(R.id.editOTP);
        resendotp = findViewById(R.id.resend);
        final String phonenumber = getIntent().getStringExtra("phonenumber");
        sendOTP(phonenumber);






        findViewById(R.id.resend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOTP(phonenumber);
                Toast.makeText(SignupSP_OTP.this, "OTP sent again", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view)  {
                String code = editText.getText().toString().trim();

                if(code.isEmpty() || code.length()<6)  {
                    editText.setError("Enter Code!");
                    editText.requestFocus();
                    return;
                }
                pbar.setVisibility(View.VISIBLE);
                verifycode(code);
            }
        });
    }

    private void verifycode(String code)   {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otp, code );
        signInWithCredential(credential);

    }
    private void signInWithCredential(PhoneAuthCredential credential)   {
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                           Bundle bundle = getIntent().getExtras();

                                        Toast.makeText(SignupSP_OTP.this, "Registered Successfully", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(SignupSP_OTP.this, asklocationSP.class);
                           intent.putExtras(bundle);
                            startActivity(intent);
                            finish();

                        }else{

                            Toast.makeText(SignupSP_OTP.this,"Wrong OTP!"
                                    , Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private void sendOTP(String number) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            otp = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if(code != null)    {
                pbar.setVisibility(View.VISIBLE);
                verifycode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(SignupSP_OTP.this,"Verification Failed! ",Toast.LENGTH_LONG).show();
        }
    };
}