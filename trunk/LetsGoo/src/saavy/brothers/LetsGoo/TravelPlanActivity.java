package saavy.brothers.LetsGoo;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import saavy.brothers.LetsGoo.R;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;


public class TravelPlanActivity extends Activity {

	public static TravelPlanDTO travelPlanDTO=null;
	String userStartLocCity ="";
	String userEndLocCity =""; 
	String Travel_mode_hint="Travel Mode";
	String Start_loc_hint="Start Location";
	String End_loc_hint="End Location";
	String Travel_time_hint="Start Time";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityHelper.turnGPSOn(this);
		setContentView(R.layout.activity_travel_plan);
		
		String selLocality="";
		
		String locPosition="";
		String persistPosition="";
		String persistTravelMode="";
		
		
		ActivityHelper.setApplicationTitle(getWindow());
		userStartLocCity=LaunchActivity.repository.getUsersData().getUserCity();
		userEndLocCity=userStartLocCity;
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
				//if(userEndLocCity.length()==0)
				//{
					userEndLocCity=userStartLocCity;
				//}
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
			GPSTracker gps = new GPSTracker(this);
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
			reqParameters.put("LOGGEDINUSERID", LaunchActivity.getUserId());
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
					ArrayList<String> travelType =new ArrayList<String>(); 
					for (int i=0;i<travelModeData.length();i++ ) 
					{
						JSONObject row= travelModeData.getJSONObject(i);
						travelType.add(row.getString("TYPE"));
					}
					
					HintAdapter adapterTravel = new HintAdapter(this,  android.R.layout.simple_spinner_item, travelType);
					adapterTravel.setDropDownViewResource(R.layout.activity_settings_spinner_item);
					adapterTravel.add(Travel_mode_hint);
					final Spinner ddTravelType=(Spinner)findViewById(R.id.ddTravelType);
					ddTravelType.setAdapter(adapterTravel);
					int spinnerTravelPosition=adapterTravel.getPosition(persistTravelMode);
					if(spinnerTravelPosition==-1)
					{
						spinnerTravelPosition=adapterTravel.getCount();
					}
					ddTravelType.setSelection(spinnerTravelPosition);
					
							
					HintAdapter adapterCurLocation = getLocationAdapterFromJsonResult(result,"STARTCITYLOCALITES",Start_loc_hint);
					adapterCurLocation.add(Start_loc_hint);
					HintAdapter adapterEndLocation = null;
					
					if(!userStartLocCity.equalsIgnoreCase(userEndLocCity) )
					{
						adapterEndLocation = getLocationAdapterFromJsonResult(result,"ENDCITYLOCALITES",End_loc_hint);
					}
					else
					{
						adapterEndLocation= getLocationAdapterFromJsonResult(result,"STARTCITYLOCALITES",End_loc_hint);
					}
					adapterEndLocation.add(End_loc_hint);
					
					final Spinner ddCurrentLoc=(Spinner)findViewById(R.id.ddCurrentLoc);
					ddCurrentLoc.setAdapter(adapterCurLocation);
					ddCurrentLoc.setSelection(adapterCurLocation.getCount());		
					
					final Spinner ddEndLocation=(Spinner)findViewById(R.id.ddEndLocation);
					ddEndLocation.setAdapter(adapterEndLocation);
					ddEndLocation.setSelection(adapterEndLocation.getCount());

					
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
		

	private HintAdapter getLocationAdapterFromJsonResult(JSONObject result,String type,String hint) 
			throws JSONException {
		JSONArray cityLocalitesData =result.getJSONArray(type);
		ArrayList<String> cityLocalitesType =new ArrayList<String>();
		if(cityLocalitesData.length()==0)
		{
			cityLocalitesType.add(AppConstant.EMPTYSTRING);
		}
		else
		{
			for (int i=0;i<cityLocalitesData.length();i++ ) 
			{
				JSONObject row= cityLocalitesData.getJSONObject(i);
				cityLocalitesType.add(row.getString("LOCALITY"));
			}
		}
		
								
		HintAdapter adapterLocation = new HintAdapter(this,  android.R.layout.simple_spinner_item, cityLocalitesType);
		adapterLocation.setDropDownViewResource(R.layout.activity_settings_spinner_item);
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
		TableRow tableRow2 =(TableRow)findViewById(R.id.ErrorRowOnTravelPlanPage);
		if(tableRow2 !=null)
		{
			TextView lblError =(TextView)findViewById(R.id.lblErrorMsgOnTravelPlanePage);
			if(lblError != null)
			{
				tableRow2.setVisibility(visibility);
				lblError.setText(errorResId);
			}
		}
	}
	
	private void fillStartTimeDd()
	{
		ArrayList<String> startTime =new ArrayList<String>();
		
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
        	startTime.add(showTime);
        	calMinutes =calMinutes+15;
		}
        
        HintAdapter adapterStartTime = new HintAdapter(this,  android.R.layout.simple_spinner_item, startTime);
        adapterStartTime.setDropDownViewResource(R.layout.activity_settings_spinner_item);
        adapterStartTime.add(Travel_time_hint);
		final Spinner ddStartTime=(Spinner)findViewById(R.id.ddStartTime);
		ddStartTime.setAdapter(adapterStartTime);
		if(travelPlanDTO !=null)
        {
        	int spinnerPosition = adapterStartTime.getPosition(travelPlanDTO.getStartTime());
        	ddStartTime.setSelection(spinnerPosition);
        }
		else
		{
			ddStartTime.setSelection(adapterStartTime.getCount());
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
			//validationResult= validator.validate(txtStartPoint);
			validationResult= validator.validate(txtNoOfPass);
			if(ddCurrentLoc.getSelectedItem()==Start_loc_hint || ddCurrentLoc.getSelectedItem()==AppConstant.EMPTYSTRING)
			{
				View v= ddCurrentLoc.getSelectedView();
				TextView temp= (TextView)v.findViewById(android.R.id.text1);
				temp.setError("This is mandatory!");
				validationResult=false;
			}
	
			
			if(ddEndLocation.getSelectedItem()==End_loc_hint || ddEndLocation.getSelectedItem()==AppConstant.EMPTYSTRING)
			{
				View v= ddEndLocation.getSelectedView();
				TextView temp= (TextView)v.findViewById(android.R.id.text1);
				temp.setError(End_loc_hint);
				validationResult=false;
			}
			
			if(ddTravelType.getSelectedItem()==Travel_mode_hint)
			{
				View v= ddTravelType.getSelectedView();
				TextView temp= (TextView)v.findViewById(android.R.id.text1);
				temp.setError(Travel_mode_hint);
				validationResult=false;
			}
			
			if(ddStartTime.getSelectedItem()==Travel_time_hint)
			{
				View v= ddStartTime.getSelectedView();
				TextView temp= (TextView)v.findViewById(android.R.id.text1);
				temp.setError(Travel_time_hint);
				validationResult=false;
			}
			
			if(validationResult)
			{
				
				if(ddCurrentLoc.getSelectedItem() == ddEndLocation.getSelectedItem())
		        {
		        	setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorForSameLocation);
		        	return;
		        }
				
				int noOfPass = Integer.parseInt(txtNoOfPass.getText().toString());
				if(noOfPass==0)
				{
					setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorForTotalNoOfPerson);
		        	return;
				}
				else if(noOfPass>10)
				{
					setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorForTotalNoOfPersonGTTen);
		        	return;
				}
				try
				{
					JSONObject reqParameters= new JSONObject();
					reqParameters.put("USERID", LaunchActivity.getUserId());
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
			else
			{
				setErrorLabelVisibility(View.VISIBLE,R.string.ErrorMandatoryMsg);
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
			if(ddCurrentLoc.getSelectedItem() !=null && ddCurrentLoc.getSelectedItem()!=Start_loc_hint)
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
			if(ddEndLocation.getSelectedItem() !=null && ddEndLocation.getSelectedItem()!=End_loc_hint)
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
