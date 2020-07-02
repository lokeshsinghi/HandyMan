package com.example.locale_lite;
//
//import androidx.appcompat.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.text.DecimalFormat;
import java.util.HashMap;

public class profileSP extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout review, call, message, directions;
    TextView Name;
    TextView Category;
    TextView nrate, avrate;
    ImageView DP;
    RatingBar ratingBar,avbar;
    FirebaseDatabase database;
    DatabaseReference ratingTbl;
    Button submit, cancel, request, pay;
    Double latSP,lngSP;
    int numRate, totalRate;
    float avRate;

    String userid;
    FirebaseUser fuser;
    DatabaseReference databaseReference;
    EditText date,time,jobdes;
    Button send;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_sp);
        database = FirebaseDatabase.getInstance();
        review = findViewById(R.id.addreview);
        call = findViewById(R.id.call);
        Name = findViewById(R.id.name);
        Category = findViewById(R.id.category);
        DP = findViewById(R.id.dp);
        message = findViewById(R.id.message);
        directions = findViewById(R.id.directions);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        request = findViewById(R.id.request);
        nrate = findViewById(R.id.nrate);
        avrate = findViewById(R.id.avRate);
        avbar = findViewById(R.id.avBar);
        pay = findViewById(R.id.pay);
        pay.setOnClickListener(this);
        final String name = getIntent().getStringExtra("name");
        final String category = getIntent().getStringExtra("category");
        final String dpURL = getIntent().getStringExtra("dp");
        final String phone = getIntent().getStringExtra("phone");
        numRate = getIntent().getIntExtra("numRate",0);
        avRate = getIntent().getFloatExtra("avRate",0);
        totalRate = getIntent().getIntExtra("totalRate",0);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        userid = getIntent().getStringExtra("userid");
        final String currentid = fuser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Requests");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    RequestBox requestBox = snapshot.getValue(RequestBox.class);
                    if(currentid.equals(requestBox.getSender()) && userid.equals(requestBox.getReceiver()) &&
                            (!requestBox.getStatus().equals("completed") || !requestBox.getStatus().equals("declined"))){
                        request.setText("Request Sent");
                        request.setClickable(false);
                    }
                    if(requestBox.getStatus().equals("completed") || requestBox.getStatus().equals("declined")){
                        request.setText("REQUEST SERVICE");
                        request.setClickable(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        Picasso.with(profileSP.this).load(dpURL).into(DP);



        Name.setText(name);
        Category.setText(category);
        review.setOnClickListener(this);
        call.setOnClickListener(this);
        message.setOnClickListener(this);
        directions.setOnClickListener(this);
        request.setOnClickListener(this);
        nrate.setText(""+numRate);
        avrate.setText(new DecimalFormat("#.#").format(avRate));
        avbar.setRating(avRate);

    }


    private void showRatingDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(profileSP.this);
        final View mView = getLayoutInflater().inflate(R.layout.rating_dialog, null);
        final RatingBar rbar = mView.findViewById(R.id.ratingBar);
        EditText comment = mView.findViewById(R.id.comment);
        Button submit = mView.findViewById(R.id.submit);
        Button cancel = mView.findViewById(R.id.cancel);
        final TextView Word = mView.findViewById(R.id.word);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userid = getIntent().getStringExtra("userid");
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("ServiceProviders").child(userid);
                numRate++;
                totalRate+=rbar.getRating();
                databaseReference.child("numrate").setValue(numRate);
                databaseReference.child("totalrate").setValue(totalRate);
                float av = (float)totalRate/(float)numRate;
                databaseReference.child("avrate").setValue(av);
                avRate = av;
                avrate.setText(new DecimalFormat("#.#").format(avRate));
                nrate.setText(""+numRate);
                avbar.setRating(avRate);
                dialog.cancel();

            }
        });

        rbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int V = (int) v;
                if (V == 1)
                    Word.setText("Very Bad");
                if (V == 2)
                    Word.setText("Not Good");
                if (V == 3)
                    Word.setText("Quite Ok");
                if (V==4)
                    Word.setText("Very Good");
                if(V==5)
                    Word.setText("Excellent");
            }
        });
    }


    private void showRequestDialog()
    {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(profileSP.this);
        final View mView = getLayoutInflater().inflate(R.layout.request_service, null);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();


        date = mView.findViewById(R.id.date_text);
        time = mView.findViewById(R.id.time_text);
        jobdes = mView.findViewById(R.id.job_text);
        send = mView.findViewById(R.id.send_service_request_btn);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String datetext = date.getText().toString();
                String timetext = time.getText().toString();
                String destext = jobdes.getText().toString();
                String status = "pending";
                userid = getIntent().getStringExtra("userid");
                fuser = FirebaseAuth.getInstance().getCurrentUser();
                if(!datetext.equals("") && !timetext.equals("") && !destext.equals("")){
                    Toast.makeText(profileSP.this,"Request Sent",Toast.LENGTH_SHORT).show();
                    sendMessage(fuser.getUid(),userid,datetext,timetext,destext,status);
                    String sent = "Request Sent";
                    //request.setText(sent);
                    request.setBackgroundColor(Color.GRAY);
                    //request.setClickable(false);
                    dialog.hide();
                }
                else{
                    Toast.makeText(profileSP.this,"Fill out all the fields",Toast.LENGTH_SHORT).show();
                }
            }
        });


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(profileSP.this,
                        AlertDialog.THEME_HOLO_DARK,
                        onDateSetListener,
                        year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int min = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(profileSP.this,
                        AlertDialog.THEME_HOLO_DARK,
                        onTimeSetListener,
                        hour,min,false);
                timePickerDialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String datetext = dayOfMonth + "/" + month + "/" + year;
                date.setText(datetext);
            }
        };

        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String a;
                String timetext;
                if(hourOfDay > 12){
                    hourOfDay = hourOfDay - 12;
                    a = "PM";
                }else
                    a = "AM";
                if(minute<10){
                timetext = hourOfDay + ":" + "0"+ minute + " " + a;
                    time.setText(timetext);
                }else{
                    timetext= hourOfDay + ":" + minute + " " + a;
                    time.setText(timetext);
                }

            }
        };

    }

    private void showPayDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(profileSP.this);
        final View mView = getLayoutInflater().inflate(R.layout.payment_dialog, null);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        LinearLayout paytm = mView.findViewById(R.id.paytm);
        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://www.paytm.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        LinearLayout cash = mView.findViewById(R.id.cash);
        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(profileSP.this, "Cash mode selected", Toast.LENGTH_LONG).show();
                dialog.cancel();
            }
        });
    }




    @Override
    public void onClick(View view) {
        if (view == call) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + getIntent().getStringExtra("phone")));
            startActivity(intent);
        }
        if(view == review)
        {
            showRatingDialog();
        }
        if(view == directions)
        {   Bundle b = getIntent().getExtras();
            Intent intent = new Intent(profileSP.this, MapDirections.class);
            intent.putExtras(b);
            startActivity(intent);
        }
        if(view == message)
        {
            Intent intent = new Intent(profileSP.this,Chat.class);
            intent.putExtra("userid",getIntent().getStringExtra("userid"));
            intent.putExtra("type","ServiceProvider");
            startActivity(intent);
        }
        if(view == submit)
        {

        }
        if(view == request)
        {
            showRequestDialog();
        }
        if(view == pay)
        {
            showPayDialog();
        }

    }

    public void sendMessage(String sender, String receiver, String datetext, String timetext, String destext, String status){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Requests").push();
        String key = reference.getKey();
        HashMap<String, Object> hashmap = new HashMap<>();
        hashmap.put("sender",sender);
        hashmap.put("receiver",receiver);
        hashmap.put("date",datetext);
        hashmap.put("time",timetext);
        hashmap.put("description",destext);
        hashmap.put("status",status);
        hashmap.put("id",key);

        reference.setValue(hashmap);

        final DatabaseReference dataref = FirebaseDatabase.getInstance().getReference("Requestlist")
                .child(fuser.getUid())
                .child(userid);

        dataref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    dataref.child("id").setValue(userid);
                    dataref.child("status").setValue("pending");
                }
                if(dataSnapshot.exists()){
                    dataref.child("status").setValue("pending");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("Requestlist")
                .child(userid).child(fuser.getUid());
        chatRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    chatRef1.child("id").setValue(fuser.getUid());
                    chatRef1.child("status").setValue("pending");
                }
                if(dataSnapshot.exists()){
                    chatRef1.child("status").setValue("pending");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}