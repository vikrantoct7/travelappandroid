package com.example.LetsGO; 


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.mindyourtravel.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class LocationLocatorActivity extends Activity {

	AutoCompleteTextView atvPlaces;
	PlacesTask placesTask;
	ParserTask parserTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_locator);
		ActivityHelper.setApplicationTitle(this.getWindow());
		
		Bundle extras  = getIntent().getExtras();
		
		//final TextView lblLocPosition =(TextView) findViewById(R.id.lblLocPosition);
		//lblLocPosition.setText(extras.getString("LOCPOSITION"));
		
		//final TextView lblPersistPosition =(TextView) findViewById(R.id.lblPersistPosition);
		//lblPersistPosition.setText(extras.getString("PERSISTPOSITION"));
		
		SetErrorLabelVisibility(View.INVISIBLE,R.string.lblError);
		
		final Button btnLocationOK =(Button)findViewById(R.id.btnLocationOK);
		btnLocationOK.setOnClickListener(onClickbtnLocationOK);
		
		
		atvPlaces = (AutoCompleteTextView) findViewById(R.id.atv_places);
		atvPlaces.setThreshold(3);	
		final PlacesAutoCompleteAdapter adapter = new PlacesAutoCompleteAdapter(this,android.R.layout.simple_list_item_1);
		atvPlaces.setAdapter(adapter);
		atvPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HashMap<String, String> hm = (HashMap<String, String>) adapter.getItem(position);
				if(hm !=null)
				{
					TextView lblSelectedLoc =(TextView)findViewById(R.id.lblSelectedSubLocality);
					lblSelectedLoc.setText(hm.get("sublocality"));
					
					TextView lblSelectedLocality =(TextView)findViewById(R.id.lblSelectedLocality);
					lblSelectedLocality.setText(hm.get("locality"));
				}
			}
	    });
		
	}

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
	
	// Fetches all places from GooglePlaces AutoComplete Web Service
	public class PlacesTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... place){
			// For storing data from web service
			String data = "";
			
			// Obtain browser key from https://code.google.com/apis/console
			String key = "key=AIzaSyCfdXATlz7jtM6MEvy9Xh_3_g_Ivc5ysXE";
			
			String input="";
			
			try {
				input = "input=" + URLEncoder.encode(place[0], "utf-8");
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
	
			try{
				// Fetching the data from web service in background
				data = downloadUrl(url);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return data;		
		}
		
		@Override
		protected void onPostExecute(String result) {			
			super.onPostExecute(result);
			
			// Creating ParserTask
			parserTask = new ParserTask();
			
			// Starting Parsing the JSON string returned by Web Service
			parserTask.execute(result);
		}		
	}
	
	
	/** A class to parse the Google Places in JSON format */
	public class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

    	JSONObject jObject;
    	
		@Override
		protected List<HashMap<String, String>> doInBackground(String... jsonData) {			
			
			List<HashMap<String, String>> places = null;
			
            PlaceJSONParserHashMap placeJsonParser = new PlaceJSONParserHashMap();
            
            try{
            	jObject = new JSONObject(jsonData[0]);
            	
            	// Getting the parsed data as a List construct
            	places = placeJsonParser.parse(jObject);

            }catch(Exception e){
            	e.printStackTrace();
            	
            }
            return places;
		}
		
		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {			
			
				String[] from = new String[] { "description"};
				int[] to = new int[] { android.R.id.text1 };
				
				// Creating a SimpleAdapter for the AutoCompleteTextView			
				final SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);
				//final ArrayAdapter<HashMap<String, String>> adapter = new ArrayAdapter<HashMap<String, String>>(getBaseContext(),  android.R.layout.simple_list_item_1,result);
				
				// Setting the adapter
				atvPlaces.setAdapter(adapter);
				
				
				atvPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						@SuppressWarnings("unchecked")
						HashMap<String, String> hm = (HashMap<String, String>) adapter.getItem(position);
						if(hm !=null)
						{
							TextView lblSelectedLoc =(TextView)findViewById(R.id.lblSelectedSubLocality);
							lblSelectedLoc.setText(hm.get("sublocality"));
							
							TextView lblSelectedLocality =(TextView)findViewById(R.id.lblSelectedLocality);
							lblSelectedLocality.setText(hm.get("locality"));
						}
					}
			    });
		}			
    	
    }   
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location_locator, menu);
		return true;
	}


	private OnClickListener onClickbtnLocationOK = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			TextView lblSelectedLoc =(TextView)findViewById(R.id.lblSelectedSubLocality);
			TextView lblSelectedLocality =(TextView)findViewById(R.id.lblSelectedLocality);
			if(lblSelectedLoc.getText().length()>0)
			{
				try
            	{
	            	JSONObject reqParameters= new JSONObject();
	    			reqParameters.put("LOCALITYNAME", lblSelectedLoc.getText());
	    			reqParameters.put("CITYNAME", lblSelectedLocality.getText());
	    			JsonHandler jsonHandler =JsonHandler.getInstance();
	    			String url=jsonHandler.getFullUrl("LocalityDataAdapter.php");
	    			JSONObject result = jsonHandler.PostJsonDataToServer(url, reqParameters);
	    			String resultCode= result.getString("RESULT");
	    			if(resultCode.contentEquals(AppConstant.PHPResponse_KO))
	    			{
	    				String errorCode=result.getString("ERRORCODE");
	    				
	    				if(errorCode.contentEquals(AppConstant.PHP_ERROR_CODE.ALREADYEXISTS))
	    				{
	    					GoToTravelPlanActivity(v, lblSelectedLoc.getText(),
									lblSelectedLocality.getText());
	    				}
	    			}
	    			else
	    			{
	    				GoToTravelPlanActivity(v, lblSelectedLoc.getText(),
								lblSelectedLocality.getText());
	    			}
	            }
		    	catch(JSONException ex)
		    		{
		    			SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
		    		}
	            	catch (IOException e) 
	        		{    
	        			SetErrorLabelVisibility(View.VISIBLE,R.string.lblErrorTechnical);
	        		} 
			}
		}

	};
	
	private void GoToTravelPlanActivity(View v, CharSequence lblSelectedLoc,
			CharSequence lblSelectedLocality) {
		//final TextView lblLocPosition =(TextView) findViewById(R.id.lblLocPosition);
		//final TextView lblPersistPosition =(TextView) findViewById(R.id.lblPersistPosition);
	
		Intent intent = new Intent(v.getContext(),TravelPlanActivity.class);
		intent.putExtra("SELLOCALITY", lblSelectedLoc);
		intent.putExtra("SELCITY", lblSelectedLocality);
		//intent.putExtra("LOCPOSITION", lblLocPosition.getText());
		//intent.putExtra("PERSISTPOSITION", lblPersistPosition.getText());
		startActivity(intent);
	}
	private void SetErrorLabelVisibility(int visibility,int errorResId)
	{
		TextView lblError =(TextView)findViewById(R.id.lblLocErrorMsg);
		if(lblError != null)
		{
			lblError.setVisibility(visibility);
			lblError.setText(errorResId);
		}
	}
}
