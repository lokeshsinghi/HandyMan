package com.example.locale_lite;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
//import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.locale_lite.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.Marker;

import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DirectionsSp extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMyLocationButtonClickListener {
    private GoogleMap mMap;
    Button getDirection;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions_sp);
        getDirection = findViewById(R.id.btnGetDirection);
        Bundle b = getIntent().getExtras();
        final double cusLat = b.getDouble("cusLat");
        final double cusLng = b.getDouble("cusLng");
        final LatLng cusLocation = new LatLng(cusLat,cusLng);
        getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+cusLat + "," + cusLng);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        fetchLastLocation();

    }
    private void fetchLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(DirectionsSp.this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        Bundle b = getIntent().getExtras();
        final double cusLat = b.getDouble("cusLat");
        final double cusLng = b.getDouble("cusLng");

        double spLat = sp_homepage.spLat;
        double spLng = sp_homepage.spLng;

        LatLng spLocation = new LatLng(spLat,spLng);
        LatLng cusLocation = new LatLng(cusLat,cusLng);
        mMap = googleMap;
        Log.d("mylog", "Added Markers");
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(cusLocation)
                .title("You need to reach here");
        googleMap.addMarker(markerOptions).showInfoWindow();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cusLocation, 16));

        MarkerOptions markerOptions1 = new MarkerOptions();
        markerOptions1.position(spLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        googleMap.addMarker(markerOptions1);






        googleMap.addMarker(markerOptions);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
        }}


    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }
}
