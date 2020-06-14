package com.example.warningsystem;
import java.util.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GoogleMap{
	public String nameLand;
	public Long speedLand;
	public double lat;
	public double lon;
	DatabaseReference reff;
	ValueEventListener listener;
	public void getNearbyLandmark(){
		reff = FirebaseDatabase.getInstance().getReference();
		listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot landName = dataSnapshot.child("Landmark").child("1");
                //Landmark Data
                String name = (String) landName.child("landmark").getValue();
                Double lonLand = (Double) landName.child("longitude").getValue();
                Double latLand = (Double) landName.child("latitude").getValue();
                Long speed = (Long) landName.child("speed").getValue();
				nameLand = name;
				speedLand = speed;
				lat = latLand;
				lon = lonLand;
			}
			@Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reff.addValueEventListener(listener);

	}	
}