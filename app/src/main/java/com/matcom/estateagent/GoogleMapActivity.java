package com.matcom.estateagent;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import android.location.Address;
import android.location.Geocoder;


public class GoogleMapActivity extends Activity {
	
	 private GoogleMap map;
	 LatLng cursel;
	 LatLng cursel2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_google_map);
		// Get Intent Data
				Intent intent = getIntent();
				String addr = intent.getStringExtra("address");
				
				// Maps
				   
				map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				        .getMap();
				cursel=getLongLat(addr);
			    Marker meeting = map.addMarker(new MarkerOptions()
			    .position(cursel)
			        .title("Title"+" - "+addr));
			    
			   
			 	
			 	map.setMyLocationEnabled(true);

			    // Move the camera instantly to the RDV address.
			    map.moveCamera(CameraUpdateFactory.newLatLngZoom(cursel, 11
			    		));

			    // Zoom in, animating the camera.
			    map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
			  
				
	}
	
	private LatLng getLongLat(String addr) {
		
		
		 cursel = new LatLng(0,0);
		  double longt=0;
		  double lat=0;

		// Find Address
		Geocoder geoCoder =	new Geocoder(
				getBaseContext(), Locale.getDefault());
		try {
			List<Address> addresses = geoCoder.getFromLocationName
					(addr, 5);

			if (addresses.size() > 0) 
			{
				
					longt = addresses.get(0).getLongitude();
					lat = addresses.get(0).getLatitude();
					cursel = new LatLng(lat, longt);
					
			}
		}
			 
		
		catch (IOException e) {
			e.printStackTrace();
		}
		
	 	System.out.println("=== Get Long Lat  ======> "+addr+"  Adr "+longt+"  Adr "+lat);
	
		return cursel;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.google_map, menu);
		return true;
	}

}
