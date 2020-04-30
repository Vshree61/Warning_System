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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LocationActivity extends AppCompatActivity {

    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    TextView latTextView, lonTextView, nearbyView;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    Double currLatitude,currLongitude;
    Long safeSpeed;
    DatabaseReference reff;
    String nearestLocation;
    Map<Float, String> map = new HashMap<Float, String>();
    Map<String, Long> speedMap = new HashMap<String, Long>();
    AlertDialog.Builder builder;
    LayoutInflater inflater;
    View dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        latTextView = findViewById(R.id.latTextView);
        lonTextView = findViewById(R.id.lonTextView);
        nearbyView = findViewById(R.id.nearbyLoc);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        reff = FirebaseDatabase.getInstance().getReference();
        builder = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();
        //reffJn = FirebaseDatabase.getInstance().getReference().child("Junction").child("1");
        getLastLocation();
        getNearby();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            fAuth.signOut();
            startActivity(new Intent(LocationActivity.this, Login.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
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
                                    latTextView.setText(location.getLatitude() + "");
                                    lonTextView.setText(location.getLongitude() + "");
                                    currLatitude = Double.parseDouble(latTextView.getText().toString());
                                    currLongitude = Double.parseDouble(lonTextView.getText().toString());

                                }
                            }
                        }
                );
                //getNearby();
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
    private void requestNewLocationData() {

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
            latTextView.setText(mLastLocation.getLatitude() + "");
            lonTextView.setText(mLastLocation.getLongitude() + "");
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
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

    public void getNearby() {
        //reffLand
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot landName = dataSnapshot.child("Landmark").child("1");
                DataSnapshot junctionName = dataSnapshot.child("Junction").child("1");
                //Landmark Data
                String name = (String) landName.child("landmark").getValue();
                Double lonLand = (Double) landName.child("longitude").getValue();
                Double latLand = (Double) landName.child("latitude").getValue();
                Long speedLand = (Long) landName.child("speed").getValue();
                //Junction Data
                String nameJn = (String) junctionName.child("junction").getValue();
                Double lonJun = (Double) junctionName.child("longitude").getValue();
                Double latJun = (Double) junctionName.child("latitude").getValue();
                Long speedJn = (Long) junctionName.child("speed").getValue();
                //distanceComparison
                Location startPoint = new Location("startPoint");
                startPoint.setLatitude(currLatitude);
                startPoint.setLongitude(currLongitude);
                Location endPoint1 = new Location("Location B");
                endPoint1.setLatitude(latLand);
                endPoint1.setLongitude(lonLand);
                Location endPoint2 = new Location("Location C");
                endPoint2.setLatitude(latJun);
                endPoint2.setLongitude(lonJun);
                float distance1 = startPoint.distanceTo(endPoint1);
                map.put(distance1, name);
                speedMap.put(name,speedLand);
                float distance2 = startPoint.distanceTo(endPoint2);
                map.put(distance2, nameJn);
                speedMap.put(nameJn,speedJn);
                float minDistance = Collections.min(map.keySet());
                nearestLocation = map.get(minDistance);
                safeSpeed = speedMap.get(nearestLocation);
                nearbyView.setText(nearestLocation);
                dialog = inflater.inflate(R.layout.alert_dialog,null);
                builder.setMessage(nearestLocation+" is approaching! Your current speed is 45 km/hr.Lower it down to " +
                        safeSpeed+" km/hr")
                .setTitle("Alert").setView(dialog).setIcon(R.drawable.ic_warning_black_24dp);
                AlertDialog alert = builder.create();
                alert.show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reff.addValueEventListener(listener);

    }



}

