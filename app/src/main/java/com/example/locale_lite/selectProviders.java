package com.example.locale_lite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;

public class selectProviders extends AppCompatActivity {

    ListView listView;
    FirebaseListAdapter adapter;
    String Name, ProfilePic, Phone;
    ProgressBar pbar;
    TextView heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_providers);

        listView = findViewById(R.id.service_list);
        heading = findViewById(R.id.service_text);
        pbar = findViewById(R.id.progressBar);
        final String check = getIntent().getStringExtra("buttontext");
        String display = check + " in your city";
        heading.setText(display);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ServiceProviders");
        Query query = databaseReference.orderByChild("category").equalTo(check);


        FirebaseListOptions<ServiceProviders> options = new FirebaseListOptions.Builder<ServiceProviders>()
                .setLayout(R.layout.list_row)
                .setQuery(query,ServiceProviders.class)
                .build();
        adapter = new FirebaseListAdapter(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Object model, int position) {
                TextView name = v.findViewById(R.id.name_list);
                TextView info = v.findViewById(R.id.info_list);
                ImageView dp = v.findViewById(R.id.providers_image);

                ServiceProviders sp = (ServiceProviders) model;

                    name.setText(sp.getFirstname() + " " + sp.getLastname());
                    Name = sp.getFirstname() + " " + sp.getLastname();
                    info.setText(sp.getPhonenum());
                    Picasso.with(selectProviders.this).load(sp.getProfilePicUrl()).into(dp);
                    ProfilePic = sp.getProfilePicUrl();
                    Phone = "+91"+sp.getPhonenum();
                pbar.setVisibility(GONE);

//                else{
//                    name.setVisibility(GONE);
//                    info.setVisibility(GONE);
//                    dp.setVisibility(GONE);
//                }
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                Intent intent = new Intent(selectProviders.this, profileSP.class);
                intent.putExtra("name",Name);
                intent.putExtra("dp",ProfilePic);
                intent.putExtra("category",check);
                intent.putExtra("phone",Phone);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}