package com.example.warningsystem;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GoogleMapAPI {
    Long landmark_speed;
    DatabaseReference reff;
    public Long getNearbyLandMark(){
        reff = FirebaseDatabase.getInstance().getReference();
        ValueEventListener listener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                DataSnapshot landName = dataSnapshot.child("Landmark").child("1");
                String name = (String) landName.child("landmark").getValue();
                Double lonLand = (Double) landName.child("longitude").getValue();
                Double latLand = (Double) landName.child("latitude").getValue();
                Long speedLand = (Long) landName.child("speed").getValue();
                landmark_speed = (Long) landName.child("speed").getValue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reff.addValueEventListener(listener);
        return landmark_speed;
    }
}
