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
	public void getNearbyJunction(){
		reff = FirebaseDatabase.getInstance().getReference();
		listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot landName = dataSnapshot.child("Junction").child("1");
                //Landmark Data
                String name = (String) landName.child("junction").getValue();
                Double lonJn = (Double) landName.child("longitude").getValue();
                Double latJn = (Double) landName.child("latitude").getValue();
                Long speed = (Long) landName.child("speed").getValue();
				nameJn = name;
				speedJn = speed;
				lat = latJn;
				lon = lonJn;
			}
		@Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reff.addValueEventListener(listener);

	}
}