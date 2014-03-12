package com.example.mindyourtravel;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.telephony.TelephonyManager;


public final class ActivityHelper {

	public static void CheckLogin(String userMobileNo) throws Exception
	{
		try
		{
			JSONObject reqParameters= new JSONObject();
			//reqParameters.put("ULOGIN", txtUserName.getText());
			//reqParameters.put("UPASSWORD", txtPassword.getText());
			reqParameters.put("UMOBILENO", userMobileNo);
			
			JsonHandler jsonHandler =JsonHandler.getInstance();
			String url=jsonHandler.getFullUrl("UserLogin.php");
			JSONObject result = jsonHandler.PostJsonDataToServer(url, reqParameters);
			String resultCode= result.getString("RESULT");
			
			if(resultCode.contentEquals(AppConstant.PHPResponse_KO))
			{
				String errorCode=result.getString("ERRORCODE");
				/*if(errorCode.contentEquals(AppConstant.PHP_ERROR_CODE.NOTEXISTS))
				{
					SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorUserNotExist);
				}
				else*/ 
				if(errorCode.contentEquals(AppConstant.PHP_ERROR_CODE.TECHNICAL))
				{
					throw new Exception("Technical error in PHP code");
				}
			}
			else
			{
				JSONObject jsonData =result.getJSONObject("USERDATA");
				UserDTO userDto = new UserDTO();
				LaunchActivity.loginUserId = jsonData.getInt("USERID");
				userDto.setUserId(LaunchActivity.loginUserId);
				userDto.setFirstName(jsonData.getString("UFNAME"));
				userDto.setLastName(jsonData.getString("ULNAME"));
				userDto.setGender(jsonData.getInt("GENDER"));
				userDto.setAge(jsonData.getInt("AGE"));
				userDto.setContactNo(jsonData.getString("UCONTACTNO"));
				userDto.setAppLoginUser(true);
				LaunchActivity.repository.AddUserDTO(userDto);
				
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
			throw e;
		}
		catch (ClientProtocolException e)
		{    
			e.printStackTrace();
			throw e;
		}    
		catch (IOException e) 
		{    
			e.printStackTrace();
			throw e;
		} 
		catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	
	public static String getUserMobileNo(Context context)
	{
		TelephonyManager tMgr =(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); 
		return tMgr.getLine1Number();
	}
}
