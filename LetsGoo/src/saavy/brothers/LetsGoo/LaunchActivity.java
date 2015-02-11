package saavy.brothers.LetsGoo;
import java.net.ConnectException;

import saavy.brothers.LetsGoo.R;

import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressLint("NewApi")
public class LaunchActivity extends Activity {

	public static XmlDataRepository repository=null;
	public static int loginUserId;
	ProgressBar launchProgressBar =null;
	Handler myHandler=null ;
	
	public static int getUserId()
	{
		if(loginUserId == 0)
		{
			if(repository !=null)
			{
				loginUserId=repository.getUsersData().getUserId();
			}
		}
		return loginUserId;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy); 
		}
		myHandler= new Handler(Looper.getMainLooper());
		
		ActivityHelper.setApplicationTitle(this.getWindow());		
		new Thread(new Runnable() {             
			public void run() {
				BoforeLoginProcess();
			}	
		}).start();
	}
	
	private void BoforeLoginProcess()
	{
		
		SetErrorLabelVisibility(View.INVISIBLE,R.string.lblError);
		try 
		{	
			repository = new XmlDataRepository(this);
		} 
		catch (Exception e) {
			SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
		}
			
		String mPhoneNumber = ActivityHelper.getUserMobileNo(this);
		if(repository.getUsersData() ==null)
		{
			try
			{
				if(mPhoneNumber.trim().length() > 0)
				{
					ActivityHelper.checkLogin(mPhoneNumber,this);
				}
				if(repository.getUsersData() == null)
				{
					LaunchLoginWindew();
				}
				else
				{
					LaunchHomeActivity();
				}
			}
			catch(ConnectException ie)
			{
					SetErrorLabelVisibilityOnThread(View.VISIBLE,R.string.InternetConnectiivityErrorMsg);
					
			}
			catch(Exception ex)
			{
					SetErrorLabelVisibilityOnThread(View.VISIBLE,R.string.lblErrorTechnical);
			}
		}
		else
		{
			if(mPhoneNumber.trim().length() > 0 && repository.getUsersData().getContactNo() != mPhoneNumber)
			{
				repository.clearRepository();
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
	
	private void SetErrorLabelVisibilityOnThread(int visibility,int errorResId)
	{
		final int finalvisibility=visibility;
		final int finalerrorResId=errorResId;
		myHandler.post(new Runnable(){
		public void run() {
			if(launchProgressBar ==null)
			{
				launchProgressBar = (ProgressBar)findViewById(R.id.launchProgressBar);
			}
			launchProgressBar.setVisibility(View.INVISIBLE);
			TextView lblError =(TextView)findViewById(R.id.lblLaunchErrorMsg);
			if(lblError != null)
			{
				lblError.setVisibility(finalvisibility);
				lblError.setText(finalerrorResId);
			}
		}
		});
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
	
	public boolean checkInternetConnection() {
        final ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() &&    conMgr.getActiveNetworkInfo().isConnected()) {
              return true;
        } else {
              //System.out.println("Internet Connection Not Present");
            return false;
        }
     }

}
