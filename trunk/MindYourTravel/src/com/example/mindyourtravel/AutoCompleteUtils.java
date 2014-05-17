package com.example.mindyourtravel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
public class AutoCompleteUtils 
{    
	/** A method to download json data from url 
	 * @throws Exception */
    private String downloadUrl(String strUrl) throws Exception{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
                URL url = new URL(strUrl);                

                // Creating an http connection to communicate with url 
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url 
                urlConnection.connect();

                // Reading data from url 
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb  = new StringBuffer();

                String line = "";
                while( ( line = br.readLine())  != null){
                        sb.append(line);
                }
                
                data = sb.toString();

                br.close();

        }catch(Exception e){
                throw e;
        }finally{
                iStream.close();
                urlConnection.disconnect();
        }
        return data;
    }	
    

	protected ArrayList<HashMap<String, String>> autoComplete (String place){
		// For storing data from web service
		String data = "";
		ArrayList<HashMap<String, String>> places =null;
		// Obtain browser key from https://code.google.com/apis/console
		String key = "key=AIzaSyCfdXATlz7jtM6MEvy9Xh_3_g_Ivc5ysXE";
		
		String input="";
		
		try {
			input = "input=" + URLEncoder.encode(place, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}		
		
		
		// place type to be searched
		String types = "types=geocode";
		
		// Sensor enabled
		String sensor = "sensor=false";			
		
		// Building the parameters to the web service
		String parameters = input+"&"+types+"&"+sensor+"&"+key;
		
		// Output format
		String output = "json";
		
		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;
		
		PlaceJSONParser placeJsonParser = new PlaceJSONParser();

		try{
			// Fetching the data from web service in background
			data = downloadUrl(url);
			
			
			
			JSONObject jObject = new JSONObject(data);
        	
        	// Getting the parsed data as a List construct
        	places = placeJsonParser.parse(jObject);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return places;		
	}
    
	/*private static final String LOG_TAG = "HomeDevApp";
	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON ="/json";
	public final static ArrayList<String> autoComplete (String input)
	{        
		ArrayList<String> resultList = null;
		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try{
			StringBuilder sb = new StringBuilder(PLACES_API_BASE+TYPE_AUTOCOMPLETE+OUT_JSON);
			sb.append("?sensor=false&key=" + ApplicationProperties.getApiKey());
			sb.append("&components=country:ru");
			sb.append("&input=Omsk,%20"+ URLEncoder.encode(input, "utf8"));
			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			int read;            char[] buff= new char[1024];
			while ((read = in.read(buff)) != -1)
			{                
				jsonResults.append(buff, 0, read);            
			}        
		}   catch (MalformedURLException e)
		{            
			return resultList;        
		}  
		catch (IOException e)
		{            
			return resultList;        
		}        
		try{            
			JSONObject jsonObject = new JSONObject(jsonResults.toString());
			JSONArray predsJsonArray = jsonObject.getJSONArray("predictions");
			resultList = new ArrayList<String>(predsJsonArray.length());
			for (int i=0; i<predsJsonArray.length(); i++)
			{
				resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
				
			}        
		} 
		catch (JSONException e)
		{

		}       
		return resultList;    
		}*/
	}