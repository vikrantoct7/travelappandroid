package com.example.mindyourtravel;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
//import android.widget.RadioGroup;
import android.widget.EditText;

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
				final EditText txtFName= (EditText)findViewById(R.id.txtFName);
				final EditText txtLName= (EditText)findViewById(R.id.txtLName);
				final EditText txtUserName= (EditText)findViewById(R.id.txtUserName);
				final EditText txtPassword= (EditText)findViewById(R.id.txtPassword);
				final EditText txtPhNo= (EditText)findViewById(R.id.txtPhNo);
				TextViewValidator validator = new TextViewValidator();
				boolean validationResult;
				validationResult= validator.validate(txtFName);
				validationResult =validationResult && validator.validate(txtLName);
				validationResult =validationResult && validator.validate(txtUserName);
				validationResult =validationResult && validator.validate(txtPassword);
				validationResult= validationResult && validator.validate(txtPhNo);
				if(validationResult)
				{
					
				}
			}
	};
	
	private void addFocusChangeListernerOnEditText()
	{
		final EditText txtFName= (EditText)findViewById(R.id.txtFName);
		txtFName.setOnFocusChangeListener(new TextViewValidator(txtFName));
		final EditText txtLName= (EditText)findViewById(R.id.txtLName);
		txtLName.setOnFocusChangeListener(new TextViewValidator(txtLName));
		final EditText txtUserName= (EditText)findViewById(R.id.txtUserName);
		txtUserName.setOnFocusChangeListener(new TextViewValidator(txtUserName));
		final EditText txtPassword= (EditText)findViewById(R.id.txtPassword);
		txtPassword.setOnFocusChangeListener(new TextViewValidator(txtPassword));
		final EditText txtPhNo= (EditText)findViewById(R.id.txtPhNo);
		txtPhNo.setOnFocusChangeListener(new TextViewValidator(txtPhNo));
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
