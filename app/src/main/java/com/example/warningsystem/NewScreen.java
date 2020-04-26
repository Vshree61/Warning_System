package com.example.warningsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.EmptyStackException;

public class NewScreen extends AppCompatActivity {
    EditText junctionName;
    EditText mLongitude;
    EditText mLatitude;
    EditText speedVal;
    Button mSaveBtn;
    Junction junction;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    DatabaseReference reff;
    long maxId = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_screen);
        junctionName = findViewById(R.id.name);
        mLongitude= findViewById(R.id.longitude);
        mLatitude = findViewById(R.id.latitude);
        speedVal = findViewById(R.id.speed);
        mSaveBtn = findViewById(R.id.saveBtn);
        junction = new Junction();
        reff = FirebaseDatabase.getInstance().getReference().child("Junction");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    maxId = (dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jName = junctionName.getText().toString().trim();
                Double longit = Double.parseDouble(mLongitude.getText().toString().trim());
                Double lati = Double.parseDouble(mLatitude.getText().toString().trim());
                Integer sp = Integer.parseInt(speedVal.getText().toString().trim());
                junction.setJunction(jName);
                junction.setLongitude(longit);
                junction.setLatitude(lati);
                junction.setSpeed(sp);
                reff.child(String.valueOf(maxId+1)).setValue(junction);
                Toast.makeText(NewScreen.this, "Upload Successful", Toast.LENGTH_SHORT).show();
            }
        });

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
            startActivity(new Intent(NewScreen.this,Login.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    /*
    reff = FirebaseDatabase.getInstance().getReference().child("Junction");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot child:children){
                    Junction value = child.getValue(Junction.class);
                    Toast.makeText(Login.this, "Change in data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
     */

}

