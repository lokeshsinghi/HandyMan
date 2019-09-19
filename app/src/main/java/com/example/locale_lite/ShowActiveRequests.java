package com.example.locale_lite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowActiveRequests extends AppCompatActivity {

    CircleImageView dp;
    TextView name;
    private LinearLayout call, message, directions;
    TextView job,time,date,des;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String userid,requestid;
    List<RequestBox> mchat;
    Button complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_active_requests);

        name = findViewById(R.id.chat_name);
        job = findViewById(R.id.chat_job);
        dp = findViewById(R.id.chat_dp);

        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        des = findViewById(R.id.description);
        complete = findViewById(R.id.endtask);

        call = findViewById(R.id.call);
        message = findViewById(R.id.message);
        directions = findViewById(R.id.directions);

        userid = getIntent().getStringExtra("userid");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Customers").child(userid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Customers s = dataSnapshot.getValue(Customers.class);
                String mname = s.getFirstname() + " " + s.getLastname();
                name.setText(mname);
                job.setText(s.getPhonenum());
                dp.setImageResource(R.drawable.cuslogo);
                readMessage(firebaseUser.getUid(),userid);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + getIntent().getStringExtra("phone")));
                startActivity(intent);

            }});
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowActiveRequests.this,Chat.class);
                intent.putExtra("userid",getIntent().getStringExtra("userid"));
                intent.putExtra("type","Customer");
                startActivity(intent);

            }});
        directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = getIntent().getExtras();
                Intent intent = new Intent(ShowActiveRequests.this, DirectionsSp.class);
                intent.putExtras(b);
                startActivity(intent);

            }});

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("Requests");
                databaseReference.child(requestid)
                        .child("status").setValue("completed");

                databaseReference = FirebaseDatabase.getInstance().getReference("Requestlist");
                databaseReference.child(userid)
                        .child(firebaseUser.getUid()).child("status").setValue("completed");

                databaseReference = FirebaseDatabase.getInstance().getReference("Requestlist");
                databaseReference.child(firebaseUser.getUid())
                        .child(userid).child("status").setValue("completed");

                complete.setClickable(false);
                complete.setText("Completed");
                complete.setBackgroundColor(Color.GRAY);

                finish();
            }
        });

    }

    private void readMessage(final String myid, final String userid){
        mchat = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Requests");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    RequestBox requestBox = snapshot.getValue(RequestBox.class);
                    if(requestBox.getReceiver().equals(myid) && requestBox.getSender().equals(userid) ||
                            requestBox.getReceiver().equals(userid) && requestBox.getSender().equals(myid) &&
                                    requestBox.getStatus().equals("active")){
                        date.setText(requestBox.getDate());
                        time.setText(requestBox.getTime());
                        des.setText(requestBox.getDescription());
                        requestid = requestBox.getId();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}