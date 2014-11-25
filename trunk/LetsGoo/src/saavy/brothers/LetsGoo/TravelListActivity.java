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
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
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
			reqParameters.put("CONUSERID", LaunchActivity.getUserId());
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
		catch (Exception ex) 
		{    
			setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
		} 
	}

	@SuppressWarnings("deprecation")
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
			TableLayout.LayoutParams tblparams = new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.FILL_PARENT,1);
			@SuppressWarnings("deprecation")
			TableRow.LayoutParams txtViewParams = new TableRow.LayoutParams(0,TableRow.LayoutParams.FILL_PARENT,1);
			@SuppressWarnings("deprecation")
			TableRow.LayoutParams paramsForDisplayTime = new TableRow.LayoutParams();
			paramsForDisplayTime.width=0;
			paramsForDisplayTime.weight=(float).75;
			
			@SuppressWarnings("deprecation")
			TableRow.LayoutParams paramsForDumyBox = new TableRow.LayoutParams();
			paramsForDumyBox.column=4;
			
						
			TableRow.LayoutParams paramsForIcons = new TableRow.LayoutParams();
			paramsForIcons.setMargins(10, 0, 0, 0);
			
			int textColor = Color.parseColor("#568536");
			int isTravelAlreadyconfirmedByCurrentUser=0;
			int textSize=14;
			if(jsonData.length()==0)
			{
				TableLayout tblTravelDetails = new TableLayout(this);
				tblTravelDetails.setLayoutParams(tblparams);
				TableRow tblrow1= new TableRow(this);
				tblrow1.setPadding(5, 5, 0, 0);
				TextView displayNoRecordMsg =new TextView(this);
				displayNoRecordMsg.setLayoutParams(txtViewParams);
				displayNoRecordMsg.setTypeface(Typeface.DEFAULT_BOLD);
				displayNoRecordMsg.setTextSize(textSize);
				displayNoRecordMsg.setTextColor(textColor);
				displayNoRecordMsg.setText(R.string.lblNoTravellerFoundMsg);
				tblrow1.addView(displayNoRecordMsg);
				tblTravelDetails.addView(tblrow1);
				tblParentTravelDetails.addView(tblTravelDetails,0);
			}
			else
			{
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
					
					TableRow tblrow1= new TableRow(this);
					tblrow1.setPadding(5, 5, 0, 0);
										
					TextView displayUserDetails =new TextView(this);
					displayUserDetails.setTypeface(Typeface.DEFAULT_BOLD);
					displayUserDetails.setTextSize(textSize);
					displayUserDetails.setTextColor(textColor);
					displayUserDetails.setLayoutParams(txtViewParams);
					
					String age = Integer.toString(userDto.getAge());
					String userDetail = userDto.getUserFullName() + " ("  + age + "/" +userDto.getGenderStringValue()+")";
					displayUserDetails.setText(userDetail);
					
					tblrow1.addView(displayUserDetails);
					
					TableRow tblrow2= new TableRow(this);
					tblrow2.setPadding(5, 0, 0, 0);
					
					
					TextView displayTravelDetails =new TextView(this);
					displayTravelDetails.setTextSize(textSize);
					displayTravelDetails.setTextColor(textColor);
					displayTravelDetails.setLayoutParams(txtViewParams);
					displayTravelDetails.setText(datarow.getString("CURRLOCATION") + "("+datarow.getString("STARTLOCATION")+") To " +datarow.getString("ENDLOCATION"));
					
					tblrow2.addView(displayTravelDetails);
					
					TableRow tblrow3= new TableRow(this);
					tblrow3.setPadding(5, 0, 0, 0);
					
					
					TextView displayStartTime =new TextView(this);
					displayStartTime.setLayoutParams(paramsForDisplayTime);
					displayStartTime.setTextSize(12);
					displayStartTime.setTextColor(textColor);
					displayStartTime.setText(datarow.getString("TRAVELTIME") + "/"+ datarow.getString("TRAVELMODE"));
					
					tblrow3.addView(displayStartTime);
					
					TextView dumyBox= new TextView(this);
					dumyBox.setLayoutParams(paramsForDumyBox);
					dumyBox.setBackgroundResource(R.drawable.textview_boxstyle);
					dumyBox.setTextColor(Color.WHITE);
					dumyBox.setGravity(Gravity.CENTER);
					dumyBox.setWidth(40);
					dumyBox.setText(datarow.getString("NOOFPASSENGER"));
					tblrow3.addView(dumyBox);
								
					TableRow tblrow4= new TableRow(this);
					tblrow4.setPadding(5, 0, 0, 0);
										
					
					
					Button btnDeleteTravel = new Button(this);
					btnDeleteTravel.setWidth(16);
					btnDeleteTravel.setHeight(22);
					btnDeleteTravel.setOnClickListener(onDeleteAction);
					btnDeleteTravel.setTag(Integer.toString(currentUserTravelID));
					btnDeleteTravel.setBackgroundResource(R.drawable.deleteicon_style);
					btnDeleteTravel.setEnabled(false);
					
									
					Button btnPhone = new Button(this);
					btnPhone.setLayoutParams(paramsForIcons);
					btnPhone.setWidth(16);
					btnPhone.setHeight(22);
					btnPhone.setOnClickListener(onShowMobileAction);
					btnPhone.setBackgroundResource(R.drawable.phone_icon_style);
					btnPhone.setEnabled(false);
					
					Button btnConfirm = new Button(this);
					btnConfirm.setLayoutParams(paramsForIcons);
					btnConfirm.setWidth(16);
					btnConfirm.setHeight(22);
					btnConfirm.setOnClickListener(onConfirmAction);
					btnConfirm.setBackgroundResource(R.drawable.confirm_icon_style);
					btnConfirm.setEnabled(false);
					
					Button btnAlreadyConfirmed = new Button(this);
					btnAlreadyConfirmed.setLayoutParams(paramsForIcons);
					btnAlreadyConfirmed.setWidth(16);
					btnAlreadyConfirmed.setHeight(22);
					btnAlreadyConfirmed.setBackgroundResource(R.drawable.alreadyconfirmed_icon_style);
					btnAlreadyConfirmed.setEnabled(false);
					
					
		
					// TODO To be check whether it is effective to use setTag method for passing object
					if(datarow.getInt("ISSELFPLAN")==1)
					{
						if(isTravelAlreadyconfirmedByCurrentUser ==0 && !datarow.isNull("ISCONFIRMED"))
						{
							isTravelAlreadyconfirmedByCurrentUser =datarow.getInt("ISCONFIRMED");
						}
						currentUserTravelID=datarow.getInt("TRAVELID");
						btnDeleteTravel.setEnabled(true);
					}
					else
					{
						
						if( !datarow.isNull("CONFIRMEDTO"))
						{
							btnPhone.setTag(userDto.getContactNo());
							btnPhone.setEnabled(true);
						}
						else
						{
							int isUserConfirmTravel =0;
							if(!datarow.isNull("ISCONFIRMED"))
							{
								isUserConfirmTravel =datarow.getInt("ISCONFIRMED");
								
							}
							if(isTravelAlreadyconfirmedByCurrentUser ==1 || isUserConfirmTravel ==1 )
							{
								
								if(isUserConfirmTravel ==1)
								{
									btnAlreadyConfirmed.setEnabled(true);
								}
								else
								{
									//btnDeleteTravel.setEnabled(false);
								}
								
							}
							else
							{
								btnConfirm.setTag(userDto);
								btnConfirm.setEnabled(true);
							}
						}
						
					}
					tblrow4.addView(btnDeleteTravel);
					tblrow4.addView(btnPhone);
					tblrow4.addView(btnConfirm);
					tblrow4.addView(btnAlreadyConfirmed);
					
					
					tblTravelDetails.addView(tblrow1);
					tblTravelDetails.addView(tblrow2);
					tblTravelDetails.addView(tblrow3);
					tblTravelDetails.addView(tblrow4);
					tblParentTravelDetails.addView(tblTravelDetails,i);
				}
			}
		}
		catch(Exception ex)
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
			final String mobileNoToCall = strMobileNo.substring(lenght -10, lenght);
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
			    			reqParameters.put("CONUSERID", LaunchActivity.getUserId());
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
		    			reqParameters.put("CONUSERID", LaunchActivity.getUserId());
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
		TableRow tableRow2 =(TableRow)findViewById(R.id.ErrorRowOnTravelListPage);
		if(tableRow2 !=null)
		{
			TextView lblError =(TextView)findViewById(R.id.lblLocErrorMsgOnTravelListPage);
			if(lblError != null)
			{
				tableRow2.setVisibility(visibility);
				lblError.setText(errorResId);
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		Intent startMain = new Intent(Intent.ACTION_MAIN);      
        startMain.addCategory(Intent.CATEGORY_HOME);                        
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);          
        startActivity(startMain); 
	}
	
}
