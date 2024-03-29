package saavy.brothers.LetsGoo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

@SuppressLint("NewApi")
public final class JsonHandler {
	
	private static final JsonHandler _INSTANCE= new JsonHandler();
	
	//Private constructor to declare single ton class
	private JsonHandler()
	{
	
	}
	
	public boolean checkInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected =activeNetwork != null && activeNetwork.isConnected();
		//isConnected=false;
	    return isConnected;
     }
	
	public static JsonHandler getInstance() {
		return _INSTANCE;
	}
	//Method to send the data in json format and receive response in json format
	public JSONObject postJsonDataToServer(String url,JSONObject requestParameters,Context _context) throws JSONException, IOException
	{
		JSONObject jsonResultObject =null;
		if(!checkInternetConnection(_context))
			throw new ConnectException("Internent connection Error");
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpRequest = new HttpPost(url);
		try
		{
			httpRequest.setHeader("json",requestParameters.toString());
			StringEntity s = new StringEntity(requestParameters.toString());
			s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			httpRequest.setEntity(s);
			HttpResponse response = httpClient.execute(httpRequest);
			String jsonResult = inputStreamToString(response.getEntity().getContent());
			jsonResultObject = new JSONObject(jsonResult);
		}
		catch (JSONException e) 
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

		return jsonResultObject;
	}
	
	//Method to send the request as basic name value pair and receive response in json format
	@SuppressWarnings({"unused"})
	private JSONObject postValuePairDataToServer(String url, ArrayList<NameValuePair> requestParameters) throws JSONException, IOException
	{
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpRequest = new HttpPost(url);
		JSONObject jsonResultObject =null;
		try
		{
			httpRequest.setEntity(new UrlEncodedFormEntity(requestParameters));
			HttpResponse response = httpClient.execute(httpRequest);
			String jsonResult = inputStreamToString(response.getEntity().getContent());
			jsonResultObject = new JSONObject(jsonResult);
		}
		catch (JSONException e) 
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
		return jsonResultObject;
	}
		
	private String inputStreamToString(InputStream is) throws IOException 
	{         
		String rLine = "";         
		StringBuilder answer = new StringBuilder();         
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));                    
		try 
		{          
			while ((rLine = rd.readLine()) != null) 
			{           
				answer.append(rLine);            
			}         
		}                    
		catch (IOException e) 
		{             
			//e.printStackTrace();
			throw e;
		}         
		return answer.toString();        
	} 
	
	public String getFullUrl(String relativePage)
	{
		//return "http://192.168.0.100:90/" +relativePage;
		//return "http://letsgetout.esy.es/" +relativePage;
		return "http://ec2-54-173-11-27.compute-1.amazonaws.com/php/" +relativePage;
	}

}
