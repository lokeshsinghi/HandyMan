package com.example.locale_lite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Arrays;

public class profileSP extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout review, call, message, directions;
    TextView Name;
    TextView Category;
    TextView nrate, avrate;
    ImageView DP;
    RatingBar ratingBar,avbar;
    FirebaseDatabase database;
    DatabaseReference ratingTbl;
    Button submit, cancel, request;
    Double latSP,lngSP;
    int numRate, totalRate;
    float avRate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_sp);
        database = FirebaseDatabase.getInstance();
        ratingTbl = database.getReference("Rating");
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
        avrate.setText(""+avRate);
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
                  avrate.setText(""+avRate);
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
//        if(view == request)
//        {
//            showRequestDialog();
//        }

    }

}
