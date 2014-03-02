package com.example.mindyourtravel;
import java.io.IOException;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class TravelPlanActivity extends Activity {

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
        	EditText curLocationView =(EditText) findViewById(R.id.txtCurrentLoc);
    		curLocationView.setText(gps.getCurrentLocation(this));
        		
       	}else
       	{
    		// can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
       	}
        final EditText timebox= (EditText)findViewById(R.id.txtStartTime);
        Calendar cal= Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        String showTime=String.format("%1$tI:%1$tM %1$Tp",cal);
        timebox.setText(showTime);
        try
		{
		
	        JSONObject reqParameters= new JSONObject();
			reqParameters.put("DATATYPE", "TRAVELMODE");
			JsonHandler jsonHandler =JsonHandler.getInstance();
			String url=jsonHandler.getFullUrl("DataAdapterFactory.php");
			JSONObject result = jsonHandler.PostJsonDataToServer(url, reqParameters);
			String resultCode= result.getString("RESULT");
			if(resultCode.contentEquals(AppConstant.PHPResponse_KO))
			{
				String errorCode=result.getString("ERRORCODE");
				if(errorCode.contentEquals(AppConstant.PHP_ERROR_CODE.NOTEXISTS))
				{
					SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorUserNotExist);
				}
				else if(errorCode.contentEquals(AppConstant.PHP_ERROR_CODE.TECHNICAL))
				{
					SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
				}
			}
			else
			{
				JSONArray jsonData =result.getJSONArray("DATAARRAY");
				String[] travelType =new String[jsonData.length()];
				for (int i=0;i<jsonData.length();i++ ) 
				{
					JSONObject row= jsonData.getJSONObject(i);
					travelType[i] = row.getString("TYPE");
				}
				
				
				ArrayAdapter<String> adapterTravel = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, travelType);
				adapterTravel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				final Spinner ddTravelType=(Spinner)findViewById(R.id.ddTravelType);
				ddTravelType.setAdapter(adapterTravel);
			}
		}
        catch (IOException e) 
		{    
			SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
		} 
		catch(JSONException ex)
		{
			SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.travel_plan, menu);
		return true;
	}
	
	private void SetErrorLabelVisibility(int visibility,int errorResId)
	{
		TextView lblError =(TextView)findViewById(R.id.lblTravelErrorMsg);
		if(lblError != null)
		{
			lblError.setVisibility(visibility);
			lblError.setText(errorResId);
		}
	}

}
