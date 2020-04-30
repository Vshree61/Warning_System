package com.example.warningsystem;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BusyJunctionManager {
    Long junction_speed;
    String nameJn;
    Double lonJun;
    Double latJun;
    DatabaseReference reff;

    public BusyJunctionManager() {
    }

    public Long getJunction_speed() {
        return junction_speed;
    }

    public void setJunction_speed(Long junction_speed) {
        this.junction_speed = junction_speed;
    }

    public String getNameJn() {
        return nameJn;
    }

    public void setNameJn(String nameJn) {
        this.nameJn = nameJn;
    }

    public Double getLonJun() {
        return lonJun;
    }

    public void setLonJun(Double lonJun) {
        this.lonJun = lonJun;
    }

    public Double getLatJun() {
        return latJun;
    }

    public void setLatJun(Double latJun) {
        this.latJun = latJun;
    }

    public Long getNearbyJunction(){
        final Long[] speed = new Long[1];
        reff = FirebaseDatabase.getInstance().getReference();
        ValueEventListener listener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                DataSnapshot junctionName = dataSnapshot.child("Junction").child("1");
                String name =(String) junctionName.child("junction").getValue();
                Double lon= (Double) junctionName.child("longitude").getValue();
                Double lat= (Double) junctionName.child("latitude").getValue();
                speed[0] = (Long) junctionName.child("speed").getValue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reff.addValueEventListener(listener);
        return speed[0];
    }

}
