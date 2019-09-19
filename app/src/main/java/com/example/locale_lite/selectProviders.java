package com.example.locale_lite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;

public class selectProviders extends AppCompatActivity {

    TextView heading;
    private RecyclerView recyclerView;
    private ListAdapter useradapter;
    private List<ServiceProviders> musers;
    ProgressBar pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_providers);

        pbar = findViewById(R.id.progressBar);
        heading = findViewById(R.id.service_text);
        final String check = getIntent().getStringExtra("buttontext");
        String display = check + " in your city";
        heading.setText(display);
        recyclerView = findViewById(R.id.service_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        musers = new ArrayList<>();
        readUsers();

    }

    public void readUsers(){
        final String check = getIntent().getStringExtra("buttontext");
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ServiceProviders");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                musers.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ServiceProviders serviceProviders = snapshot.getValue(ServiceProviders.class);

                    assert serviceProviders!=null;
                    assert firebaseUser!=null;

                    if(serviceProviders.getCategory().equalsIgnoreCase(check)){
                        musers.add(serviceProviders);
                    }
                    pbar.setVisibility(GONE);
                }
                useradapter = new ListAdapter(selectProviders.this,musers);
                recyclerView.setAdapter(useradapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}