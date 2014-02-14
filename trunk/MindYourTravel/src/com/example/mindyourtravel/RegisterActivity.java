package com.example.mindyourtravel;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
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
		addFocusChangeListernerOnEditText();
		final Button btnSubmit = (Button)findViewById(R.id.btnRegister);
		btnSubmit.setOnClickListener(addRegisterButtonListener);
		
		//addListnerOnRadioButton();
	}
	
	private OnClickListener addRegisterButtonListener = new OnClickListener()
	{
			@Override
			public void onClick(View v) {
				final TextView txtFName= (TextView)findViewById(R.id.txtFName);
				final TextView txtLName= (TextView)findViewById(R.id.txtLName);
				final TextView txtUserName= (TextView)findViewById(R.id.txtUserName);
				final TextView txtPassword= (TextView)findViewById(R.id.txtPassword);
				final TextView txtPhNo= (TextView)findViewById(R.id.txtPhNo);
				final TextView txtAge =(TextView)findViewById(R.id.txtAge);
				GenericValidator validator = new GenericValidator();
				boolean validationResult=true;
				/*validationResult= validator.validate(txtFName);
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
				}*/
				
				if(validationResult)
				{
					try
					{
						JSONObject reqParameters= new JSONObject();
						/*reqParameters.put("UFNAME", txtFName.getText());
						reqParameters.put("ULNAME", txtLName.getText());
						reqParameters.put("ULOGIN", txtUserName.getText());
						reqParameters.put("GENDER", selectedRdoOption.isChecked());
						reqParameters.put("AGE", txtAge.getText());
						reqParameters.put("UPASSWORD", txtPassword.getText());
						reqParameters.put("UCONTACTNO", txtPhNo.getText());*/
						reqParameters.put("UFNAME", "Vikrant");
						reqParameters.put("ULNAME", "Jain");
						reqParameters.put("ULOGIN", "vikrant");
						reqParameters.put("GENDER", 1);
						reqParameters.put("AGE", "34");
						reqParameters.put("UPASSWORD", "ppppp");
						reqParameters.put("UCONTACTNO", "9891109568");
						JsonHandler jsonHandler =JsonHandler.getInstance();
						String url=jsonHandler.getFullUrl("UserRegisteration.php");
						JSONObject result = jsonHandler.PostJsonDataToServer(url, reqParameters);
						String error= result.getString("result");
						error=error;
					}
					catch(JSONException ex)
					{
						ex.printStackTrace();
					}
				}
			}
	};
	
	private void addFocusChangeListernerOnEditText()
	{
		/*final TextView txtFName= (TextView)findViewById(R.id.txtFName);
		txtFName.setOnFocusChangeListener(new TextViewValidator(txtFName));
		final TextView txtLName= (TextView)findViewById(R.id.txtLName);
		txtLName.setOnFocusChangeListener(new TextViewValidator(txtLName));
		final TextView txtUserName= (TextView)findViewById(R.id.txtUserName);
		txtUserName.setOnFocusChangeListener(new TextViewValidator(txtUserName));
		final TextView txtPassword= (TextView)findViewById(R.id.txtPassword);
		txtPassword.setOnFocusChangeListener(new TextViewValidator(txtPassword));
		final TextView txtPhNo= (TextView)findViewById(R.id.txtPhNo);
		txtPhNo.setOnFocusChangeListener(new TextViewValidator(txtPhNo));*/
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}
	
	/*public void addListenerOnButton() 
	{                         
		radioSexGroup = (RadioGroup) findViewById(R.id.radioGender);                
		btnDisplay = (Button) findViewById(R.id.btnDisplay);                         
		btnDisplay.setOnClickListener(new OnClickListener() {                             
		@Override                    
		public void onClick(View v) {                                 
			// get selected radio button from radioGroup                        
			int selectedId = radioSexGroup.getCheckedRadioButtonId();                                 
			// find the radiobutton by returned id                        
			radioSexButton = (RadioButton) findViewById(selectedId);                                 
             
		}                         
		});          
		
	}*/
	

}
