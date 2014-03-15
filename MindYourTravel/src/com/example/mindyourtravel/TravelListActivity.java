package com.example.mindyourtravel;

import java.io.IOException;
import java.util.Collections;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TravelListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);
		final Button btnNewPlan = (Button)findViewById(R.id.btnNewTravelPlan);
		btnNewPlan.setOnClickListener(onClickNewPlan);
		SetErrorLabelVisibility(View.INVISIBLE,R.string.lblError);
		try
		{
			JSONObject reqParameters= new JSONObject();
			reqParameters.put("CONUSERID", LaunchActivity.loginUserId);
			JsonHandler jsonHandler =JsonHandler.getInstance();
			String url=jsonHandler.getFullUrl("UserTravelList.php");
			JSONObject result = jsonHandler.PostJsonDataToServer(url, reqParameters);
			String resultCode= result.getString("RESULT");
			if(resultCode.contentEquals(AppConstant.PHPResponse_KO))
			{
				String errorCode=result.getString("ERRORCODE");
				/*if(errorCode.contentEquals(AppConstant.PHP_ERROR_CODE.NOTEXISTS))
				{
					SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorUserNotExist);
				}
				else*/ 
				if(errorCode.contentEquals(AppConstant.PHP_ERROR_CODE.TECHNICAL))
				{
					SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
				}
			}
			else
			{
				JSONArray jsonData =result.getJSONArray("TRAVELLIST");
				generateTravelList(jsonData);
				
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

	private void generateTravelList(JSONArray jsonData)
	{
		try
		{
			TableLayout tblParentTravelDetails = (TableLayout) findViewById(R.id.tblParentTravelDetails);
			tblParentTravelDetails.removeAllViews();
			
	
			@SuppressWarnings("deprecation")
			TableRow.LayoutParams tblparams = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
			
			@SuppressWarnings("deprecation")
			TableRow.LayoutParams rowparams = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
			@SuppressWarnings("deprecation")
			TableRow.LayoutParams viewParams = new TableRow.LayoutParams(0,TableRow.LayoutParams.FILL_PARENT,1);
			
			for (int i=0;i<jsonData.length();i++ ) 
			{
				JSONObject datarow= jsonData.getJSONObject(i);
				
				TableLayout tblTravelDetails = new TableLayout(this);
				
				tblTravelDetails.setLayoutParams(tblparams);
				tblTravelDetails.setOrientation(LinearLayout.HORIZONTAL);
				
				TableRow tblrow1= new TableRow(this);
				
	
				
				tblrow1.setLayoutParams(rowparams);
				tblrow1.setPadding(0, 5, 0, 0);
				TextView lblUserDetails =new TextView(this);
				lblUserDetails.setLayoutParams(viewParams);
				lblUserDetails.setText(R.string.lblUserDetails);
				
				TextView displayUserDetails =new TextView(this);
				displayUserDetails.setLayoutParams(viewParams);
				int gender =datarow.getInt("GENDER");
				String genderStr="M";
				if(gender ==1)
				{
					genderStr="M";
				}
				else if(gender ==2)
				{
					genderStr="F";
				}
				String age = Integer.toString(datarow.getInt("AGE"));
				String userDetail = datarow.getString("UFNAME") + " " +  datarow.getString("ULNAME") + " "  + age + " " +genderStr;
				displayUserDetails.setText(userDetail);
				tblrow1.addView(lblUserDetails);
				tblrow1.addView(displayUserDetails);
				
				TableRow tblrow2= new TableRow(this);
				
				tblrow2.setLayoutParams(rowparams);
				tblrow2.setPadding(0, 5, 0, 0);
				TextView lblTravelDetails =new TextView(this);
				lblTravelDetails.setLayoutParams(viewParams);
				lblTravelDetails.setText(R.string.lblTravelDetails);
				
				TextView displayTravelDetails =new TextView(this);
				displayTravelDetails.setLayoutParams(viewParams);
				displayTravelDetails.setText(datarow.getString("CURRLOCATION") + "("+datarow.getString("STARTLOCATION")+") To " +datarow.getString("ENDLOCATION"));
				tblrow2.addView(lblTravelDetails);
				tblrow2.addView(displayTravelDetails);
				
				TableRow tblrow3= new TableRow(this);
				tblrow3.setLayoutParams(rowparams);
				tblrow3.setPadding(0, 5, 0, 0);
				TextView lblStartTime =new TextView(this);
				lblStartTime.setLayoutParams(viewParams);
				lblStartTime.setText(R.string.lblTravelTime);
				
				TextView displayStartTime =new TextView(this);
				displayStartTime.setLayoutParams(viewParams);
				displayStartTime.setText(datarow.getString("TRAVELTIME"));
				tblrow3.addView(lblStartTime);
				tblrow3.addView(displayStartTime);
				
				TableRow tblrow4= new TableRow(this);
				tblrow4.setLayoutParams(rowparams);
				tblrow4.setPadding(0, 5, 0, 0);
				TextView lblNoOfPassenger =new TextView(this);
				lblNoOfPassenger.setLayoutParams(viewParams);
				lblNoOfPassenger.setText(R.string.lblNoOfPassenger);
				
				TextView displayNoOfPassenger =new TextView(this);
				displayNoOfPassenger.setLayoutParams(viewParams);
				displayNoOfPassenger.setText(datarow.getString("NOOFPASSENGER"));
				tblrow4.addView(lblNoOfPassenger);
				tblrow4.addView(displayNoOfPassenger);
				
				TableRow tblrow5= new TableRow(this);
				tblrow5.setLayoutParams(rowparams);
				tblrow5.setPadding(0, 5, 0, 0);
				Button btnSubmitTravel = new Button(this);
				if(datarow.getInt("ISSELFPLAN")==1)
				{
					btnSubmitTravel.setOnClickListener(onDeleteButton);
					btnSubmitTravel.setHint(datarow.getString("TRAVELID"));
					btnSubmitTravel.setText(R.string.btnDeleteTravel);
				}
				else
				{
					btnSubmitTravel.setHint(datarow.getString("USERID"));
					btnSubmitTravel.setText(R.string.btnConfimTravel);
				}
				tblrow5.addView(btnSubmitTravel);
				
				tblTravelDetails.addView(tblrow1);
				tblTravelDetails.addView(tblrow2);
				tblTravelDetails.addView(tblrow3);
				tblTravelDetails.addView(tblrow4);
				tblTravelDetails.addView(tblrow5);
				tblParentTravelDetails.addView(tblTravelDetails,i);
			}
		}
		catch(JSONException ex)
		{
			SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page, menu);
		return true;
	}
	
	private OnClickListener onClickNewPlan = new OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			Intent intent = new Intent(view.getContext(),TravelPlanActivity.class);
			startActivity(intent);
		}
	};
	
	private OnClickListener onConfirmButton = new OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			Intent intent = new Intent(view.getContext(),TravelPlanActivity.class);
			startActivity(intent);
		}
	};
	
	private OnClickListener onDeleteButton = new OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			final Button deleteBtn = (Button)view;
			
			//Ask the user if they want to quit
	        new AlertDialog.Builder(view.getContext())
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle(R.string.titleConfirmBox)
	        .setMessage(R.string.titlePlanDeleteMsg)
	        .setPositiveButton(R.string.lblYes, new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	            		
	            	try
	            	{
		            	String travelid=(String) deleteBtn.getHint();
		            	JSONObject reqParameters= new JSONObject();
		    			reqParameters.put("TRAVELID", travelid);
		    			reqParameters.put("CONUSERID", LaunchActivity.loginUserId);
		    			JsonHandler jsonHandler =JsonHandler.getInstance();
		    			String url=jsonHandler.getFullUrl("UserTravelDelete.php");
		    			JSONObject result = jsonHandler.PostJsonDataToServer(url, reqParameters);
		    			String resultCode= result.getString("RESULT");
		    			if(resultCode.contentEquals(AppConstant.PHPResponse_KO))
		    			{
		    				String errorCode=result.getString("ERRORCODE");
		    				
		    				if(errorCode.contentEquals(AppConstant.PHP_ERROR_CODE.TECHNICAL))
		    				{
		    					SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
		    				}
		    			}
		    			else
		    			{
		    				JSONArray jsonData =result.getJSONArray("TRAVELLIST");
		    				generateTravelList(jsonData);
		    			}
		            }
			    	catch(JSONException ex)
			    		{
			    			SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
			    		}
		            	catch (IOException e) 
		        		{    
		        			SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
		        		} 
		            }

	        })
	        .setNegativeButton(R.string.lblNo, null)
	        .show();


		}
	};
	
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
