package com.example.warningsystem;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.warningsystem.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LocationActivity extends AppCompatActivity {

    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    TextView latTextView, lonTextView,nearbyView;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    Double currLatitude;
    Double currLongitude;
    DatabaseReference reffJn;
    DatabaseReference reffLand;
    Button mNearby;
    Map<Float,String> map = new HashMap<Float, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        latTextView = findViewById(R.id.latTextView);
        lonTextView = findViewById(R.id.lonTextView);
        nearbyView = findViewById(R.id.nearbyLoc);
        mNearby = findViewById(R.id.nearbyBtn);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        reffLand = FirebaseDatabase.getInstance().getReference().child("Landmark").child("1");
        reffJn = FirebaseDatabase.getInstance().getReference().child("Junction").child("1");
        getLastLocation();
        //getNearby();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.logout){
            fAuth.signOut();
            startActivity(new Intent(LocationActivity.this,Login.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    latTextView.setText(location.getLatitude()+"");
                                    lonTextView.setText(location.getLongitude()+"");
                                    currLatitude = Double.parseDouble(latTextView.getText().toString());
                                    currLongitude = Double.parseDouble(lonTextView.getText().toString());

                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latTextView.setText(mLastLocation.getLatitude()+"");
            lonTextView.setText(mLastLocation.getLongitude()+"");
            currLatitude = Double.parseDouble(latTextView.getText().toString());
            currLongitude = Double.parseDouble(lonTextView.getText().toString());

        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }
/*
    public void getNearby() {
        reffJn.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Junction junction = dataSnapshot.getValue(Junction.class);
                assert junction != null;
                String name = junction.getJunction();
                Double latitude = junction.getLatitude();
                Double longitude = junction.getLongitude();
                Location startPoint = new Location("Location A");
                startPoint.setLatitude(currLatitude);
                startPoint.setLongitude(currLongitude);
                Location endPoint = new Location("Location B");
                endPoint.setLatitude(latitude);
                endPoint.setLongitude(longitude);
                float distance = startPoint.distanceTo(endPoint);
                map.put(distance,name);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reffLand.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Landmark landmark = dataSnapshot.getValue(Landmark.class);
                assert landmark != null;
                String name = landmark.getLandmark();
                Double latitude = landmark.getLatitude();
                Double longitude = landmark.getLongitude();
                Location startPoint = new Location("startPoint");
                startPoint.setLatitude(currLatitude);
                startPoint.setLongitude(currLongitude);
                Location endPoint = new Location("endPoint");
                endPoint.setLatitude(latitude);
                endPoint.setLongitude(longitude);
                float distance = startPoint.distanceTo(endPoint);
                map.put(distance,name);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        float minDistance = Collections.min(map.keySet());
        String nearbyLocation = map.get(minDistance);
        nearbyView.setText(nearbyLocation);
    }
*/
}