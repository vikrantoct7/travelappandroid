package com.example.mindyourtravel;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LaunchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		final Button btnSingUp =(Button)findViewById(R.id.btnSingUp);
		btnSingUp.setOnClickListener(onClickBtnSingUp);
		final Button btnLogin =(Button)findViewById(R.id.btnLoginBack);
		btnLogin.setOnClickListener(onClickBtnLogin);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.launch, menu);
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
			Intent intent = new Intent(view.getContext(),RegisterActivity.class);
			
			startActivity(intent);
		}
	};

}
