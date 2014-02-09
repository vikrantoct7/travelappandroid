package com.example.mindyourtravel;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
//import android.widget.RadioGroup;

public class RegisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		//addListnerOnRadioButton();
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
