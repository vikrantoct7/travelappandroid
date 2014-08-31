package com.example.LetsGoo;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.LetsGoo.R;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


public class TravelPlanActivity extends Activity {

	GPSTracker gps;
	public static TravelPlanDTO travelPlanDTO=null;
	String userStartLocCity ="";
	String userEndLocCity ="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_travel_plan);
		
		String selLocality="";
		
		String locPosition="";
		String persistPosition="";
		String persistTravelMode="";
		
		
		ActivityHelper.setApplicationTitle(getWindow());
		
        Bundle extras  = getIntent().getExtras();
		if(extras != null) {
			 if(travelPlanDTO !=null)
			 {
				 final  TextView txtStartPoint = (TextView)findViewById(R.id.txtStartLoc);
				 final  TextView txtNoOfPass = (TextView)findViewById(R.id.txtNoOfPass);
				
				 locPosition= travelPlanDTO.getLocationPosition();
				 persistPosition= travelPlanDTO.getLocationValue();
				 txtStartPoint.setText(travelPlanDTO.getStartLocation());
				 txtNoOfPass.setText(travelPlanDTO.getTotalNoOfPerson());
				 persistTravelMode = travelPlanDTO.getTravelMode();
				 userStartLocCity=travelPlanDTO.getCurUserCity();
				 userEndLocCity=travelPlanDTO.getEndUserCity();
			 }
			selLocality=extras.getString("SELLOCALITY");
			if(locPosition.equals("CURLOC"))
			{
				//set the default according to value
				userStartLocCity=extras.getString("SELCITY");
				if(userEndLocCity.length()==0)
				{
					userEndLocCity=userStartLocCity;
				}

			}
			else if(locPosition.equals("ENDLOC"))
			{
				//set the default according to value
				userEndLocCity=extras.getString("SELCITY");
				if(userStartLocCity.length()==0)
				{
					userStartLocCity=userEndLocCity;
				}
			}
			 
		 }
		else
		{
			// create class object
	        gps = new GPSTracker(this);
	        // check if GPS enabled     
	        if(gps.canGetLocation())
	        {
	    		//curLocationView.setText(gps.getCurrentLocation(this));
	        	userStartLocCity=gps.getCurrentCity();
	        	userEndLocCity=userStartLocCity;
	       	}else
	       	{
	    		// can't get location
	            // GPS or Network is not enabled
	            // Ask user to enable GPS/network in settings
	            gps.showSettingsAlert();
	       	}
		}
        setErrorLabelVisibility(View.INVISIBLE,R.string.lblError);
		
        
        final Button btnTravelSubmit =(Button)findViewById(R.id.btnTravelSubmit);
        btnTravelSubmit.setOnClickListener(onClickbtnTravelSubmit);
        
        try
		{
		    JSONObject reqParameters= new JSONObject();
			reqParameters.put("LOGGEDINUSERID", LaunchActivity.loginUserId);
			reqParameters.put("STARTLOCATIONCITY", userStartLocCity);
			reqParameters.put("ENDLOCATIONCITY", userEndLocCity);
			JsonHandler jsonHandler =JsonHandler.getInstance();
			String url=jsonHandler.getFullUrl("TravelPlanDataAdapter.php");
			JSONObject result = jsonHandler.postJsonDataToServer(url, reqParameters,this);
			if(result !=null)
			{
				String resultCode= result.getString("RESULT");
				if(resultCode.contentEquals(AppConstant.PHPRESPONSE_KO))
				{
					String errorCode=result.getString("ERRORCODE");
					if(errorCode.contentEquals(AppConstant.PHP_ERROR_CODE.NOTEXISTS))
					{
						setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorUserNotExist);
					}
					else if(errorCode.contentEquals(AppConstant.PHP_ERROR_CODE.TECHNICAL))
					{
						setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
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
					int spinnerTravelPosition=adapterTravel.getPosition(persistTravelMode);
					ddTravelType.setSelection(spinnerTravelPosition);
					
					ArrayAdapter<String> adapterCurLocation = getLocationAdapterFromJsonResult(result,"STARTCITYLOCALITES");
					ArrayAdapter<String> adapterEndLocation = adapterCurLocation;
					if(!userStartLocCity.equalsIgnoreCase(userEndLocCity) )
					{
						adapterEndLocation = getLocationAdapterFromJsonResult(result,"ENDCITYLOCALITES");
					}
					
					final Spinner ddCurrentLoc=(Spinner)findViewById(R.id.ddCurrentLoc);
					ddCurrentLoc.setAdapter(adapterCurLocation);
					
					final Spinner ddEndLocation=(Spinner)findViewById(R.id.ddEndLocation);
					ddEndLocation.setAdapter(adapterEndLocation);
					
					
					if(locPosition.equals("CURLOC"))
					{
						int spinnerPosition = adapterCurLocation.getPosition(selLocality);
						int existingPersistPosition = adapterEndLocation.getPosition(persistPosition);
						//set the default according to value
						ddCurrentLoc.setSelection(spinnerPosition);
						ddEndLocation.setSelection(existingPersistPosition);
					}
					else if(locPosition.equals("ENDLOC"))
					{
						int spinnerPosition = adapterEndLocation.getPosition(selLocality);
						int existingPersistPosition = adapterCurLocation.getPosition(persistPosition);
						//set the default according to value
						ddCurrentLoc.setSelection(existingPersistPosition);
						ddEndLocation.setSelection(spinnerPosition);
					}
					
					fillStartTimeDd();
					
				}
			}
		}
        catch(ConnectException ie)
		{
			setErrorLabelVisibility(View.VISIBLE,R.string.InternetConnectiivityErrorMsg);
		}
        catch (IOException e) 
		{    
			setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
		} 
		catch(JSONException ex)
		{
			setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
		}
        final Button btnPlusCurLoc = (Button)findViewById(R.id.btnPlusCurLoc);
        btnPlusCurLoc.setOnClickListener(onClickCurLocBtnPlusLoc);
        
        final Button btnPlusEndLoc = (Button)findViewById(R.id.btnPlusEndLoc);
        btnPlusEndLoc.setOnClickListener(onClickEndLocBtnPlusLoc);
        

	}

	private ArrayAdapter<String> getLocationAdapterFromJsonResult(JSONObject result,String type) 
			throws JSONException {
		JSONArray cityLocalitesData =result.getJSONArray(type);
		String[] cityLocalitesType =new String[cityLocalitesData.length()];
		for (int i=0;i<cityLocalitesData.length();i++ ) 
		{
			JSONObject row= cityLocalitesData.getJSONObject(i);
			cityLocalitesType[i] = row.getString("LOCALITY");
		}
						
		ArrayAdapter<String> adapterLocation = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, cityLocalitesType);
		adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapterLocation;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.travel_plan, menu);
		return true;
	}
	
	private void setErrorLabelVisibility(int visibility,int errorResId)
	{
		TextView lblError =(TextView)findViewById(R.id.lblTravelErrorMsg);
		if(lblError != null)
		{
			lblError.setVisibility(visibility);
			lblError.setText(errorResId);
		}
	}
	
	private void fillStartTimeDd()
	{
		String[] startTime =new String[8];
		
        Calendar cal= Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        int sysMinutes= cal.getTime().getMinutes();
        int calMinutes = sysMinutes +(15-sysMinutes % 15);
        Date calculatedDate= cal.getTime();

        for (int i=0;i<8;i++ ) 
		{ 
        	calculatedDate.setMinutes(calMinutes);
            String showTime=String.format("%1$tI:%1$tM %1$Tp",calculatedDate);
            calMinutes =calculatedDate.getMinutes();
        	startTime[i]=showTime;
        	calMinutes =calMinutes+15;
		}
        
        ArrayAdapter<String> adapterStartTime = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, startTime);
        adapterStartTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       
		final Spinner ddStartTime=(Spinner)findViewById(R.id.ddStartTime);
		ddStartTime.setAdapter(adapterStartTime);
		if(travelPlanDTO !=null)
        {
        	int spinnerPosition = adapterStartTime.getPosition(travelPlanDTO.getStartTime());
        	ddStartTime.setSelection(spinnerPosition);
        }
		
        
	}
	
	private OnClickListener onClickbtnTravelSubmit = new OnClickListener()
	{
		@SuppressLint("SimpleDateFormat")
		@Override
		public void onClick(View view)
		{
			travelPlanDTO=null;
			final  Spinner  ddCurrentLoc =(Spinner)findViewById(R.id.ddCurrentLoc);
			final  TextView txtStartPoint = (TextView)findViewById(R.id.txtStartLoc);
			final  Spinner  ddEndLocation =(Spinner)findViewById(R.id.ddEndLocation);
			final  Spinner ddStartTime = (Spinner)findViewById(R.id.ddStartTime);
			final  TextView txtNoOfPass = (TextView)findViewById(R.id.txtNoOfPass);
			final  Spinner  ddTravelType =(Spinner)findViewById(R.id.ddTravelType);
			
			boolean validationResult=true;
			GenericValidator validator = new GenericValidator();
			validationResult= validator.validate(txtStartPoint);
			validationResult= validator.validate(txtNoOfPass);
			if(validationResult)
			{
				if(ddCurrentLoc.getSelectedItem() == ddEndLocation.getSelectedItem())
		        {
		        	setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorForSameLocation);
		        	return;
		        }
				
				if(txtNoOfPass.getText().toString().equals("0"))
				{
					setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorForTotalNoOfPerson);
		        	return;
				}
				try
				{
					JSONObject reqParameters= new JSONObject();
					reqParameters.put("USERID", LaunchActivity.loginUserId);
					reqParameters.put("CURRLOCATION", ddCurrentLoc.getSelectedItem());
					reqParameters.put("STARTLOCATION", txtStartPoint.getText());
					reqParameters.put("ENDLOCATION", ddEndLocation.getSelectedItem());
					reqParameters.put("TRAVELTIME", ddStartTime.getSelectedItem());
					reqParameters.put("TRAVELMODE", ddTravelType.getSelectedItem());
					reqParameters.put("NOOFPASSENGER", txtNoOfPass.getText());
					JsonHandler jsonHandler =JsonHandler.getInstance();
					String url=jsonHandler.getFullUrl("UserTravelPlan.php");
					JSONObject result = jsonHandler.postJsonDataToServer(url, reqParameters,view.getContext());
					if(result!=null)
					{
						String resultCode= result.getString("RESULT");
						if(resultCode.contentEquals(AppConstant.PHPRESPONSE_KO))
						{
							String errorCode=result.getString("ERRORCODE");
							if(errorCode.contentEquals(AppConstant.PHP_ERROR_CODE.NOTEXISTS))
							{
								setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorUserNotExist);
							}
							else if(errorCode.contentEquals(AppConstant.PHP_ERROR_CODE.TECHNICAL))
							{
								setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
							}
						}
						else
						{
							Intent intent = new Intent(view.getContext(),TravelListActivity.class);
							startActivity(intent);
						}
					}
				}
				catch(ConnectException ie)
				{
					setErrorLabelVisibility(View.VISIBLE,R.string.InternetConnectiivityErrorMsg);
				}
				catch(JSONException ex)
				{
					setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
				}
				catch (ClientProtocolException e)
				{    
					setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
				}    
				catch (IOException e) 
				{    
					setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
				} 
				catch(Exception e)
				{
					setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
				}
			}
			
		}
	};
	
	private OnClickListener onClickEndLocBtnPlusLoc = new OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			
			Intent intent = new Intent(view.getContext(),LocationLocatorActivity.class);
						
			final  Spinner  ddCurrentLoc =(Spinner)findViewById(R.id.ddCurrentLoc);
			final  TextView txtStartPoint = (TextView)findViewById(R.id.txtStartLoc);
			final  Spinner ddStartTime = (Spinner)findViewById(R.id.ddStartTime);
			final  TextView txtNoOfPass = (TextView)findViewById(R.id.txtNoOfPass);
			final  Spinner  ddTravelType =(Spinner)findViewById(R.id.ddTravelType);
			
			travelPlanDTO = new TravelPlanDTO();
			travelPlanDTO.setLocationPosition("ENDLOC");
			if(ddCurrentLoc.getSelectedItem() !=null)
			{
				travelPlanDTO.setLocationValue(ddCurrentLoc.getSelectedItem().toString());
			}
			travelPlanDTO.setStartLocation(txtStartPoint.getText().toString());
			travelPlanDTO.setStartTime(ddStartTime.getSelectedItem().toString());
			travelPlanDTO.setTravelMode(ddTravelType.getSelectedItem().toString());
			travelPlanDTO.setTotalNoOfPerson(txtNoOfPass.getText().toString());
			travelPlanDTO.setCurUserCity(userStartLocCity);
			travelPlanDTO.setEndUserCity(userEndLocCity);
			startActivity(intent);
			
		}
	};
	
	private OnClickListener onClickCurLocBtnPlusLoc = new OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			Intent intent = new Intent(view.getContext(),LocationLocatorActivity.class);
			
			final  Spinner  ddEndLocation =(Spinner)findViewById(R.id.ddEndLocation);
			final  TextView txtStartPoint = (TextView)findViewById(R.id.txtStartLoc);
			final  Spinner ddStartTime = (Spinner)findViewById(R.id.ddStartTime);
			final  TextView txtNoOfPass = (TextView)findViewById(R.id.txtNoOfPass);
			final  Spinner  ddTravelType =(Spinner)findViewById(R.id.ddTravelType);

			travelPlanDTO = new TravelPlanDTO();
			travelPlanDTO.setLocationPosition("CURLOC");
			if(ddEndLocation.getSelectedItem() !=null)
			{
				travelPlanDTO.setLocationValue(ddEndLocation.getSelectedItem().toString());
			}
			travelPlanDTO.setStartLocation(txtStartPoint.getText().toString());
			travelPlanDTO.setStartTime(ddStartTime.getSelectedItem().toString());
			travelPlanDTO.setTravelMode(ddTravelType.getSelectedItem().toString());
			travelPlanDTO.setTotalNoOfPerson(txtNoOfPass.getText().toString());
			travelPlanDTO.setCurUserCity(userStartLocCity);
			travelPlanDTO.setEndUserCity(userEndLocCity);
			startActivity(intent);
			
		}
	};

}
