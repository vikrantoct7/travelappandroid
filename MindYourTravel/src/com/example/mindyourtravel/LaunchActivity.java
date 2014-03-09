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
import android.widget.TextView;

public class LaunchActivity extends Activity {

	public static XmlDataRepository repository=null;
	public static int loginUserId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		try {
			repository = new XmlDataRepository();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		final Button btnSingUp =(Button)findViewById(R.id.btnSingUp);
		btnSingUp.setOnClickListener(onClickBtnSingUp);
		final Button btnLoginSubmit =(Button)findViewById(R.id.btnLoginSubmit);
		btnLoginSubmit.setOnClickListener(onClickBtnLogin);
		
		SetErrorLabelVisibility(View.INVISIBLE,R.string.lblError);
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
			
			final  TextView txtUserName = (TextView)findViewById(R.id.txtLogin);
			final  TextView txtPassword = (TextView)findViewById(R.id.txtPassword);
			
			try
			{
				JSONObject reqParameters= new JSONObject();
				reqParameters.put("ULOGIN", txtUserName.getText());
				reqParameters.put("UPASSWORD", txtPassword.getText());
				
				JsonHandler jsonHandler =JsonHandler.getInstance();
				String url=jsonHandler.getFullUrl("UserLogin.php");
				JSONObject result = jsonHandler.PostJsonDataToServer(url, reqParameters);
				String resultCode= result.getString("RESULT");
				
				if(resultCode.contentEquals(AppConstant.PHPResponse_KO))
				{
					String errorCode=result.getString("ERRORCODE");
					if(errorCode.contentEquals(AppConstant.PHP_ERROR_CODE.NOTEXISTS))
					{
						SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorUserNotExist);
					}
					else if(errorCode.contentEquals(AppConstant.PHP_ERROR_CODE.TECHNICAL))
					{
						SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
					}
				}
				else
				{
					JSONObject jsonData =result.getJSONObject("USERDATA");
					UserDTO userDto = new UserDTO();
					loginUserId = jsonData.getInt("USERID");
					userDto.setUserId(loginUserId);
					userDto.setFirstName(jsonData.getString("UFNAME"));
					userDto.setLastName(jsonData.getString("ULNAME"));
					userDto.setGender(jsonData.getInt("GENDER"));
					userDto.setAge(jsonData.getInt("AGE"));
					userDto.setContactNo(jsonData.getString("UCONTACTNO"));
					userDto.setAppLoginUser(true);
					repository.AddUserDTO(userDto);
					Intent intent = new Intent(view.getContext(),HomePageActivity.class);
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
	};
	
	private void SetErrorLabelVisibility(int visibility,int errorResId)
	{
		TextView lblError =(TextView)findViewById(R.id.lblLoginErrorMsg);
		if(lblError != null)
		{
			lblError.setVisibility(visibility);
			lblError.setText(errorResId);
		}
	}

}
