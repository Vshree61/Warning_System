package com.example.warningsystem;
import java.util.*;
import android.location.Location;
public class Comparison{
	int speed = 40;
	int th_value = 2;
	Map<Float, String> map = new HashMap<Float, String>();
	public Comparison(){}
	public String getNearestPoint(double currLatitude,double currLongitude,double latLand, double lonLand
			double latJun, double lonJun,String nameLand, String nameJn){
				//distanceComparison
                Location startPoint = new Location("startPoint");
                startPoint.setLatitude(currLatitude);
                startPoint.setLongitude(currLongitude);
		// curr_speed = startPoint.getSpeed(); // in m/s
                Location endPoint1 = new Location("Location B");
                endPoint1.setLatitude(latLand);
                endPoint1.setLongitude(lonLand);
                Location endPoint2 = new Location("Location C");
                endPoint2.setLatitude(latJun);
                endPoint2.setLongitude(lonJun);
                float distance1 = startPoint.distanceTo(endPoint1);
                map.put(distance1, nameLand);
                float distance2 = startPoint.distanceTo(endPoint2);
                map.put(distance2, nameJn);
                float minDistance = Collections.min(map.keySet());
                String nearestLocation = map.get(minDistance);
				return nearestLocation;
			}
	
	public int compareSpeed(int safe){
		if (speed - safe <= th_value){
			return 1;
		}
		else {
			return 0;
		}
	}
	
}
