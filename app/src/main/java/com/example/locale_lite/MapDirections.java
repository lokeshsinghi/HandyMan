package com.example.locale_lite;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapDirections extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMyLocationButtonClickListener {
    private GoogleMap mMap;
    Button getDirection;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_directions);
           getDirection = findViewById(R.id.btnGetDirection);
        Bundle b = getIntent().getExtras();
        final double spLat = b.getDouble("spLat");
        final double spLng = b.getDouble("spLng");
        getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+spLat + "," + spLng);
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
        mapFragment.getMapAsync(MapDirections.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Bundle b = getIntent().getExtras();
        final double spLat = b.getDouble("spLat");
        final double spLng = b.getDouble("spLng");

        double cusLat = Main2Activity.cusLat;
        double cusLng = Main2Activity.cusLng;

        LatLng spLocation = new LatLng(spLat,spLng);
        LatLng cusLocation = new LatLng(cusLat,cusLng);
        mMap = googleMap;
        Log.d("mylog", "Added Markers");
        MarkerOptions markerOptions1 = new MarkerOptions();
        markerOptions1.position(spLocation)
                .title("Service Provier will reach here");
        googleMap.addMarker(markerOptions1).showInfoWindow();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(spLocation, 16));

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(cusLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
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
