package saavy.brothers.LetsGoo;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import saavy.brothers.LetsGoo.AppConstant.PHP_ERROR_CODE;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public final class ActivityHelper {

	public static void turnGPSOn(Context context)
    {
		try
		{
			 String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		     if(!provider.contains("gps")){ //if gps is disabled
		         final Intent poke = new Intent();
		         poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
		         poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
		         poke.setData(Uri.parse("3")); 
		         context.sendBroadcast(poke);
		     }
		}
		catch(Exception e)
		{
	         Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
	         intent.putExtra("enabled", true);
	         context.sendBroadcast(intent);
		}
    }
	    
    public static void turnGPSOff(Context context)
    {
    	try
		{
	    	String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
	        if(provider.contains("gps")){ //if gps is enabled
	            final Intent poke = new Intent();
	            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
	            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	            poke.setData(Uri.parse("3")); 
	            context.sendBroadcast(poke);
	        }
		}
		catch(Exception e)
		{
			Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
	        intent.putExtra("enabled", false);
	        context.sendBroadcast(intent);
		}
    }
	public static String checkLogin(String userMobileNo,Context _context) throws Exception
	{
		String errorCode="";
		try
		{
			JSONObject reqParameters= new JSONObject();
			reqParameters.put("UMOBILENO", userMobileNo);
			
			JsonHandler jsonHandler =JsonHandler.getInstance();
			String url=jsonHandler.getFullUrl("UserLogin.php");
			JSONObject result = jsonHandler.postJsonDataToServer(url, reqParameters,_context);
			if(result !=null)
			{
				String resultCode= result.getString("RESULT");
				
				if(resultCode.contentEquals(AppConstant.PHPRESPONSE_KO))
				{
					errorCode=result.getString("ERRORCODE");
					
				}
				else
				{
					JSONObject jsonData =result.getJSONObject("USERDATA");
					UserDTO userDto = new UserDTO();
					LaunchActivity.loginUserId = jsonData.getInt("USERID");
					userDto.setUserId(LaunchActivity.loginUserId);
					userDto.setFirstName(jsonData.getString("UFNAME"));
					userDto.setLastName(jsonData.getString("ULNAME"));
					userDto.setCityId(jsonData.getInt("CITYID"));
					userDto.setGender(jsonData.getInt("GENDER"));
					userDto.setAge(jsonData.getInt("AGE"));
					userDto.setContactNo(jsonData.getString("UCONTACTNO"));
					userDto.setUserCity(jsonData.getString("USERCITY"));
					userDto.setAppLoginUser(true);
					LaunchActivity.repository.addUserDTO(userDto);
				}
			}
			else
			{
				errorCode=PHP_ERROR_CODE.TECHNICAL;
			}
		}
		catch(JSONException e)
		{
			//e.printStackTrace();
			throw e;
		}
		catch (ClientProtocolException e)
		{    
			//e.printStackTrace();
			throw e;
		}    
		catch (IOException e) 
		{    
			//e.printStackTrace();
			throw e;
		} 
		catch(Exception e)
		{
			//e.printStackTrace();
			throw e;
		}
		return errorCode;
	}
	
	public static String getUserMobileNo(Context context)
	{
		TelephonyManager tMgr =(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); 
		String phoneNo=tMgr.getLine1Number();
		if(phoneNo !=null)
		{
			if(phoneNo.length()>10)
			{
				phoneNo=phoneNo.substring(phoneNo.length() -10, phoneNo.length());
			}
		}
		else
		{
			phoneNo="";
		}
		return phoneNo;
		//Phone mPhone = PhoneFactory.getDefaultPhone();
		//return mPhone.getLine1Number();
	}
	
	public static void setApplicationTitle(Window window)
	{
		//setTitleColor(window);
		setTitleAlignment(window);
	}
	public static void setTitleColor(Window window)
	{
		View title = window.findViewById(android.R.id.title);
		View titleBar = (View) title.getParent();
		titleBar.setBackgroundColor(Color.parseColor("#365F91"));
	}
	
	public static void setTitleAlignment(Window window)
	{
		ViewGroup decorView= (ViewGroup) window.getDecorView();
		LinearLayout root= (LinearLayout) decorView.getChildAt(0);
		if(root.getChildAt(0).getClass() ==FrameLayout.class ) 
		{
			FrameLayout titleContainer= (FrameLayout) root.getChildAt(0);
			TextView title= (TextView) titleContainer.getChildAt(0);
			title.setGravity(Gravity.CENTER);
		}
		else if(root.getChildAt(1).getClass() ==FrameLayout.class ) 
		{
			FrameLayout titleContainer= (FrameLayout) root.getChildAt(1);
			if(titleContainer.getChildAt(0).getClass()==RelativeLayout.class)
			{
				RelativeLayout relativeLayout=(RelativeLayout)titleContainer.getChildAt(0);
				if(relativeLayout.getChildAt(0).getClass()==TextView.class)
				{
					TextView title= (TextView) relativeLayout.getChildAt(0);
					title.setGravity(Gravity.CENTER);
				}
			}
			else if(titleContainer.getChildAt(0).getClass()==TextView.class)
			{
				TextView title= (TextView) titleContainer.getChildAt(0);
				title.setGravity(Gravity.CENTER);
			}
		}
	
	}
}
