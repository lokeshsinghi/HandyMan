package com.example.locale_lite;
//
//import androidx.appcompat.app.AlertDialog;
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
import android.widget.TextView;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.DatePicker;
import android.widget.TimePicker;
import java.util.Calendar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import java.text.DecimalFormat;

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
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://www.paytm.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        final String name = getIntent().getStringExtra("name");
        final String category = getIntent().getStringExtra("category");
        final String dpURL = getIntent().getStringExtra("dp");
        final String phone = getIntent().getStringExtra("phone");
        numRate = getIntent().getIntExtra("numRate",0);
        avRate = getIntent().getFloatExtra("avRate",0);
        totalRate = getIntent().getIntExtra("totalRate",0);






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
                final String userid = getIntent().getStringExtra("userid");
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
                if(hourOfDay > 12){
                    hourOfDay = hourOfDay - 12;
                    a = "PM";
                }else
                    a = "AM";
                String timetext = hourOfDay + ":" + minute + " " + a;
                time.setText(timetext);
            }
        };

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
            startActivity(intent);
        }
        if(view == submit)
        {

        }
        if(view == request)
        {
            showRequestDialog();
        }

    }

}