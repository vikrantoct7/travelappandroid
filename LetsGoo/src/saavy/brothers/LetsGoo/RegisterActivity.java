package saavy.brothers.LetsGoo;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import saavy.brothers.LetsGoo.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.text.*;
import android.text.method.LinkMovementMethod;



public class RegisterActivity extends Activity {
	String User_City_hint="Select city";
	Handler myHandler=null ;
	ProgressBar registerProgressBar =null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityHelper.turnGPSOn(this);
		setContentView(R.layout.activity_register);
		ActivityHelper.setApplicationTitle(getWindow());
		myHandler= new Handler(Looper.getMainLooper());
		setErrorLabelVisibility(View.INVISIBLE,R.string.lblError);
		
		addFocusChangeListernerOnEditText();
		final Button btnSubmit = (Button)findViewById(R.id.btnRegister);
		btnSubmit.setOnClickListener(addRegisterButtonListener);
		// create class object
		GPSTracker gps = new GPSTracker(this);
		String userGpsLocationCity="";
        // check if GPS enabled     
        if(gps.canGetLocation())
        {
        	userGpsLocationCity=gps.getCurrentCity();
        	gps.stopUsingGPS();
       	}else
       	{
    		// can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
       	}
		try
		{
			JSONObject reqParameters= new JSONObject();
			JsonHandler jsonHandler =JsonHandler.getInstance();
			String url=jsonHandler.getFullUrl("CityDataAdapter.php");
			JSONObject result = jsonHandler.postJsonDataToServer(url, reqParameters,this);
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
					
					JSONArray cityData =result.getJSONArray("CITYDATA");
					ArrayList<String> cityAdapeterData = new ArrayList<String>();
					
					if(userGpsLocationCity.length()>0)
					{
						if(!cityData.toString().toLowerCase().contains(userGpsLocationCity.toLowerCase()))
						{
							cityAdapeterData.add(userGpsLocationCity);
						}
					}
					for (int i=0;i<cityData.length();i++ ) 
					{
						JSONObject row= cityData.getJSONObject(i);
						cityAdapeterData.add(row.getString("CITY"));
					}
					
					HintAdapter adapterCity = new HintAdapter(this, android.R.layout.simple_spinner_item,cityAdapeterData);
					adapterCity.setDropDownViewResource(R.layout.activity_settings_spinner_item);
					adapterCity.add(User_City_hint);
					final Spinner ddCity=(Spinner)findViewById(R.id.ddCity);
					ddCity.setAdapter(adapterCity);
					int gpsCitySelectionIndex=adapterCity.getCount();
					if(userGpsLocationCity.length()>0)
					{
						gpsCitySelectionIndex =adapterCity.getPosition(userGpsLocationCity);
					}
					ddCity.setSelection(gpsCitySelectionIndex);
					
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
		try
		{
			String mPhoneNumber =  ActivityHelper.getUserMobileNo(this);
			final EditText txtPhNo= (EditText)findViewById(R.id.txtPhNo);
			txtPhNo.setText(mPhoneNumber);
			if(mPhoneNumber.length()> 0)
			{
				txtPhNo.setEnabled(false);
			}
		}
		catch(Exception ex)
		{
			setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
		}
		
		//Place hyper link
	    TextView link = (TextView) findViewById(R.id.lblCondition);
	    link.setText(Html.fromHtml("I have read and agree to <a href=\"TermsAndCondition://TermsAndCondition\">terms and condition</a> for using this app."));
	    link.setMovementMethod(LinkMovementMethod.getInstance());
	    
  
	}
	
	
	private OnClickListener addRegisterButtonListener = new OnClickListener()
	{
			@Override
			public void onClick(View view) {
				
				if(registerProgressBar==null)
				{
					registerProgressBar = (ProgressBar)findViewById(R.id.RegisterProgressBar);
				}
				registerProgressBar.setVisibility(View.VISIBLE);
				
				
				
				final TextView txtFName= (TextView)findViewById(R.id.txtFName);
				final TextView txtLName= (TextView)findViewById(R.id.txtLName);
				final TextView txtPhNo= (TextView)findViewById(R.id.txtPhNo);
				final TextView txtAge =(TextView)findViewById(R.id.txtAge);
				final  Spinner  ddcity =(Spinner)findViewById(R.id.ddCity);
				GenericValidator validator = new GenericValidator();
				boolean validationResult=true;
				validationResult= validator.validate(txtFName);
				validationResult =validationResult && validator.validate(txtLName);
				validationResult= validationResult && validator.validate(txtPhNo);
				validationResult= validationResult && validator.validate(txtAge);
				
				if(ddcity.getSelectedItem()==User_City_hint)
				{
					View v= ddcity.getSelectedView();
					TextView temp= (TextView)v.findViewById(android.R.id.text1);
					temp.setError("Please select city...");
					validationResult=false;
				}
				if(txtAge.getText().length()> 0)
				{
					int age = Integer.parseInt(txtAge.getText().toString());
					if(age < 18 || age>70)
					{
						txtAge.setError("This age is not consider by Application.");
						validationResult=false;
					}
				}
				
				if(txtPhNo.getText().length()!= 10)
				{
					txtPhNo.setError("Use 10 digit mobile number.");
					validationResult=false;
				}
				
				RadioGroup rdoGender = (RadioGroup) findViewById(R.id.rdoGender);
				// get selected radio button from radioGroup                        
				RadioButton selectedRdoOption =((RadioButton) findViewById(rdoGender.getCheckedRadioButtonId()))  ;
				if(selectedRdoOption ==null)
				{
				// find the radiobutton by returned id      
					validationResult=false;
					((RadioButton) findViewById(R.id.rdoFemale)).setError("This is mandatory");
					return ;
				}
				
				int genderValue=0;
				if(selectedRdoOption.getId() ==R.id.rdoMale  )
				{
					genderValue =1;
				}
				else if(selectedRdoOption.getId() ==R.id.rdoFemale)
				{
					genderValue =2;
				}
								
				if(validationResult)
				{
					final int finalgenderValue=genderValue;
					new Thread(new Runnable() {             
						public void run() {
					
					try
					{
						JSONObject reqParameters= new JSONObject();
						reqParameters.put("UFNAME", txtFName.getText());
						reqParameters.put("ULNAME", txtLName.getText());
						reqParameters.put("CITY", ddcity.getSelectedItem());
						reqParameters.put("GENDER", finalgenderValue);
						reqParameters.put("AGE", txtAge.getText());
						reqParameters.put("UCONTACTNO", txtPhNo.getText());
						JsonHandler jsonHandler =JsonHandler.getInstance();
						String url=jsonHandler.getFullUrl("UserRegisteration.php");
						JSONObject result = jsonHandler.postJsonDataToServer(url, reqParameters,RegisterActivity.this);
						if(result !=null)
						{
							String resultCode= result.getString("RESULT");
							if(resultCode.contentEquals(AppConstant.PHPRESPONSE_KO))
							{
								String errorCode=result.getString("ERRORCODE");
								if(errorCode.contentEquals(AppConstant.PHP_ERROR_CODE.ALREADYEXISTS))
								{
									setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorUserExist);
								}
								else if(errorCode.contentEquals(AppConstant.PHP_ERROR_CODE.TECHNICAL))
								{
									setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
								}
							}
							else
							{
								JSONObject jsonData =result.getJSONObject("USERDATA");
								UserDTO userDto = new UserDTO();
								LaunchActivity.loginUserId = jsonData.getInt("USERID");
								userDto.setUserId(LaunchActivity.loginUserId);
								userDto.setFirstName(jsonData.getString("UFNAME"));
								userDto.setLastName(jsonData.getString("ULNAME"));
								userDto.setGender(jsonData.getInt("GENDER"));
								userDto.setAge(jsonData.getInt("AGE"));
								userDto.setContactNo(jsonData.getString("UCONTACTNO"));
								userDto.setUserCity(jsonData.getString("USERCITY"));
								userDto.setAppLoginUser(true);
								LaunchActivity.repository.addUserDTO(userDto);
								Intent intent = new Intent(RegisterActivity.this,TravelListActivity.class);
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
					}).start();	
				}
				else
				{
					setErrorLabelVisibility(View.VISIBLE,R.string.ErrorMandatoryMsg);
				}
					
			}
	};
	
	private void addFocusChangeListernerOnEditText()
	{
		final TextView txtFName= (TextView)findViewById(R.id.txtFName);
		txtFName.setOnFocusChangeListener(new TextViewValidator(txtFName));
		final TextView txtLName= (TextView)findViewById(R.id.txtLName);
		txtLName.setOnFocusChangeListener(new TextViewValidator(txtLName));
		final TextView txtAge= (TextView)findViewById(R.id.txtAge);
		txtAge.setOnFocusChangeListener(new TextViewValidator(txtAge));
		final TextView txtPhNo= (TextView)findViewById(R.id.txtPhNo);
		txtPhNo.setOnFocusChangeListener(new TextViewValidator(txtPhNo));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}
	
	private void setErrorLabelVisibility(int visibility,int errorResId)
	{
		final int finalvisibility=visibility;
		final int finalerrorResId=errorResId;
		
		myHandler.post(new Runnable(){
			public void run() {
				if(registerProgressBar==null)
				{
					registerProgressBar = (ProgressBar)findViewById(R.id.RegisterProgressBar);
				}
				registerProgressBar.setVisibility(View.INVISIBLE);
				
		
				TableRow tableRow2 =(TableRow)findViewById(R.id.ErrorRowOnRegisterPage);
				if(tableRow2 !=null)
				{
					TextView lblError =(TextView)findViewById(R.id.lblErrorMsgOnRegisterPage);
					if(lblError != null)
					{
						tableRow2.setVisibility(finalvisibility);
						lblError.setText(finalerrorResId);
					}
				}
			}
		});
	}
}
