package com.example.LetsGO;
import com.example.LetsGO.R;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

@SuppressLint("NewApi")
public class LaunchActivity extends Activity {

	public static XmlDataRepository repository=null;
	public static int loginUserId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy); 
		}	
		
		ActivityHelper.setApplicationTitle(this.getWindow());
		SetErrorLabelVisibility(View.INVISIBLE,R.string.lblError);
		try 
		{	
			repository = new XmlDataRepository();
		} 
		catch (Exception e) {
			SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
		}
			
		String mPhoneNumber = ActivityHelper.getUserMobileNo(this);
		if(repository.GetUsersData() ==null)
		{
			try
			{
				if(mPhoneNumber.trim().length() > 0)
				{
					ActivityHelper.CheckLogin(mPhoneNumber);
				}
			}
			catch(Exception ex)
			{
				SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
			}
			if(repository.GetUsersData() == null)
			{
				LaunchLoginWindew();
			}
			else
			{
				LaunchHomeActivity();
			}
		}
		else
		{
			if(repository.GetUsersData().getContactNo() != mPhoneNumber)
			{
				repository.ClearRepository();
				LaunchLoginWindew();
			}
			else
			{
				LaunchHomeActivity();
			}
		}
	}
	
	private void LaunchLoginWindew()
	{
		Intent intent = new Intent(this.getApplicationContext(),LoginActivity.class);
		startActivity(intent);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.launch, menu);
		return true;
	}
	
	public void LaunchHomeActivity()
	{
		Intent intent = new Intent(this.getApplicationContext(),TravelListActivity.class);
		startActivity(intent);
	}
	
	private void SetErrorLabelVisibility(int visibility,int errorResId)
	{
		TextView lblError =(TextView)findViewById(R.id.lblLaunchErrorMsg);
		if(lblError != null)
		{
			lblError.setVisibility(visibility);
			lblError.setText(errorResId);
		}
	}

}
