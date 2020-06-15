package com.example.warningsystem;
import java.util.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BusyJunctionManager{
	public String nameJn;
	public Long speedJn;
	public double lat;
	public double lon;
	DatabaseReference reff;
	ValueEventListener listener;
	public void getNearbyJunction(double currLatitude, double currLongitude){
		float distance = Float.MAX_VALUE;
		reff = FirebaseDatabase.getInstance().getReference();
		listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				for( DataSnapshot JnName : dataSnapshot.child("Junction").getChildren()){
                //Junction Data
                String name = (String) JnName.child("junction").getValue();
                Double lonJn = (Double) JnName.child("longitude").getValue();
                Double latJn = (Double) JnName.child("latitude").getValue();
                Long speed = (Long) JnName.child("speed").getValue();
				Location startPoint = new Location("startPoint");
                startPoint.setLatitude(currLatitude);
                startPoint.setLongitude(currLongitude);
				Location endPoint = new Location("Location B");
                endPoint.setLatitude(latJn);
                endPoint.setLongitude(lonJn);
				float distance_Jn = startPoint.distanceTo(endPoint);
				if(distance_Jn < distance && distance_Jn <= 500.0){
					distance = distance_Jn;
					nameJn = name;
					speedJn = speed;
					lat = latJn;
					lon = lonJn;
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
