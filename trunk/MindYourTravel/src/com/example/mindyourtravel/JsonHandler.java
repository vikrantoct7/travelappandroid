package com.example.mindyourtravel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

public final class JsonHandler {
	
	private static final JsonHandler _instance= new JsonHandler();
	
	//Private constructor to declare single ton class
	private JsonHandler()
	{
		
	}
	
	public static JsonHandler getInstance() {
		return _instance;
	}
	//Method to send the data in json format and receive response in json format
	public JSONObject PostJsonDataToServer(String url,JSONObject requestParameters) throws JSONException, IOException
	{
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpRequest = new HttpPost(url);
		JSONObject jsonResultObject =null;
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
		return jsonResultObject;
	}
	
	//Method to send the request as basic name value pair and receive response in json format
	@SuppressWarnings({"unused"})
	private JSONObject PostValuePairDataToServer(String url, ArrayList<NameValuePair> requestParameters) throws JSONException, IOException
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
		return jsonResultObject;
	}
		
	private String inputStreamToString(InputStream is) 
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
			e.printStackTrace();          
		}         
		return answer.toString();        
	} 
	
	public String getFullUrl(String relativePage)
	{
		return "http://192.168.0.100:90/" +relativePage;
	}

}
