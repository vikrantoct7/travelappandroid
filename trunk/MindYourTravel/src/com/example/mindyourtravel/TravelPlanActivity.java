package com.example.mindyourtravel;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.annotation.SuppressLint;
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
		
        String selLocality="";
		String userCity ="";
		String locPosition="";
		String persistPosition="";
		Bundle extras  = getIntent().getExtras();
		if(extras != null) {
			 selLocality=extras.getString("SELLOCALITY");
			 userCity=extras.getString("SELCITY");
			 locPosition= extras.getString("LOCPOSITION");
			 persistPosition= extras.getString("PERSISTPOSITION");
		 }
		else
		{
			// create class object
	        gps = new GPSTracker(this);
	        // check if GPS enabled     
	        if(gps.canGetLocation())
	        {
	        	//TextView curLocationView =(TextView) findViewById(R.id.txtCurrentLoc);
	    		//curLocationView.setText(gps.getCurrentLocation(this));
	        	userCity=gps.getCurrentCity();
	       	}else
	       	{
	    		// can't get location
	            // GPS or Network is not enabled
	            // Ask user to enable GPS/network in settings
	            gps.showSettingsAlert();
	       	}
		}
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
			reqParameters.put("LOGGEDINUSERID", LaunchActivity.loginUserId);
			reqParameters.put("GPSLOCATIONCITY", userCity);
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
				
				int spinnerPosition = adapterLocation.getPosition(selLocality);
				int existingPersistPosition = adapterLocation.getPosition(persistPosition);
				if(locPosition.equals("CURLOC"))
				{
					//set the default according to value
					ddCurrentLoc.setSelection(spinnerPosition);
					ddEndLocation.setSelection(existingPersistPosition);
				}
				else if(locPosition.equals("ENDLOC"))
				{
					//set the default according to value
					ddCurrentLoc.setSelection(existingPersistPosition);
					ddEndLocation.setSelection(spinnerPosition);
				}
				
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
        final Button btnPlusCurLoc = (Button)findViewById(R.id.btnPlusCurLoc);
        btnPlusCurLoc.setOnClickListener(onClickCurLocBtnPlusLoc);
        
        final Button btnPlusEndLoc = (Button)findViewById(R.id.btnPlusEndLoc);
        btnPlusEndLoc.setOnClickListener(onClickEndLocBtnPlusLoc);
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
		@SuppressLint("SimpleDateFormat")
		@SuppressWarnings("deprecation")
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
			
			boolean validationResult=true;
			GenericValidator validator = new GenericValidator();
			validationResult= validator.validate(txtStartPoint);
			validationResult= validator.validate(txtNoOfPass);
			if(validationResult)
			{
				
				try{
					SimpleDateFormat dateFormat= new SimpleDateFormat("hh:mm aa");
					Date inputTime= dateFormat.parse(txtStartTime.getText().toString());
					Date currentTime = dateFormat.parse(dateFormat.format(new Date()));
					
			        if(inputTime.getTime() < currentTime.getTime())
			        {
			        	txtStartTime.setError("Time can not be less than current time");
			        	return ;
			        }
			        currentTime.setHours(currentTime.getHours()+2);
			        if(inputTime.getTime() > currentTime.getTime())
			        {
			        	txtStartTime.setError("More than 2 hours advance time is not allow");
			        	return ;
			        }
			        
			        if(ddCurrentLoc.getSelectedItem() == ddEndLocation.getSelectedItem())
			        {
			        	SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorForSameLocation);
			        	return;
			        }
				}
				catch(ParseException e)
				{
					txtStartTime.setError("Invalid time");
				}
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
		}
	};
	
	private OnClickListener onClickEndLocBtnPlusLoc = new OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			final  Spinner  ddCurrentLoc =(Spinner)findViewById(R.id.ddCurrentLoc);
			Intent intent = new Intent(view.getContext(),LocationLocatorActivity.class);
			intent.putExtra("LOCPOSITION", "ENDLOC");
			intent.putExtra("PERSISTPOSITION", ddCurrentLoc.getSelectedItem().toString());
			startActivity(intent);
		}
	};
	
	private OnClickListener onClickCurLocBtnPlusLoc = new OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			
			final  Spinner  ddEndLocation =(Spinner)findViewById(R.id.ddEndLocation);
			
			Intent intent = new Intent(view.getContext(),LocationLocatorActivity.class);
			intent.putExtra("LOCPOSITION", "CURLOC");
			intent.putExtra("PERSISTPOSITION", ddEndLocation.getSelectedItem().toString());
			startActivity(intent);
		}
	};

}
