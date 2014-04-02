package com.example.mindyourtravel;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.mindyourtravel.R.id;

import android.net.Uri;
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

	private int CurUserTravelID;
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
				JSONArray jsonData =result.getJSONArray("TRAVELLIST");
				generateTravelList(jsonData);
			}
		}
		catch(JSONException ex)
		{
			SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
		}
		catch (ClientProtocolException ex)
		{    
			SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
		}    
		catch (IOException ex) 
		{    
			SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
		} 
		catch(Exception ex)
		{
			SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
		}
		
		final TableLayout tblUserDetail = (TableLayout) findViewById(R.id.tblUserDetail); 
		tblUserDetail.setVisibility(View.INVISIBLE);
	}

	private void generateTravelList(JSONArray jsonData)
	{
		final Button btnNewPlan = (Button)findViewById(R.id.btnNewTravelPlan);
		btnNewPlan.setOnClickListener(onClickNewPlan);
		if(jsonData.length()>0)
		{
			btnNewPlan.setEnabled(false);
		}
		else
		{
			btnNewPlan.setEnabled(true);
		}
		SetErrorLabelVisibility(View.INVISIBLE,R.string.lblErrorTechnical);
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
				userDto.setTravelId(datarow.getInt("TRAVELID"));
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
				Button btnShowMobileNo = null;
				// TODO To be check whether it is effective to use setTag method for passing object
				if(datarow.getInt("ISSELFPLAN")==1)
				{
					CurUserTravelID=datarow.getInt("TRAVELID");
					btnSubmitTravel.setOnClickListener(onDeleteAction);
					btnSubmitTravel.setTag(Integer.toString(CurUserTravelID));
					btnSubmitTravel.setText(R.string.btnDeleteTravel);
				}
				else
				{
					btnSubmitTravel.setText(R.string.btnConfimTravel);
					int isconfirmed=0;
					if(!datarow.isNull("ISCONFIRMED"))
					{
						isconfirmed =datarow.getInt("ISCONFIRMED");
					}
					if( isconfirmed ==1)
					{
						btnShowMobileNo = new Button(this);
						btnShowMobileNo.setText(R.string.btnShowMobileNo);
						btnShowMobileNo.setTag(userDto.getContactNo());
						btnShowMobileNo.setOnClickListener(onShowMobileAction);
						
						btnSubmitTravel.setEnabled(false);
					}
					else
					{
						btnSubmitTravel.setOnClickListener(onConfirmAction);
						btnSubmitTravel.setTag(userDto);
					}
					
				}
				tblrow5.addView(btnSubmitTravel);
				if(btnShowMobileNo !=null)
				{
					tblrow5.addView(btnShowMobileNo);
				}
				
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
	
	private OnClickListener onShowMobileAction = new OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			String strMobileNo=(String) view.getTag();
			int lenght = strMobileNo.length();
			final String mobileNoToCall = "91" + strMobileNo.substring(lenght -10, lenght);
			 new AlertDialog.Builder(view.getContext())
		        .setIcon(android.R.drawable.ic_dialog_info)
		        .setTitle(R.string.titleShowMobileBox)
		        .setMessage("Mobile No : "+ mobileNoToCall)
		        .setPositiveButton(R.string.lblToCall, new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            //No implementation required
		            	Intent intendtoCall= new Intent(Intent.ACTION_CALL);
		            	intendtoCall.setData(Uri.parse("tel:"+mobileNoToCall));
		            	startActivity(intendtoCall);
		            }
		        })
		        .setNegativeButton(R.string.lblNo, null)
		        .show();;
		}
	};
	
	private OnClickListener onConfirmAction = new OnClickListener()
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
				final int travelid =CurUserTravelID;
				final int usertravelid =userDto.getTravelId();
				
				//Ask the user if they want to quit
		        new AlertDialog.Builder(context)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setView(userView)
		        .setTitle(R.string.titleConfirmBox)
		        .setPositiveButton(R.string.lblYes, new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	try
		            	{
			            	JSONObject reqParameters= new JSONObject();
			    			reqParameters.put("CURUSERTRAVELID", travelid);
			    			reqParameters.put("USERTRAVELID", usertravelid);
			    			reqParameters.put("CONUSERID", LaunchActivity.loginUserId);
			    			JsonHandler jsonHandler =JsonHandler.getInstance();
			    			String url=jsonHandler.getFullUrl("UserTravelConfirm.php");
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
		}
	};
	
	private OnClickListener onDeleteAction = new OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			final String travelid=(String) view.getTag();
			//Ask the user if they want to quit
	        new AlertDialog.Builder(view.getContext())
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle(R.string.titleConfirmBox)
	        .setMessage(R.string.lblPlanDeleteMsg)
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
	
	@Override
	public void onBackPressed() {
	    // do nothing.
	}
}
