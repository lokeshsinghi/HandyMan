package com.example.locale_lite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity{

    FirebaseUser firebaseUser;
    String userid;
    LinearLayout mainact1,mainact2;

Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.startPager);
        ImageAdapter adapter = new ImageAdapter(this);
        viewPager.setAdapter(adapter);
        mainact1=findViewById(R.id.mainact1);
        mainact2=findViewById(R.id.mainact2);



        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            mainact1.setVisibility(View.INVISIBLE);
            mainact2.setVisibility(View.VISIBLE);
            userid = firebaseUser.getUid();
            DatabaseReference database = FirebaseDatabase.getInstance().getReference("Customers");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int count=0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Customers c = snapshot.getValue(Customers.class);
                        if (c.getId().equals(userid)) {
                            count=1;
                        }
                    }
                    if(count==1)
                    {
                        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                        {
                            DatabaseReference database1 = FirebaseDatabase.getInstance().getReference("ServiceProviders");
                            database1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    int cont =0;
                                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                        ServiceProviders sp = dataSnapshot1.getValue(ServiceProviders.class);
                                        if(sp.getId().equals(userid) && sp.getPending().equals(true)){
                                            cont=1;
                                            break;
                                        }
                                    }
                                    if(cont==1)
                                    {
                                        Toast.makeText(MainActivity.this,"Logged in",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, sp_homepage.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(MainActivity.this,"Not verified yet",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, Pending.class);
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


        }

        Button createNew = (Button) findViewById(R.id.createnew);
        createNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateAccount.class);
                startActivity(intent);
            }
        });
        Button logIn = (Button) findViewById(R.id.login);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2 = new Intent(MainActivity.this, Login.class);
                startActivity(i2);
            }
        });
    }
    @Override
    public void onBackPressed() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you want to exit ?");
        builder.setCancelable(true);
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                moveTaskToBack(true);
            }
        });
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();
            }

        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



}
