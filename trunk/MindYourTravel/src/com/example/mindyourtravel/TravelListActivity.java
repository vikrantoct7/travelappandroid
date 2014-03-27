package com.example.mindyourtravel;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.mindyourtravel.R.id;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
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
		setContentView(R.layout.activity_travel_list);
		
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
				if(errorCode.contentEquals(AppConstant.PHP_ERROR_CODE.TECHNICAL))
				{
					SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
				}
			}
			else
			{
				final Button btnNewPlan = (Button)findViewById(R.id.btnNewTravelPlan);
				btnNewPlan.setOnClickListener(onClickNewPlan);
				
				JSONArray jsonData =result.getJSONArray("TRAVELLIST");
				if(jsonData.length()>0)
				{
					btnNewPlan.setEnabled(false);
					generateTravelList(jsonData);
				}
				else
				{
					btnNewPlan.setEnabled(true);
				}
				
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
		
		final TableLayout tblUserDetail = (TableLayout) findViewById(R.id.tblUserDetail); 
		tblUserDetail.setVisibility(View.INVISIBLE);
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
				
				UserDTO userDto = new UserDTO();
				int userId = datarow.getInt("USERID");
				userDto.setUserId(userId);
				userDto.setFirstName(datarow.getString("UFNAME"));
				userDto.setLastName(datarow.getString("ULNAME"));
				userDto.setGender(datarow.getInt("GENDER"));
				userDto.setAge(datarow.getInt("AGE"));
				userDto.setContactNo(datarow.getString("UCONTACTNO"));
				userDto.setAppLoginUser(false);
		
				
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
				
				String age = Integer.toString(userDto.getAge());
				String userDetail = userDto.getUserFullName() + " "  + age + " " +userDto.getGenderStringValue();
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
				lblStartTime.setText(R.string.lblTravelerTimeMode);
				
				TextView displayStartTime =new TextView(this);
				displayStartTime.setLayoutParams(viewParams);
				displayStartTime.setText(datarow.getString("TRAVELTIME") + "/"+ datarow.getString("TRAVELMODE"));
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
				// TODO To be check whether it is effective to use setTag method for passing object
				if(datarow.getInt("ISSELFPLAN")==1)
				{
					btnSubmitTravel.setOnClickListener(onDeleteButton);
					btnSubmitTravel.setTag(datarow.getString("TRAVELID"));
					btnSubmitTravel.setText(R.string.btnDeleteTravel);
				}
				else
				{
					btnSubmitTravel.setOnClickListener(onConfirmButton);
					btnSubmitTravel.setTag(userDto);
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
			UserDTO userDto= (UserDTO) view.getTag();
			
			if(userDto !=null)
			{
				Context context =view.getContext();
	
				// Get the layout inflater    
				LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				View userView = mInflater.inflate(R.layout.customdialog,null);
				TextView txtTravelerName = (TextView) userView.findViewById(id.txtTravelerName);
				txtTravelerName.setText(userDto.getUserFullName());
				TextView txtAge =(TextView) userView.findViewById(id.txtAge);
				String age = Integer.toString(userDto.getAge());
				txtAge.setText( age);
				TextView txtGender = (TextView)userView.findViewById(id.txtGender);
				txtGender.setText(userDto.getGenderStringValue());
				TextView txtContactNo =(TextView)userView.findViewById(id.txtContactNo);
				txtContactNo.setText(userDto.getContactNo());
				
				
				//Ask the user if they want to quit
		        new AlertDialog.Builder(context)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setView(userView)
		        .setTitle(R.string.titleConfirmBox)
		        .setPositiveButton(R.string.lblYes, new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		              
		            	}
		            })
			        .setNegativeButton(R.string.lblNo, null)
			        .show();
			}
		}
	};
	
	private OnClickListener onDeleteButton = new OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			final String travelid=(String) view.getTag();
			
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
