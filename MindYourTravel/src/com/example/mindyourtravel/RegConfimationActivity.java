package com.example.mindyourtravel;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RegConfimationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration_confimation);
		String userName = getIntent().getStringExtra("USERNAME");
		if(userName != null)
		{
			if(userName.length() != 0)
			{
				TextView lblRegMsg =(TextView)findViewById(R.id.lblRegMsg);
				String lblMsg=lblRegMsg.getText().toString();
				lblRegMsg.setText(String.format(lblMsg, userName)) ;
			}
		}
		
		final Button btnSingUp =(Button)findViewById(R.id.btnLoginBack);
		btnSingUp.setOnClickListener(onClickBtnSingUp);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration_confimation, menu);
		return true;
	}
	
	private OnClickListener onClickBtnSingUp = new OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			Intent intent = new Intent(view.getContext(),LaunchActivity.class);
			
			startActivity(intent);
		}
	};

}
