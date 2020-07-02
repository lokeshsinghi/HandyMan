package com.example.locale_lite;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import com.firebase.client.Firebase;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CreateAccount<findView> extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText firstName, lastName, emailId, phoneNum, password, cpassword;
    TextView signIn;

    Button next, submit;
    Spinner cityList, categories;
    Bundle bundle;
    RadioGroup gender, accType;
    RadioButton male, female, others, typecus, typeser;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;

    int p=0;
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

        progressBar = (ProgressBar) findViewById(R.id.pBar);
        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();


        signIn = (TextView) findViewById(R.id.signin);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                Intent intent = new Intent(CreateAccount.this, Login.class);
                startActivity(intent);
            }
        });
        final ArrayList<ServiceProviders> serviceprovidersArrayList = new ArrayList<>();
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


                    final Bundle bundle = new Bundle();
                    bundle.putString("firstname",firstname);
                    bundle.putString("lastname",lastname);
                    bundle.putString("emailid",emailid);
                    bundle.putString("phonenum",phonenum);
                    bundle.putString("city",city);
                    bundle.putString("category",category);


                    mAuth.createUserWithEmailAndPassword(emailid,pword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull final Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    final String phone = phoneNum.getText().toString();
                                    FirebaseDatabase.getInstance().getReference().child("ServiceProviders")
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                        ServiceProviders s = snapshot.getValue(ServiceProviders.class);
                                                        serviceprovidersArrayList.add(s);
                                                        if(s.getPhonenum().toString().equals(phone)) {
                                                            p=1;
                                                            break;
                                                        }
                                                        else
                                                            p=0;
                                                    }
                                                    if(p==1)
                                                    {
                                                        Toast.makeText(CreateAccount.this, "This Phone Number is already registered", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else if (p==0 && task.isSuccessful()) {
                                                        String phoneNumber = "+91" + phonenum;
                                                        Intent intent = new Intent(CreateAccount.this, ProfileServiceProvider.class);
                                                        intent.putExtra("phonenumber",phoneNumber);
                                                        intent.putExtras(bundle);
                                                        startActivity(intent);
                                                    }
                                                    else{
                                                        Toast.makeText(CreateAccount.this, "This Email Id is already registered",Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                }
                                            });



                                }
                            });
                }}

        });
        final ArrayList<Customers> customersArrayList = new ArrayList<>();

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




                    final Bundle bundle = new Bundle();
                    bundle.putString("firstname",firstname);
                    bundle.putString("lastname",lastname);
                    bundle.putString("emailid",emailid);
                    bundle.putString("phonenum",phonenum);
                    bundle.putString("city",city);


                    mAuth.createUserWithEmailAndPassword(emailid,pword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull final Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    final String phone = phoneNum.getText().toString();
                                    FirebaseDatabase.getInstance().getReference().child("Customers")
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                        Customers c = snapshot.getValue(Customers.class);
                                                        customersArrayList.add(c);
                                                        if(c.getPhonenum().toString().equals(phone)) {
                                                            p=1;
                                                        }
                                                        else
                                                            p=0;
                                                    }
                                                    if(p==1)
                                                    {
                                                        Toast.makeText(CreateAccount.this, "This Phone Number is already registered", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else if (p==0 && task.isSuccessful()) {
                                                        String phoneNumber = "+91" + phonenum;
                                                        Intent intent = new Intent(CreateAccount.this, Signup_OTP.class);
                                                        intent.putExtra("phonenumber", phoneNumber);
                                                        intent.putExtras(bundle);
                                                        startActivity(intent);
                                                    }
                                                    else{
                                                        Toast.makeText(CreateAccount.this, "This Email Id is already registered",Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    Log.e("error",databaseError.getDetails());
                                                }
                                            });



                                }
                            });
                }}

        });
        int x = customersArrayList.size();
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