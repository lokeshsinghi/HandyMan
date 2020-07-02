package com.example.locale_lite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class asklocationSP extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMyLocationButtonClickListener {

    Location currentLocation;
    EditText nickaddress;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private GoogleMap mMap;
    private Marker markerCenter;
    Button savebutton;
    LatLng position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asklocation_sp);
        nickaddress = findViewById(R.id.nickname_other);
        savebutton = findViewById(R.id.save_button);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();


    }



    private void fetchLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(asklocationSP.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(mMap.getCameraPosition().target);
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        markerCenter = mMap.addMarker(markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        position = markerCenter.getPosition();

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            public void onCameraMove() {
                markerCenter.setPosition(mMap.getCameraPosition().target);
                position = markerCenter.getPosition();
            }
        });
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = getIntent().getExtras();
                String firstname = bundle.getString("firstname");
                String lastname = bundle.getString("lastname");
                String emailid = bundle.getString("emailid");
                String phonenum = bundle.getString("phonenum");
                String city = bundle.getString("city");
                String category = bundle.getString("category");
                String downloadUrlID = bundle.getString("downloadUrlID");
                String downloadUrlPP = bundle.getString("downloadUrlPP");
                String qualifications = bundle.getString("qualifications");
                Float experience = bundle.getFloat("yearsOfExperience");


                ServiceProviders sp = new ServiceProviders(firstname, lastname, emailid, phonenum, city, category);
                sp.setIdProofUrl(downloadUrlID);
                sp.setProfilePicUrl(downloadUrlPP);
                sp.setQualifications(qualifications);
                sp.setYearsOfExperience(experience);
                sp.setLatitude(position.latitude);
                sp.setLongitude(position.longitude);
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String userid = firebaseUser.getUid();
                sp.setId(userid);
                sp.setPending(false);

                FirebaseDatabase.getInstance().getReference("ServiceProviders")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(sp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(asklocationSP.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(asklocationSP.this, Pending.class);
                            startActivity(intent);
                        }
                    }
                });

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
        }
    }



    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
}
