package com.example.LetsGO;

import com.example.LetsGO.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ActivityHelper.setApplicationTitle(this.getWindow());
		
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
	
	private OnClickListener onClickBtnSingUp = new OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			Intent intent = new Intent(view.getContext(),RegisterActivity.class);
			startActivity(intent);
		}
	};
	
	private OnClickListener onClickBtnLogin = new OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			final  TextView txtUserName = (TextView)findViewById(R.id.txtLogin);
			try {
				
				//if(txtUserName.getText().length()!= 10)
				//{
				//	txtUserName.setError("Use 10 digit mobile number.");
				//}
				//else
				//{
					String errorCode = ActivityHelper.checkLogin(txtUserName.getText().toString());
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
						Intent intent = new Intent(view.getContext(),TravelListActivity.class);
						startActivity(intent);
					}
				//}
			} 
			catch (Exception e) 
			{
				setErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
			}
			
			
		}
	};
	
	private void setErrorLabelVisibility(int visibility,int errorResId)
	{
		TextView lblError =(TextView)findViewById(R.id.lblLoginErrorMsg);
		if(lblError != null)
		{
			lblError.setVisibility(visibility);
			lblError.setText(errorResId);
		}
	}
	
	@Override
	public void onBackPressed() {
	    // do nothing.
	}
}
