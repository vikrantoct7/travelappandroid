package saavy.brothers.LetsGoo;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Address;

public final class Geocoder 
{
	public static List<Address> getFromLocation(double latitude, double longitude, int maxResult){
		
		String address = String.format(Locale.ENGLISH,"http://maps.googleapis.com/maps/api/geocode/json?latlng=%1$f,%2$f&sensor=true&language="+Locale.getDefault().getCountry(), latitude, longitude);
		HttpGet httpGet = new HttpGet(address);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		StringBuilder stringBuilder = new StringBuilder();

		List<Address> retList = null;
		
		try {
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}
			
			JSONObject jsonObject = new JSONObject();
			jsonObject = new JSONObject(stringBuilder.toString());
			
			
			retList = new ArrayList<Address>();
			
			
			if("OK".equalsIgnoreCase(jsonObject.getString("status"))){
				JSONArray results = jsonObject.getJSONArray("results");
				for (int i=0;i<results.length() && i<=maxResult;i++ ) {
					Address addr = getAddressFromJson(results.getJSONObject(i));
					addr.setLatitude(latitude);
					addr.setLongitude(longitude);
					retList.add(addr);
				}
			}
			
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			//Log.e(MyGeocoder.class.getName(), "Error calling Google geocode webservice.", e);
		} catch (IOException e) {
			e.printStackTrace();
			//Log.e(MyGeocoder.class.getName(), "Error calling Google geocode webservice.", e);
		} catch (JSONException e) {
			e.printStackTrace();
			//Log.e(MyGeocoder.class.getName(), "Error parsing Google geocode webservice response.", e);
		}
		
		return retList;
	}
	
	private static Address getAddressFromJson(JSONObject jsonResultAddress) throws JSONException
	{
		Address addr = new Address(Locale.ITALY);
		if(jsonResultAddress != null)
		{
			//String indiStr = jsonResultAddress.getString("formatted_address");
			//addr.setAddressLine(0, indiStr);
			JSONArray jsonAddess = jsonResultAddress.getJSONArray("address_components");
			String streetAddres ="";
			String sublocality ="";
			for (int i=0;i<jsonAddess.length();i++ ) {
				JSONObject result = jsonAddess.getJSONObject(i);
				
				String longName = result.getString("long_name");
				JSONArray mtypes = result.getJSONArray("types");
				if(mtypes.length()>0)
				{
					String type = mtypes.getString(0);
					
					if(type.equalsIgnoreCase("street_number"))
					{
						streetAddres =getFormatedValue(streetAddres,longName);
					}
					else if(type.equalsIgnoreCase("route") || type.equalsIgnoreCase("neighborhood")|| type.equalsIgnoreCase("establishment"))
					{
						streetAddres =getFormatedValue(streetAddres,longName);
					}
					else if(type.equalsIgnoreCase("sublocality"))
					{
						sublocality =getFormatedValue(sublocality,longName);
					}
					else if(type.equalsIgnoreCase("locality"))
					{
						addr.setLocality(longName);
					}
					else if(type.equalsIgnoreCase("administrative_area_level_2"))
					{
						addr.setSubAdminArea(longName);
					}
					else if(type.equalsIgnoreCase("administrative_area_level_1"))
					{
						addr.setAdminArea(longName);
					}
					else if(type.equalsIgnoreCase("country"))
					{
						addr.setCountryName(longName);
					}
					else if(type.equalsIgnoreCase("postal_code"))
					{
						addr.setPostalCode(longName);
					}
				}
			}
			addr.setAddressLine(0, streetAddres);
			addr.setSubLocality(sublocality);
			
		}
		return addr;
	}
	
	private static String getFormatedValue(String existVal,String newValue)
	{
		String returnValue="";
		if(existVal.length()==0)
		{
			returnValue =newValue;
		}
		else
		{
			returnValue = existVal +" ,"+newValue;
		}
		return returnValue;
	}

}
