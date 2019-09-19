package com.example.locale_lite;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowSentRequest extends AppCompatActivity {

    CircleImageView dp;
    TextView name;
    TextView job;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    String userid,type;
    List<RequestBox> mchat;
    RequestAdapter requestAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_request);

        name = findViewById(R.id.chat_name);
        job = findViewById(R.id.chat_job);
        dp = findViewById(R.id.chat_dp);

        recyclerView = findViewById(R.id.message_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        userid = getIntent().getStringExtra("userid");
        type = getIntent().getStringExtra("type");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(type.equals("ServiceProvider")){
            databaseReference = FirebaseDatabase.getInstance().getReference().child("ServiceProviders").child(userid);}
        else {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Customers").child(userid);
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ServiceProviders s = dataSnapshot.getValue(ServiceProviders.class);
                String mname = s.getFirstname() + " " + s.getLastname();
                name.setText(mname);
                job.setText(s.getCategory());
                if(s.getProfilePicUrl()==null){
                    dp.setImageResource(R.drawable.cuslogo);
                }
                Picasso.with(ShowSentRequest.this).load(s.getProfilePicUrl()).into(dp);
                readMessage(firebaseUser.getUid(),userid,s.getProfilePicUrl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readMessage(final String myid, final String userid, final String imageurl){
        mchat = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Requests");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    RequestBox requestBox = snapshot.getValue(RequestBox.class);
                    if(requestBox.getReceiver().equals(myid) && requestBox.getSender().equals(userid) ||
                            requestBox.getReceiver().equals(userid) && requestBox.getSender().equals(myid)){
                        mchat.add(requestBox);
                    }
                    requestAdapter = new RequestAdapter(ShowSentRequest.this,mchat,imageurl);
                    recyclerView.setAdapter(requestAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
