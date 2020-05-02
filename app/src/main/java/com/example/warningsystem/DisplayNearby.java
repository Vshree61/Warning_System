package com.example.warningsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DisplayNearby extends AppCompatActivity {
    TextView nearbyView;
    DatabaseReference reff;
    String nearestLocation;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    Map<Float, String> map = new HashMap<Float, String>();
    Map<String, Long> speedMap = new HashMap<String, Long>();
    AlertDialog.Builder builder;
    LayoutInflater inflater;
    Long safeSpeed;
    View dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_screen2);
        nearbyView = findViewById(R.id.nearbyLoc);
        reff = FirebaseDatabase.getInstance().getReference();
        builder = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();
        Double currLatitude = Double.parseDouble(getIntent().getStringExtra("LAT"));
        Double currLongitude = Double.parseDouble(getIntent().getStringExtra("LON"));
        getNearby(currLatitude,currLongitude);

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
            startActivity(new Intent(DisplayNearby.this, LoginManager.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    public void getNearby(final Double currLatitude, final Double currLongitude) {
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
