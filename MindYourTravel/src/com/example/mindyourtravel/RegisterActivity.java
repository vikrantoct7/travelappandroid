package com.example.mindyourtravel;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
	
	private OnClickListener addRegisterButtonListener = new OnClickListener()
	{
			@Override
			public void onClick(View v) {
				final TextView txtFName= (TextView)findViewById(R.id.txtFName);
				final TextView txtLName= (TextView)findViewById(R.id.txtLName);
				final TextView txtUserName= (TextView)findViewById(R.id.txtUserName);
				final TextView txtPassword= (TextView)findViewById(R.id.txtUserPassword);
				final TextView txtPhNo= (TextView)findViewById(R.id.txtPhNo);
				final TextView txtAge =(TextView)findViewById(R.id.txtAge);
				GenericValidator validator = new GenericValidator();
				boolean validationResult=true;
				validationResult= validator.validate(txtFName);
				validationResult =validationResult && validator.validate(txtLName);
				validationResult =validationResult && validator.validate(txtUserName);
				validationResult =validationResult && validator.validate(txtPassword);
				validationResult= validationResult && validator.validate(txtPhNo);
				RadioGroup rdoGender = (RadioGroup) findViewById(R.id.rdoGender);
				// get selected radio button from radioGroup                        
				RadioButton selectedRdoOption =((RadioButton) findViewById(rdoGender.getCheckedRadioButtonId()))  ;
				if(selectedRdoOption ==null)
				{
				// find the radiobutton by returned id                        
					validationResult=false;
					((RadioButton) findViewById(R.id.rdoFemale)).setError("This is mandatory");
				}
				
				if(validationResult)
				{
					try
					{
						JSONObject reqParameters= new JSONObject();
						reqParameters.put("UFNAME", txtFName.getText());
						reqParameters.put("ULNAME", txtLName.getText());
						reqParameters.put("ULOGIN", txtUserName.getText());
						reqParameters.put("GENDER", selectedRdoOption.isChecked());
						reqParameters.put("AGE", txtAge.getText());
						reqParameters.put("UPASSWORD", txtPassword.getText());
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
							Intent intent = new Intent(v.getContext(),RegConfimationActivity.class);
							intent.putExtra("USERNAME", txtUserName.getText().toString());
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
		final TextView txtUserName= (TextView)findViewById(R.id.txtUserName);
		txtUserName.setOnFocusChangeListener(new TextViewValidator(txtUserName));
		final TextView txtPassword= (TextView)findViewById(R.id.txtUserPassword);
		txtPassword.setOnFocusChangeListener(new TextViewValidator(txtPassword));
		final TextView txtPhNo= (TextView)findViewById(R.id.txtPhNo);
		txtPhNo.setOnFocusChangeListener(new TextViewValidator(txtPhNo));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}
	
}
