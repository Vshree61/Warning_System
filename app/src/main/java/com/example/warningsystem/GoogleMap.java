// Author : Sayari
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
	public void getNearbyLandmark(double currLatitude,double currLongitude){
		float distance = Float.MAX_VALUE;
		reff = FirebaseDatabase.getInstance().getReference();
		listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				for( DataSnapshot landName : dataSnapshot.child("Landmark").getChildren()){
                //Landmark Data
                String name = (String) landName.child("landmark").getValue();
                Double lonLand = (Double) landName.child("longitude").getValue();
                Double latLand = (Double) landName.child("latitude").getValue();
                Long speed = (Long) landName.child("speed").getValue();
				//distance caluculate
				Location startPoint = new Location("startPoint");
                startPoint.setLatitude(currLatitude);
                startPoint.setLongitude(currLongitude);
				Location endPoint = new Location("Location B");
                endPoint.setLatitude(latLand);
                endPoint.setLongitude(lonLand); 
				float distance_landmark = startPoint.distanceTo(endPoint);
				if( distance_landmark < distance && distance_landmark <= 500.0){
					distance = distance_landmark;
					nameLand = name;
					speedLand = speed;
					lat = latLand;
					lon = lonLand;
					}
				
				}
			}
			@Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reff.addValueEventListener(listener);

	}	
}
