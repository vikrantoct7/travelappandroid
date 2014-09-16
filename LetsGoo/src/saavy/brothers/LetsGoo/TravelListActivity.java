package saavy.brothers.LetsGoo;

import java.io.IOException;
import java.net.ConnectException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import saavy.brothers.LetsGoo.R;
import saavy.brothers.LetsGoo.R.id;

import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

	private int currentUserTravelID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_travel_list);
		ActivityHelper.setApplicationTitle(getWindow());
		
		setErrorLabelVisibility(View.INVISIBLE,R.string.lblError);
		loadTravelList();
		
		final Button btnRefresh = (Button)findViewById(R.id.btnRefresh);
		btnRefresh.setOnClickListener(onClickRefresh);

	}
	
	@Override
	protected void onRestart()
	{
		super.onRestart();
		loadTravelList();
	}
	
	
	private void loadTravelList()
	{
		try
		{
			JSONObject reqParameters= new JSONObject();
			reqParameters.put("CONUSERID", LaunchActivity.loginUserId);
			JsonHandler jsonHandler =JsonHandler.getInstance();
			String url=jsonHandler.getFullUrl("UserTravelList.php");
			JSONObject result = jsonHandler.postJsonDataToServer(url, reqParameters,this);
			if(result!=null)
			{
				String resultCode= result.getString("RESULT");
				if(resultCode.contentEquals(AppConstant.PHPRESPONSE_KO))
				{
					String errorCode=result.getString("ERRORCODE");
					if(errorCode.contentEquals(AppConstant.PHP_ERROR_CODE.TECHNICAL))
					{
						setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
					}
				}
				else
				{
					JSONArray jsonData =result.getJSONArray("TRAVELLIST");
					generateTravelList(jsonData);
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
		catch (IOException ex) 
		{    
			setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
		} 
	}

	@SuppressLint({ "ResourceAsColor", "NewApi" })
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
		setErrorLabelVisibility(View.INVISIBLE,R.string.lblErrorTechnical);
		try
		{
			TableLayout tblParentTravelDetails = (TableLayout) findViewById(R.id.tblParentTravelDetails);
			tblParentTravelDetails.removeAllViews();
			
	
			@SuppressWarnings("deprecation")
			TableLayout.LayoutParams tblparams = new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
			@SuppressWarnings("deprecation")
			TableRow.LayoutParams rowparams = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
			@SuppressWarnings("deprecation")
			TableRow.LayoutParams viewParams = new TableRow.LayoutParams(0,TableRow.LayoutParams.FILL_PARENT,1);
			@SuppressWarnings("deprecation")
			TableRow.LayoutParams lblNoOfPassengerParams = new TableRow.LayoutParams(0,TableRow.LayoutParams.FILL_PARENT,(float)0.5);
			@SuppressWarnings("deprecation")
			TableRow.LayoutParams displayNoOfPassengerParams = new TableRow.LayoutParams(0,TableRow.LayoutParams.FILL_PARENT,(float)0.25);
			
			int btnTextColor = Color.parseColor("#000000");
			int btnBackColor = Color.parseColor("#938953");
			int isTravelAlreadyconfirmed=0;
			int userIdWhoConfirmTravel=0;

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
				tblTravelDetails.setBackgroundColor(Color.WHITE);
				tblTravelDetails.setLayoutParams(tblparams);
				tblTravelDetails.setPadding(2, 0, 2, 0);
				tblTravelDetails.setBackgroundResource(R.drawable.cell_shape_new);
				tblTravelDetails.setOrientation(LinearLayout.HORIZONTAL);
				
				TableRow tblrow1= new TableRow(this);
			
				tblrow1.setLayoutParams(rowparams);
				tblrow1.setPadding(0, 3, 0, 0);
				TextView lblUserDetails =new TextView(this);

				lblUserDetails.setLayoutParams(viewParams);
				lblUserDetails.setText(R.string.lblUserDetails);
				
				TextView displayUserDetails =new TextView(this);
				displayUserDetails.setTextColor(btnTextColor);
				displayUserDetails.setLayoutParams(viewParams);
				
				String age = Integer.toString(userDto.getAge());
				String userDetail = userDto.getUserFullName() + " "  + age + " " +userDto.getGenderStringValue();
				displayUserDetails.setText(userDetail);
				tblrow1.addView(lblUserDetails);
				tblrow1.addView(displayUserDetails);
				
				TableRow tblrow2= new TableRow(this);
				
				tblrow2.setLayoutParams(rowparams);
				tblrow2.setPadding(0, 3, 0, 0);
				TextView lblTravelDetails =new TextView(this);
				lblTravelDetails.setLayoutParams(viewParams);
				lblTravelDetails.setText(R.string.lblTravelDetails);
				
				TextView displayTravelDetails =new TextView(this);
				displayTravelDetails.setTextColor(btnTextColor);
				displayTravelDetails.setLayoutParams(viewParams);
				displayTravelDetails.setText(datarow.getString("CURRLOCATION") + "("+datarow.getString("STARTLOCATION")+") To " +datarow.getString("ENDLOCATION"));
				tblrow2.addView(lblTravelDetails);
				tblrow2.addView(displayTravelDetails);
				
				TableRow tblrow3= new TableRow(this);
				tblrow3.setLayoutParams(rowparams);
				tblrow3.setPadding(0, 3, 0, 0);
				TextView lblStartTime =new TextView(this);
				lblStartTime.setLayoutParams(viewParams);
				lblStartTime.setText(R.string.lblTravelerTimeMode);
				
				TextView displayStartTime =new TextView(this);
				displayStartTime.setTextColor(btnTextColor);
				displayStartTime.setLayoutParams(viewParams);
				displayStartTime.setText(datarow.getString("TRAVELTIME") + "/"+ datarow.getString("TRAVELMODE"));
				tblrow3.addView(lblStartTime);
				tblrow3.addView(displayStartTime);
								
				TableRow tblrow4= new TableRow(this);
				tblrow4.setLayoutParams(rowparams);
				tblrow4.setPadding(0, 3, 0, 0);
				
				TextView lblNoOfPassenger =new TextView(this);
				
				lblNoOfPassenger.setLayoutParams(lblNoOfPassengerParams);
				lblNoOfPassenger.setText(R.string.lblNoOfPassenger);
				
				TextView displayNoOfPassenger =new TextView(this);
				displayNoOfPassenger.setLayoutParams(displayNoOfPassengerParams);
				displayNoOfPassenger.setText(datarow.getString("NOOFPASSENGER"));
				tblrow4.addView(lblNoOfPassenger);
				tblrow4.addView(displayNoOfPassenger);
				
				Button btnSubmitTravel = new Button(this);
				btnSubmitTravel.setTextColor(btnTextColor);
				btnSubmitTravel.setBackgroundColor(btnBackColor);
				
				// TODO To be check whether it is effective to use setTag method for passing object
				if(datarow.getInt("ISSELFPLAN")==1)
				{
					currentUserTravelID=datarow.getInt("TRAVELID");
					btnSubmitTravel.setOnClickListener(onDeleteAction);
					btnSubmitTravel.setTag(Integer.toString(currentUserTravelID));
					btnSubmitTravel.setText(R.string.btnDeleteTravel);
					tblrow4.addView(btnSubmitTravel);
				}
				else
				{
					if(isTravelAlreadyconfirmed ==0 && !datarow.isNull("ISCONFIRMED"))
					{
						isTravelAlreadyconfirmed =datarow.getInt("ISCONFIRMED");
						userIdWhoConfirmTravel =userId;
					}
					if( isTravelAlreadyconfirmed ==1 && userIdWhoConfirmTravel ==userId)
					{
						Button btnShowMobileNo = null;
						btnShowMobileNo = new Button(this);
						btnShowMobileNo.setTextColor(btnTextColor);
						btnShowMobileNo.setBackgroundColor(btnBackColor);
						btnShowMobileNo.setText(R.string.btnShowMobileNo);
						btnShowMobileNo.setTag(userDto.getContactNo());
						btnShowMobileNo.setOnClickListener(onShowMobileAction);
						tblrow4.addView(btnShowMobileNo);
					}
					else
					{
						if(isTravelAlreadyconfirmed ==1)
						{
							btnSubmitTravel.setEnabled(false);
						}
						else
						{
							btnSubmitTravel.setText(R.string.btnConfimTravel);
							btnSubmitTravel.setOnClickListener(onConfirmAction);
							btnSubmitTravel.setTag(userDto);
							tblrow4.addView(btnSubmitTravel);
						}
					}
					
				}
				
				tblTravelDetails.addView(tblrow1);
				tblTravelDetails.addView(tblrow2);
				tblTravelDetails.addView(tblrow3);
				tblTravelDetails.addView(tblrow4);
				tblParentTravelDetails.addView(tblTravelDetails,i);
			}
		}
		catch(JSONException ex)
		{
			setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page, menu);
		return true;
	}
	
	/*private OnClickListener onClickQuit = new OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			
		}
	};*/
	
	private OnClickListener onClickRefresh = new OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			loadTravelList();
		}
	};
			
	
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
			final String mobileNoToCall = "0" + strMobileNo.substring(lenght -10, lenght);
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
				final int travelid =currentUserTravelID;
				final int usertravelid =userDto.getTravelId();
				final int traveleruserid =userDto.getUserId();
				
				//Ask the user if they want to quit
		        //new AlertDialog.Builder(context)
		        //.setIcon(android.R.drawable.ic_dialog_alert)
		        //.setView(userView)
		        //.setTitle(R.string.titleConfirmBox)
		        //.setPositiveButton(R.string.lblYes, new DialogInterface.OnClickListener() {
		        //    @Override
		        //    public void onClick(DialogInterface dialog, int which) {
		            	try
		            	{
			            	JSONObject reqParameters= new JSONObject();
			    			reqParameters.put("CURUSERTRAVELID", travelid);
			    			reqParameters.put("USERTRAVELID", usertravelid);
			    			reqParameters.put("CONUSERID", LaunchActivity.loginUserId);
			    			reqParameters.put("TRAVELERUSERID", traveleruserid);
			    			JsonHandler jsonHandler =JsonHandler.getInstance();
			    			String url=jsonHandler.getFullUrl("UserTravelConfirm.php");
			    			JSONObject result = jsonHandler.postJsonDataToServer(url, reqParameters,view.getContext());
			    			if(result !=null)
			    			{
				    			String resultCode= result.getString("RESULT");
				    			if(resultCode.contentEquals(AppConstant.PHPRESPONSE_KO))
				    			{
				    				String errorCode=result.getString("ERRORCODE");
				    				
				    				if(errorCode.contentEquals(AppConstant.PHP_ERROR_CODE.TECHNICAL))
				    				{
				    					setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
				    				}
				    			}
				    			else
				    			{
				    				JSONArray jsonData =result.getJSONArray("TRAVELLIST");
				    				generateTravelList(jsonData);
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
			            catch (IOException e) 
			        	{    
			        		setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
			        	} 
			            //}
		            //})
			        //.setNegativeButton(R.string.lblNo, null)
			        //.show();
			}
		}
	};
	
	private OnClickListener onDeleteAction = new OnClickListener()
	{
		@Override
		public void onClick(final View dialogView)
		{
			final String travelid=(String) dialogView.getTag();
			//Ask the user if they want to quit
	        new AlertDialog.Builder(dialogView.getContext())
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
		    			JSONObject result = jsonHandler.postJsonDataToServer(url, reqParameters,dialogView.getContext());
		    			if(result !=null)
		    			{
			    			String resultCode= result.getString("RESULT");
			    			if(resultCode.contentEquals(AppConstant.PHPRESPONSE_KO))
			    			{
			    				String errorCode=result.getString("ERRORCODE");
			    				
			    				if(errorCode.contentEquals(AppConstant.PHP_ERROR_CODE.TECHNICAL))
			    				{
			    					setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
			    				}
			    			}
			    			else
			    			{
			    				JSONArray jsonData =result.getJSONArray("TRAVELLIST");
			    				generateTravelList(jsonData);
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
		           	catch (IOException e) 
		       		{    
		       			setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
		       		} 
	            }

	        })
	        .setNegativeButton(R.string.lblNo, null)
	        .show();
		}
	};
	
	private void setErrorLabelVisibility(int visibility,int errorResId)
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
