package com.example.locale_lite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class CreateAccount<findView> extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText firstName, lastName, emailId, phoneNum, password, cpassword;
    TextView signIn;
    ImageView googleSignup;
    Button next, submit;
    Spinner cityList, categories;
    RadioGroup gender, accType;
    RadioButton male, female, others, typecus, typeser;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        categories = (Spinner) findViewById(R.id.categories);
    firstName = (EditText)findViewById(R.id.firstname);
    lastName = (EditText) findViewById(R.id.lastname);
    emailId = (EditText) findViewById(R.id.email);
    phoneNum = (EditText)findViewById(R.id.phone);
    password = (EditText)findViewById(R.id.pwd);
    cpassword = (EditText) findViewById(R.id.confirmpwd);
    cityList = (Spinner) findViewById(R.id.citylist);
    gender = (RadioGroup) findViewById(R.id.gender);
    male = (RadioButton) findViewById(R.id.checkMale);
    female = (RadioButton) findViewById(R.id.checkFemale);
    others = (RadioButton) findViewById(R.id.checkOthers);
    typecus = (RadioButton) findViewById(R.id.checkCustomer);
    typeser = (RadioButton) findViewById(R.id.checkServiceProvider);
    accType = (RadioGroup) findViewById(R.id.acctype);
    googleSignup = (ImageView) findViewById(R.id.google);
    progressBar = (ProgressBar) findViewById(R.id.pBar);
    progressBar.setVisibility(View.GONE);

    mAuth = FirebaseAuth.getInstance();


    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId()
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });

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
            final String firstname = firstName.getText().toString().trim();
            final String lastname = lastName.getText().toString().trim();
            final String emailid = emailId.getText().toString();
            final String phonenum = phoneNum.getText().toString();
            final String city = cityList.getSelectedItem().toString();
            final String pword = password.getText().toString();
            final String cpword = cpassword.getText().toString();
            final String category = categories.getSelectedItem().toString();
            if (firstName.getText().toString().trim().equalsIgnoreCase(""))
                firstName.setError("First name is required!");

            else if (lastName.getText().toString().trim().equalsIgnoreCase(""))
                lastName.setError("Last name is required!");

            else if (emailId.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailId.getText().toString()).matches())
                emailId.setError("Enter valid Email Id!");

            else if (phoneNum.getText().toString().length()<10)
                phoneNum.setError("Enter valid Phone Number!");
            else if (gender.getCheckedRadioButtonId() == -1)
            {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Please Select Gender!",
                        Toast.LENGTH_LONG);
                toast.show();
            }
            else if(cityList.getSelectedItem().toString().matches("Select City"))
            { Toast toast = Toast.makeText(getApplicationContext(),
                    "Select City!",
                    Toast.LENGTH_LONG);
                toast.show();
            }
            else if(password.getText().toString().length()<8)
                password.setError("Password of minimum 8 characters is required");
            else if(!password.getText().toString().matches(cpassword.getText().toString()))
                password.setError("Passwords do not match. Try again!");
            else if(categories.getSelectedItem().toString().matches("Select Category"))
            { Toast toast = Toast.makeText(getApplicationContext(),
                    "Select Category!",
                    Toast.LENGTH_LONG);
                toast.show();
            }
            else    {
                progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(emailid,pword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    ServiceProviders sp = new ServiceProviders(firstname, lastname, emailid, phonenum, city, pword, cpword, category);
                                    FirebaseDatabase.getInstance().getReference("ServiceProvider")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(sp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressBar.setVisibility(View.GONE);
                                            if(task.isSuccessful()) {
                                                Toast.makeText(CreateAccount.this, "Registered Successfully", Toast.LENGTH_LONG).show();
//                                                Intent intent = new Intent(CreateAccount.this, ProfileServiceProvider.class);
//                                                startActivity(intent);
                                            }
                                        }
                                    });
                                }
                                else{
                                    Toast.makeText(CreateAccount.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });
        }}

    });


    submit = (Button)findViewById(R.id.btSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String firstname = firstName.getText().toString().trim();
                final String lastname = lastName.getText().toString().trim();
                final String emailid = emailId.getText().toString();
                final String phonenum = phoneNum.getText().toString();
                final String city = cityList.getSelectedItem().toString();
                final String pword = password.getText().toString();
                final String cpword = cpassword.getText().toString();
                if (firstName.getText().toString().trim().equalsIgnoreCase(""))
                    firstName.setError("First name is required!");

                else if (lastName.getText().toString().trim().equalsIgnoreCase(""))
                    lastName.setError("Last name is required!");

                else if (emailId.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailId.getText().toString()).matches())
                    emailId.setError("Enter valid Email Id!");

                else if (phoneNum.getText().toString().length()<10)
                    phoneNum.setError("Enter valid Phone Number!");

                else if (gender.getCheckedRadioButtonId() == -1)
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Please Select Gender!",
                            Toast.LENGTH_LONG);
                    toast.show();
                }
                else if(cityList.getSelectedItem().toString().matches("Select City"))
                { Toast toast = Toast.makeText(getApplicationContext(),
                        "Select City!",
                        Toast.LENGTH_LONG);
                    toast.show();
                }
                else if(password.getText().toString().length()<8)
                    password.setError("Password of minimum 8 characters is required");
                else if(!password.getText().toString().matches(cpassword.getText().toString()))
                    password.setError("Passwords do not match. Try again!");
                else    {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(emailid,pword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Customers customer = new Customers(firstname, lastname, emailid, phonenum, city);
                                        FirebaseDatabase.getInstance().getReference("Customers")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(customer).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressBar.setVisibility(View.GONE);
                                                if(task.isSuccessful()) {
                                                    Toast.makeText(CreateAccount.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                                                    String phoneNumber = "+91" + phonenum;
                                                    Intent intent = new Intent(CreateAccount.this, Signup_OTP.class);
                                                    intent.putExtra("phonenumber", phoneNumber);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                    }
                                    else{
                                        Toast.makeText(CreateAccount.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }}

        });

        Spinner cityspinner = (Spinner) findViewById(R.id.citylist);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Cities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cityspinner.setAdapter(adapter);
        cityspinner.setOnItemSelectedListener(this);

        Spinner categoryspinner = (Spinner) findViewById(R.id.categories);
        ArrayAdapter<CharSequence> adapt = ArrayAdapter.createFromResource(this,
                R.array.Categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(adapt);
        categories.setOnItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null){
            //already login
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.checkCustomer:
                if (checked)
                    next.setVisibility(View.INVISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    categories.setVisibility(View.INVISIBLE);
                    break;
            case R.id.checkServiceProvider:
                if (checked)
                    next.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.INVISIBLE);
                    categories.setVisibility(View.VISIBLE);
                    break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
