package com.example.mindyourtravel;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class RegisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		SetErrorLabelVisibility(View.INVISIBLE,R.string.lblError);
		
		addFocusChangeListernerOnEditText();
		final Button btnSubmit = (Button)findViewById(R.id.btnRegister);
		btnSubmit.setOnClickListener(addRegisterButtonListener);
		try
		{
			JSONObject reqParameters= new JSONObject();
			JsonHandler jsonHandler =JsonHandler.getInstance();
			String url=jsonHandler.getFullUrl("CityDataAdapter.php");
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
				
				JSONArray cityData =result.getJSONArray("CITYDATA");
				ArrayList<String> cityAdapeterData = new ArrayList<String>();

				for (int i=0;i<cityData.length();i++ ) 
				{
					JSONObject row= cityData.getJSONObject(i);
					cityAdapeterData.add(row.getString("CITY"));
				}
				
				ArrayAdapter<String> adapterCity = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item,cityAdapeterData);
				adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				final Spinner ddCity=(Spinner)findViewById(R.id.ddCity);
				ddCity.setAdapter(adapterCity);
				
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
		try
		{
			TelephonyManager tMgr =(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE); 
			String mPhoneNumber = tMgr.getLine1Number();
			final EditText txtPhNo= (EditText)findViewById(R.id.txtPhNo);
			txtPhNo.setText(mPhoneNumber);
			if(mPhoneNumber.length()> 0)
			{
				txtPhNo.setEnabled(false);
			}
		}
		catch(Exception ex)
		{
			SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
		}
	}
	
	
	private OnClickListener addRegisterButtonListener = new OnClickListener()
	{
			@Override
			public void onClick(View view) {
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
				
				if(txtAge.getText().length()> 0)
				{
					int age = Integer.parseInt(txtAge.getText().toString());
					if(age < 18 || age>70)
					{
						txtAge.setError("This age is not consider by Application.");
						validationResult=false;
					}
				}
				
				if(txtPhNo.getText().length()<= 10)
				{
					txtPhNo.setError("Invalid mobile number.");
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
					try
					{
						JSONObject reqParameters= new JSONObject();
						reqParameters.put("UFNAME", txtFName.getText());
						reqParameters.put("ULNAME", txtLName.getText());
						reqParameters.put("CITY", ddcity.getSelectedItem());
						reqParameters.put("GENDER", genderValue);
						reqParameters.put("AGE", txtAge.getText());
						reqParameters.put("UCONTACTNO", txtPhNo.getText());
						JsonHandler jsonHandler =JsonHandler.getInstance();
						String url=jsonHandler.getFullUrl("UserRegisteration.php");
						JSONObject result = jsonHandler.PostJsonDataToServer(url, reqParameters);
						String resultCode= result.getString("RESULT");
						if(resultCode.contentEquals(AppConstant.PHPResponse_KO))
						{
							String errorCode=result.getString("ERRORCODE");
							if(errorCode.contentEquals(AppConstant.PHP_ERROR_CODE.ALREADYEXISTS))
							{
								SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorUserExist);
							}
							else if(errorCode.contentEquals(AppConstant.PHP_ERROR_CODE.TECHNICAL))
							{
								SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
							}
						}
						else
						{
							//Intent intent = new Intent(v.getContext(),RegConfimationActivity.class);
							//intent.putExtra("USERNAME", txtUserName.getText().toString());
							//startActivity(intent);
							JSONObject jsonData =result.getJSONObject("USERDATA");
							UserDTO userDto = new UserDTO();
							LaunchActivity.loginUserId = jsonData.getInt("USERID");
							userDto.setUserId(LaunchActivity.loginUserId);
							userDto.setFirstName(jsonData.getString("UFNAME"));
							userDto.setLastName(jsonData.getString("ULNAME"));
							userDto.setGender(jsonData.getInt("GENDER"));
							userDto.setAge(jsonData.getInt("AGE"));
							userDto.setContactNo(jsonData.getString("UCONTACTNO"));
							userDto.setAppLoginUser(true);
							LaunchActivity.repository.AddUserDTO(userDto);
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
	
	private void addFocusChangeListernerOnEditText()
	{
		final TextView txtFName= (TextView)findViewById(R.id.txtFName);
		txtFName.setOnFocusChangeListener(new TextViewValidator(txtFName));
		final TextView txtLName= (TextView)findViewById(R.id.txtLName);
		txtLName.setOnFocusChangeListener(new TextViewValidator(txtLName));
		/*final TextView txtUserName= (TextView)findViewById(R.id.txtUserName);
		txtUserName.setOnFocusChangeListener(new TextViewValidator(txtUserName));
		final TextView txtPassword= (TextView)findViewById(R.id.txtUserPassword);
		txtPassword.setOnFocusChangeListener(new TextViewValidator(txtPassword));
		*/
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
	
	private void SetErrorLabelVisibility(int visibility,int errorResId)
	{
		TextView lblError =(TextView)findViewById(R.id.lblErrorMsg);
		if(lblError != null)
		{
			lblError.setVisibility(visibility);
			lblError.setText(errorResId);
		}
	}
}
