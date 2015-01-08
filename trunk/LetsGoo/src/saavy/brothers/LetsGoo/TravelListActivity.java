package saavy.brothers.LetsGoo;

import java.io.IOException;
import java.net.ConnectException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import saavy.brothers.LetsGoo.R;
import saavy.brothers.LetsGoo.R.id;

import android.R.color;
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
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
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
		/*final Button btnNewPlan = (Button)findViewById(R.id.btnNewTravelPlan);
		btnNewPlan.setOnClickListener(onClickNewPlan);
		if(jsonData.length()>0)
		{
			btnNewPlan.setEnabled(false);
		}
		else
		{
			btnNewPlan.setEnabled(true);
		}*/
		setErrorLabelVisibility(View.INVISIBLE,R.string.lblErrorTechnical);
		try
		{
			TableLayout tblParentTravelDetails = (TableLayout) findViewById(R.id.tblParentTravelDetails);
			tblParentTravelDetails.removeAllViews();
			
			//TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

			
			@SuppressWarnings("deprecation")
			TableLayout.LayoutParams tblparams = new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT,1);
			tblparams.setMargins(0, 5, 0, 0);
			
			
			@SuppressWarnings("deprecation")
			TableRow.LayoutParams txtViewParams = new TableRow.LayoutParams(0,TableRow.LayoutParams.FILL_PARENT,1);
			
			@SuppressWarnings("deprecation")
			TableRow.LayoutParams paramsForFirstRow = new TableRow.LayoutParams();
			paramsForFirstRow.span=2;
			paramsForFirstRow.weight=(float).85;
			
			@SuppressWarnings("deprecation")
			TableRow.LayoutParams paramsForActionRow = new TableRow.LayoutParams();
			paramsForActionRow.weight=(float).70;
			

			
			int textColor = Color.parseColor("#568536");
			int tableBackGroundColor=Color.parseColor("#FDFFF8");
			int isTravelAlreadyconfirmedByCurrentUser=0;
			int textSize=14;
			if(jsonData.length()==0)
			{
				TableLayout tblTravelDetails = new TableLayout(this);
				tblTravelDetails.setLayoutParams(tblparams);
				tblTravelDetails.setBackgroundColor(tableBackGroundColor);
				TableRow tblrow1= new TableRow(this);
				tblrow1.setPadding(5, 5, 0, 5);
				TextView displayNoRecordMsg =new TextView(this);
				displayNoRecordMsg.setLayoutParams(txtViewParams);
				displayNoRecordMsg.setTypeface(Typeface.DEFAULT_BOLD);
				displayNoRecordMsg.setTextSize(textSize);
				displayNoRecordMsg.setTextColor(textColor);
				displayNoRecordMsg.setText(R.string.lblNoTravellerFoundMsg);
				tblrow1.addView(displayNoRecordMsg);
				
				
				
				TableRow tblrow2= new TableRow(this);
				tblrow2.setPadding(0, 5, 0, 0);
				Button btnNewTravelPlan = new Button(this);
				btnNewTravelPlan.setBackgroundColor(Color.parseColor("#10BE5A"));
				btnNewTravelPlan.setTypeface(Typeface.DEFAULT_BOLD);
				btnNewTravelPlan.setTextSize(14);
				btnNewTravelPlan.setGravity(Gravity.CENTER);
				btnNewTravelPlan.setTextColor(Color.parseColor("#FFFFFF"));
				btnNewTravelPlan.setText(R.string.btnNewTravelPlan);
				btnNewTravelPlan.setOnClickListener(onClickNewPlan);
				btnNewTravelPlan.setHeight(33);							
				btnNewTravelPlan.setLayoutParams(txtViewParams);
				
				tblrow2.addView(btnNewTravelPlan);
				
				tblTravelDetails.addView(tblrow1);
				tblTravelDetails.addView(tblrow2);
				tblParentTravelDetails.addView(tblTravelDetails,0);
			}
			else
			{
				for (int i=0;i<jsonData.length();i++ ) 
				{
					
					TableLayout tblTravelDetails = new TableLayout(this);
					tblTravelDetails.setLayoutParams(tblparams);
					tblTravelDetails.setBackgroundColor(tableBackGroundColor);
					
					
					
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
								
										
					TableRow tblrow1= new TableRow(this);
					tblrow1.setPadding(4, 1, 2, 0);
										
					TextView displayUserDetails =new TextView(this);
					displayUserDetails.setTypeface(Typeface.DEFAULT_BOLD);
					displayUserDetails.setTextSize(textSize);
					displayUserDetails.setTextColor(textColor);
					displayUserDetails.setLayoutParams(paramsForFirstRow);
					
					String age = Integer.toString(userDto.getAge());
					String userDetail = userDto.getUserFullName() + " ("  + age + "/" +userDto.getGenderStringValue()+")";
					displayUserDetails.setText(userDetail);
					
					TextView dumyBox= new TextView(this);
					dumyBox.setBackgroundResource(R.drawable.textview_boxstyle);
					dumyBox.setTextColor(Color.WHITE);
					dumyBox.setGravity(Gravity.CENTER);
					dumyBox.setWidth(22);
					dumyBox.setText(datarow.getString("NOOFPASSENGER"));
					
					
					tblrow1.addView(displayUserDetails);
					tblrow1.addView(dumyBox);
					
					TableRow tblrow2= new TableRow(this);
					tblrow2.setPadding(4, 0, 2, 0);
					
					
					TextView displayTravelDetails =new TextView(this);
					displayTravelDetails.setTextSize(textSize);
					displayTravelDetails.setTextColor(textColor);
					displayTravelDetails.setLayoutParams(txtViewParams);
					String startFrom ="";
					if(datarow.getString("STARTLOCATION").length()>0)
					{
						startFrom=" ("+datarow.getString("STARTLOCATION")+")";
					}
					displayTravelDetails.setText(datarow.getString("CURRLOCATION") + startFrom+" to " +datarow.getString("ENDLOCATION"));
					
					tblrow2.addView(displayTravelDetails);
					
					TableRow tblrow3= new TableRow(this);
					tblrow3.setPadding(4, 0, 2, 2);
					
					
					TextView displayStartTime =new TextView(this);
					displayStartTime.setTextSize(12);
					displayStartTime.setTextColor(textColor);
					displayStartTime.setText(datarow.getString("TRAVELTIME") + "/"+ datarow.getString("TRAVELMODE"));
					//displayStartTime.setLayoutParams(txtViewParams);
					
					
										
					Button actionbuttonBox= new Button(this);
					actionbuttonBox.setBackgroundResource(R.drawable.actionbutton_statecontroller);
					actionbuttonBox.setTextSize(9);
					actionbuttonBox.setTextColor(Color.parseColor("#2D3121"));
					actionbuttonBox.setGravity(Gravity.CENTER_VERTICAL);
					actionbuttonBox.setWidth(70);
					actionbuttonBox.setHeight(25);			
					// TODO To be check whether it is effective to use setTag method for passing object
					if(datarow.getInt("ISSELFPLAN")==1)
					{
						if(isTravelAlreadyconfirmedByCurrentUser ==0 && !datarow.isNull("ISCONFIRMED"))
						{
							isTravelAlreadyconfirmedByCurrentUser =datarow.getInt("ISCONFIRMED");
						}
						currentUserTravelID=datarow.getInt("TRAVELID");
						//btnDisplayOnLeft.setOnClickListener(onDeleteAction);
						//btnDisplayOnLeft.setBackgroundResource(R.drawable.deleteicon_style);
						//btnDisplayOnLeft.setTag(Integer.toString(currentUserTravelID));
						actionbuttonBox.setTag(Integer.toString(currentUserTravelID));
						actionbuttonBox.setOnClickListener(onDeleteAction);
						actionbuttonBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.delete_icon,0,0,0);
						actionbuttonBox.setText(R.string.btnDeleteTravel);
					}
					else
					{
						
						if( !datarow.isNull("CONFIRMEDTO"))
						{
							//btnDisplayOnLeft.setOnClickListener(onShowMobileAction);
							//btnDisplayOnLeft.setBackgroundResource(R.drawable.phone_icon_style);
							//btnDisplayOnLeft.setTag(userDto.getContactNo());
							actionbuttonBox.setOnClickListener(onShowMobileAction);
							actionbuttonBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.phonecall_icon,0,0,0);
							actionbuttonBox.setText(R.string.btnShowMobileNo);
							actionbuttonBox.setTag(userDto.getContactNo());
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
									//btnDisplayOnLeft.setBackgroundResource(R.drawable.alreadyconfirmed_icon_style);
									actionbuttonBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.alreadyconfirmed_icon,0,0,0);
									actionbuttonBox.setText(R.string.lblTravelConfirmed);
								}
								else
								{
									//btnDeleteTravel.setEnabled(false);
								}
								
							}
							else
							{
								//btnDisplayOnLeft.setOnClickListener(onConfirmAction);
								//btnDisplayOnLeft.setBackgroundResource(R.drawable.confirm_icon_style);
								//btnDisplayOnLeft.setTag(userDto);
								actionbuttonBox.setOnClickListener(onConfirmAction);
								actionbuttonBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.confirm_icon,0,0,0);
								actionbuttonBox.setText(R.string.titleConfirmBox);
								actionbuttonBox.setTag(userDto);
							}
						}
						
					}
					
					displayStartTime.setLayoutParams(paramsForActionRow);
					tblrow3.addView(displayStartTime);
					
					
					
					
					tblrow3.addView(actionbuttonBox);
					
					
					tblTravelDetails.addView(tblrow1);
					tblTravelDetails.addView(tblrow2);
					tblTravelDetails.addView(tblrow3);
					
					
									
					//wrapperRow.setGravity(Gravity.CENTER);
					//wrapperRow.addView(btnDisplayOnLeft);
					//wrapperRow.addView(tblTravelDetails);
					
					
					//tblWrapper.addView(wrapperRow);
					
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
			TextView lblError =(TextView)findViewById(R.id.lblLocErrorMsgOnTravelListPage);
			if(lblError != null)
			{
				lblError.setVisibility(visibility);
				lblError.setText(errorResId);
			}
	}
	
	@Override
	public void onBackPressed() {
		ActivityHelper.turnGPSOff(this);
		Intent startMain = new Intent(Intent.ACTION_MAIN);      
        startMain.addCategory(Intent.CATEGORY_HOME);                        
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);          
        startActivity(startMain); 
	}
	
}
