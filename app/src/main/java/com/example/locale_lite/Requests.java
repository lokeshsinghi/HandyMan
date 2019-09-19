package com.example.locale_lite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class Requests extends AppCompatActivity {

    TextView heading;
    private RecyclerView recyclerView;
    ProgressBar pbar;
    private ListAdapter listAdapter;
    private List<ServiceProviders> musers;

    FirebaseUser fuser;
    DatabaseReference database;
    private List<RequestList> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        pbar = findViewById(R.id.progressBar);
        heading = findViewById(R.id.request_text);
        final String check = getIntent().getStringExtra("buttontext");
        heading.setText(check);
        recyclerView = findViewById(R.id.request_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        userList = new ArrayList<>();

        database = FirebaseDatabase.getInstance().getReference("Requestlist").child(fuser.getUid());
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RequestList chatlist = snapshot.getValue(RequestList.class);
                    userList.add(chatlist);
                }
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void chatList() {
        musers = new ArrayList<>();
        database = FirebaseDatabase.getInstance().getReference("ServiceProviders");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                musers.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ServiceProviders c = dataSnapshot1.getValue(ServiceProviders.class);
                    for (RequestList chatlist : userList) {
                        if (c.getId().equals(chatlist.getId())) {
                            musers.add(c);
                        }
                    }
                    pbar.setVisibility(GONE);
                }
                listAdapter = new ListAdapter(Requests.this, musers);
                recyclerView.setAdapter(listAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
