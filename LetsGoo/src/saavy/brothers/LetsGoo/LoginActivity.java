package saavy.brothers.LetsGoo;

import java.net.ConnectException;

import saavy.brothers.LetsGoo.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

public class LoginActivity extends Activity {
	View activityView=null;
	ProgressBar loginProgressBar =null;
	Handler myHandler=null ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login);
			
		ActivityHelper.setApplicationTitle(this.getWindow());
		myHandler= new Handler(Looper.getMainLooper());
		
		final Button btnSingUp =(Button)findViewById(R.id.btnSingUp);
		btnSingUp.setOnClickListener(onClickBtnSingUp);
		final Button btnLoginSubmit =(Button)findViewById(R.id.btnLoginSubmit);
		btnLoginSubmit.setOnClickListener(onClickBtnLogin);
		
		String mPhoneNumber = ActivityHelper.getUserMobileNo(this);
		final  TextView txtUserName = (TextView)findViewById(R.id.txtLogin);
		txtUserName.setText(mPhoneNumber);
		
		setErrorLabelVisibility(View.INVISIBLE,R.string.lblError);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	@Override
	protected void onRestart()
	{
		super.onRestart();
		loginProgressBar = (ProgressBar)findViewById(R.id.loginProgressBar);
		loginProgressBar.setVisibility(View.INVISIBLE);
	}
	private OnClickListener onClickBtnSingUp = new OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			if(loginProgressBar ==null)
			{
				loginProgressBar = (ProgressBar)findViewById(R.id.loginProgressBar);
			}
			loginProgressBar.setVisibility(View.VISIBLE);

			myHandler.post(new Runnable() {             
				public void run() {
					Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
					startActivity(intent);
				}
			});
		}
	};
	
	//This is sample for asynchronous processing
	class LaunchRegisterActivity extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() 
		{
			loginProgressBar = (ProgressBar)findViewById(R.id.loginProgressBar);
			loginProgressBar.setVisibility(View.VISIBLE);
		}
		@Override
		protected Void doInBackground(Void... params) {
			Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
			startActivity(intent);
			return null;
		}
		@Override
	    protected void onPostExecute(Void result) 
	    {

	    }

		
	}
	
	private OnClickListener onClickBtnLogin = new OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			activityView=view;
			
			if(loginProgressBar ==null)
			{
				loginProgressBar = (ProgressBar)findViewById(R.id.loginProgressBar);
			}
			loginProgressBar.setVisibility(View.VISIBLE);
			new Thread(new Runnable() {             
				public void run() {
					final  TextView txtUserName = (TextView)findViewById(R.id.txtLogin);
					try {
						String errorCode = ActivityHelper.checkLogin(txtUserName.getText().toString(),activityView.getContext());
						if(errorCode.length()> 0)
						{
							
							if(errorCode.contentEquals(AppConstant.PHP_ERROR_CODE.NOTEXISTS))
							{
									setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorUserNotExist);
							}
							else if(errorCode.contentEquals(AppConstant.PHP_ERROR_CODE.TECHNICAL))
							{
									setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
							}
						}
						else
						{
							Intent intent = new Intent(activityView.getContext(),TravelListActivity.class);
							startActivity(intent);
						}
					} 
					catch(ConnectException ie)
					{
							setErrorLabelVisibility(View.VISIBLE,R.string.InternetConnectiivityErrorMsg);
					}
					catch (Exception e) 
					{
							setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
					}
					
					//myHandler.post(UpdateCheckLogin);
			}
			}).start();
			
		}
	};
	
	private void setErrorLabelVisibility(int visibility,int errorResId)
	{
		final int finalvisibility=visibility;
		final int finalerrorResId=errorResId;
		myHandler.post(new Runnable(){
			public void run() {
				if(loginProgressBar ==null)
				{
					loginProgressBar = (ProgressBar)findViewById(R.id.loginProgressBar);
				}
				loginProgressBar.setVisibility(View.INVISIBLE);
				TableRow tableRow2 =(TableRow)findViewById(R.id.ErrorRowOnLoginPage);
				if(tableRow2 !=null)
				{
					TextView lblError =(TextView)findViewById(R.id.lblLoginErrorMsgOnLoginPage);
					if(lblError != null)
					{
						tableRow2.setVisibility(finalvisibility);
						lblError.setText(finalerrorResId);
					}
				}
			}
		});
	}
		
	/*private Runnable UpdateCheckLogin = new Runnable() {
		@Override
		public void run() {
			
		}
	};*/
	
	
	@Override
	public void onBackPressed() {
		Intent startMain = new Intent(Intent.ACTION_MAIN);      
        startMain.addCategory(Intent.CATEGORY_HOME);                        
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);          
        startActivity(startMain); 
	}
	/*@Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);           
    }
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_MENU) {
        	ActivityHelper.turnGPSOff(this);
        }
        return super.onKeyDown(keyCode, event);
    }*/
}
