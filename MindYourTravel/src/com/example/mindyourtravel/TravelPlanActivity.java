package com.example.mindyourtravel;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;


public class TravelPlanActivity extends Activity  {

	GPSTracker gps;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_travel_plan);
		
		// create class object
        gps = new GPSTracker(this);
     // check if GPS enabled     
        if(gps.canGetLocation())
        {
        	TextView curLocationView =(TextView) findViewById(R.id.myCurrentLoc);
    		curLocationView.setText(gps.getCurrentLocation(this));
        		
       	}else
       	{
    		// can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
       	}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.travel_plan, menu);
		return true;
	}

}
