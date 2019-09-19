package com.example.locale_lite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {

    CircleImageView dp;
    TextView name;
    TextView job;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    EditText message;
    ImageView send;
    MessageAdapter messageAdapter;
    List<ChatBox> mchat;
    RecyclerView recyclerView;
    String userid,type;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        dp = findViewById(R.id.chat_dp);
        name = findViewById(R.id.chat_name);
        job = findViewById(R.id.chat_job);
        message = findViewById(R.id.chat_message);
        send = findViewById(R.id.send_chat);

        recyclerView = findViewById(R.id.message_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        userid = getIntent().getStringExtra("userid");
        type = getIntent().getStringExtra("type");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(type.equals("Customer")){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Customers").child(userid);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Customers s = dataSnapshot.getValue(Customers.class);
                    String mname = s.getFirstname() + " " + s.getLastname();
                    name.setText(mname);
                    job.setText(s.getPhonenum());
                        dp.setImageResource(R.drawable.cuslogo);
                    readMessage(firebaseUser.getUid(),userid,null);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });}
        else {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("ServiceProviders").child(userid);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ServiceProviders s = dataSnapshot.getValue(ServiceProviders.class);
                        String mname = s.getFirstname() + " " + s.getLastname();
                        name.setText(mname);
                        job.setText(s.getCategory());
                        Picasso.with(Chat.this).load(s.getProfilePicUrl()).into(dp);
                    readMessage(firebaseUser.getUid(),userid,s.getProfilePicUrl());
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });}

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = message.getText().toString();
                if(!msg.equals("")){
                    sendMessage(firebaseUser.getUid(),userid,msg);
                }
                else{
                    Toast.makeText(Chat.this,"Type a message to send",Toast.LENGTH_SHORT).show();
                }
                message.setText("");
            }
        });



    }

    private void sendMessage(String sender, String receiver, String message){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashmap = new HashMap<>();
        hashmap.put("sender",sender);
        hashmap.put("receiver",receiver);
        hashmap.put("message",message);

        reference.child("Chats").push().setValue(hashmap);

        final DatabaseReference dataref = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(firebaseUser.getUid())
                .child(userid);

        dataref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()){
                        dataref.child("id").setValue(userid);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(receiver).child(firebaseUser.getUid());
        chatRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    chatRef1.child("id").setValue(firebaseUser.getUid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readMessage(final String myid, final String userid, final String imageurl){
        mchat = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatBox chatBox = snapshot.getValue(ChatBox.class);
                    if(chatBox.getReceiver().equals(myid) && chatBox.getSender().equals(userid) ||
                        chatBox.getReceiver().equals(userid) && chatBox.getSender().equals(myid)){
                        mchat.add(chatBox);
                    }
                    messageAdapter = new MessageAdapter(Chat.this,mchat,imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
