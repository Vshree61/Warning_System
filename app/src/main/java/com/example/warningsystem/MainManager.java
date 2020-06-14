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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MainManager extends AppCompatActivity {

    Button btnStartTracking;
	Double currLatitude,currLongitude;
	Comparison c = new Comparison();
	Map<String, Long> speedMap = new HashMap<String, Long>();
	AlertDialog.Builder builder;
    LayoutInflater inflater;
	View dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_tracking);
		builder = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();
        btnStartTracking = findViewById(R.id.btn_start_tracking);
		currLatitude = Double.parseDouble(getIntent().getStringExtra("LAT"));
        currLongitude = Double.parseDouble(getIntent().getStringExtra("LON"));
        btnStartTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				checkVersion();
				query(currLatitude, currLongitude);
            }
        });

    }
	public void query(double currLatitude, currLongitude){
		//fetching Landmarks
		GoogleMap land = new GoogleMap();
		land.getNearbyLandmark();
		double landLat = land.lat;
		double landLon =  land.lon;
		String nameLand = land.nameLand;
		Long speedLand = land.speedLand;
		speedMap.put(nameLand,speedLand);
		//fetching busy junction
		BusyJunctionManager junc = new BusyJunctionManager();
		junc.getNearbyJunction();
		double latJun = junc.lat;
		double lonJun = junc.lon;
		String nameJun = junc.nameJn;
		Long speedJun = junc.speedJn;
		speedMap.put(nameJun,speedJun);
		getNearby(landLat,landLon,latJun,lonJun,nameLand,nameJun);
		
	}
	public void getNearby(double landLat,double landLon, double latJun,double lonJun,String nameLand,String nameJun){
		String nearestLocation = c.getNearestPoint(landLat,landLon,latJun,lonJun,nameLand,nameJun);
		int safe_speed = speedMap.get(nearestLocation);
		compare(safe_speed,nearestLocation);
		
	}
	public void compare(int safe_speed,String nearestLocation){
		int result = c.compareSpeed(safe_speed);
		if(result == 1){
			dialog = inflater.inflate(R.layout.alert_dialog,null);
                builder.setMessage(nearestLocation+" is approaching! Your current speed is 40 km/hr.Lower it down to " +
                        safe_speed+" km/hr")
                        .setTitle("Alert").setView(dialog).setIcon(R.drawable.ic_warning_black_24dp);
            AlertDialog alert = builder.create();
            alert.show();
		}
			
	}
}