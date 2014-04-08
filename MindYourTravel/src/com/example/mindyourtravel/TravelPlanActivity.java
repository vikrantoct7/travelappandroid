package com.example.mindyourtravel;
import java.io.IOException;
import java.util.Calendar;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class TravelPlanActivity extends Activity {

	GPSTracker gps;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_travel_plan);
		ActivityHelper.setApplicationTitle(getWindow());
		/*// create class object
        gps = new GPSTracker(this);
        // check if GPS enabled     
        if(gps.canGetLocation())
        {
        	TextView curLocationView =(TextView) findViewById(R.id.txtCurrentLoc);
    		curLocationView.setText(gps.getCurrentLocation(this));
        		
       	}else
       	{
    		// can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
       	}*/
        
        SetErrorLabelVisibility(View.INVISIBLE,R.string.lblError);
        final EditText timebox= (EditText)findViewById(R.id.txtStartTime);
        Calendar cal= Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        String showTime=String.format("%1$tI:%1$tM %1$Tp",cal);
        timebox.setText(showTime);
        
        final Button btnTravelSubmit =(Button)findViewById(R.id.btnTravelSubmit);
        btnTravelSubmit.setOnClickListener(onClickbtnTravelSubmit);
        
        try
		{
		
	        JSONObject reqParameters= new JSONObject();
			reqParameters.put("CONUSERID", LaunchActivity.loginUserId);
			JsonHandler jsonHandler =JsonHandler.getInstance();
			String url=jsonHandler.getFullUrl("TravelPlanDataAdapter.php");
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
				JSONArray travelModeData =result.getJSONArray("TRAVELMODE");
				String[] travelType =new String[travelModeData.length()];
				for (int i=0;i<travelModeData.length();i++ ) 
				{
					JSONObject row= travelModeData.getJSONObject(i);
					travelType[i] = row.getString("TYPE");
				}

				ArrayAdapter<String> adapterTravel = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, travelType);
				adapterTravel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				final Spinner ddTravelType=(Spinner)findViewById(R.id.ddTravelType);
				ddTravelType.setAdapter(adapterTravel);
				
				JSONArray cityLocalitesData =result.getJSONArray("CITYLOCALITES");
				String[] cityLocalitesType =new String[cityLocalitesData.length()];
				for (int i=0;i<cityLocalitesData.length();i++ ) 
				{
					JSONObject row= cityLocalitesData.getJSONObject(i);
					cityLocalitesType[i] = row.getString("LOCALITY");
				}

				ArrayAdapter<String> adapterLocation = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, cityLocalitesType);
				adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				final Spinner ddCurrentLoc=(Spinner)findViewById(R.id.ddCurrentLoc);
				ddCurrentLoc.setAdapter(adapterLocation);
				
				final Spinner ddEndLocation=(Spinner)findViewById(R.id.ddEndLocation);
				ddEndLocation.setAdapter(adapterLocation);
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
	
	private OnClickListener onClickbtnTravelSubmit = new OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			//final  TextView txtCurrentLoc = (TextView)findViewById(R.id.txtCurrentLoc);
			final  Spinner  ddCurrentLoc =(Spinner)findViewById(R.id.ddCurrentLoc);
			final  TextView txtStartPoint = (TextView)findViewById(R.id.txtStartLoc);
			//final  TextView txtEndPoint = (TextView)findViewById(R.id.txtEndLoc);
			final  Spinner  ddEndLocation =(Spinner)findViewById(R.id.ddEndLocation);
			final  TextView txtStartTime = (TextView)findViewById(R.id.txtStartTime);
			final  TextView txtNoOfPass = (TextView)findViewById(R.id.txtNoOfPass);
			final  Spinner  ddTravelType =(Spinner)findViewById(R.id.ddTravelType);
			try
			{
				JSONObject reqParameters= new JSONObject();
				reqParameters.put("USERID", LaunchActivity.loginUserId);
				//reqParameters.put("CURRLOCATION", txtCurrentLoc.getText());
				reqParameters.put("CURRLOCATION", ddCurrentLoc.getSelectedItem());
				reqParameters.put("STARTLOCATION", txtStartPoint.getText());
				//reqParameters.put("ENDLOCATION", txtEndPoint.getText());
				reqParameters.put("ENDLOCATION", ddEndLocation.getSelectedItem());
				reqParameters.put("TRAVELTIME", txtStartTime.getText());
				reqParameters.put("TRAVELMODE", ddTravelType.getSelectedItem());
				reqParameters.put("NOOFPASSENGER", txtNoOfPass.getText());
				JsonHandler jsonHandler =JsonHandler.getInstance();
				String url=jsonHandler.getFullUrl("UserTravelPlan.php");
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
					Intent intent = new Intent(view.getContext(),TravelListActivity.class);
					startActivity(intent);
				}
			}
			catch(JSONException ex)
			{
				SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
			}
			catch (ClientProtocolException e)
			{    
				SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
			}    
			catch (IOException e) 
			{    
				SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
			} 
			catch(Exception e)
			{
				SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
			}
		}
	};

}
