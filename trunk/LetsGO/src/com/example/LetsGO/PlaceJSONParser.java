package com.example.LetsGO;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PlaceJSONParser {
	
	/** Receives a JSONObject and returns a list */
	public ArrayList<HashMap<String, String>> parse(JSONObject jObject){
	
		
		JSONArray jPlaces = null;
		try {			
			/** Retrieves all the elements in the 'places' array */
			jPlaces = jObject.getJSONArray("predictions");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		/** Invoking getPlaces with the array of json object
		 * where each json object represent a place
		 */		
		return getPlaces(jPlaces);
	}
	
	
	private ArrayList<HashMap<String, String>> getPlaces(JSONArray jPlaces){
		int placesCount = jPlaces.length();
		ArrayList<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>(placesCount);
		HashMap<String, String> place = null;	

		/** Taking each place, parses and adds to list object */
		for(int i=0; i<placesCount;i++){
			try {
				/** Call getPlace with place JSON object to parse the place */
				place = getPlace((JSONObject)jPlaces.get(i));
				placesList.add(place);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return placesList;
	}
	
	/** Parsing the Place JSON object */
	private HashMap<String, String> getPlace(JSONObject jPlace){

		HashMap<String, String> place = new HashMap<String, String>();
		
		String id="";
		String reference="";
		String description="";		
		
		try {
			
			description = jPlace.getString("description");			
			id = jPlace.getString("id");
			reference = jPlace.getString("reference");
			
			place.put("description", description);
			place.put("_id",id);
			place.put("reference",reference);
			//JSONArray mtypes = jPlace.getJSONArray("types");
			JSONArray terms = jPlace.getJSONArray("terms");
			if(terms.length()>2)
			{
				JSONObject subLocalityObject = terms.getJSONObject(0);
				place.put("sublocality", subLocalityObject.getString("value"));
				JSONObject localityObject = terms.getJSONObject(terms.length()-3);
				place.put("locality", localityObject.getString("value"));
				/*String subLocalityType = mtypes.getString(0);
				if(subLocalityType.equalsIgnoreCase("sublocality"))
				{
					JSONObject subLocalityObject = terms.getJSONObject(0);
					place.put("sublocality", subLocalityObject.getString("value"));
					JSONObject localityObject = terms.getJSONObject(terms.length()-3);
					place.put("locality", localityObject.getString("value"));
				}
				else if(subLocalityType.equalsIgnoreCase("sublocality_level_1")
						|| subLocalityType.equalsIgnoreCase("sublocality_level_2")
						||subLocalityType.equalsIgnoreCase("neighborhood"))
				{
						JSONObject subLocalityObject = terms.getJSONObject(0);
						place.put("sublocality", subLocalityObject.getString("value"));
						JSONObject localityObject = terms.getJSONObject(terms.length()-3);
						place.put("locality", localityObject.getString("value"));
				}
				else if(subLocalityType.equalsIgnoreCase("subway_station")
						|| subLocalityType.equalsIgnoreCase("bus_station")
						|| subLocalityType.equalsIgnoreCase("train_station"))
				{
					JSONObject subLocalityObject = terms.getJSONObject(0);
					place.put("sublocality", subLocalityObject.getString("value"));
					JSONObject localityObject = terms.getJSONObject(terms.length()-3);
					place.put("locality", localityObject.getString("value"));
				}
				else if(subLocalityType.equalsIgnoreCase("locality")
						||subLocalityType.equalsIgnoreCase("administrative_area_level_1"))
				{
					JSONObject subLocalityObject = terms.getJSONObject(0);
					place.put("sublocality", subLocalityObject.getString("value"));
					JSONObject localityObject = terms.getJSONObject(0);
					place.put("locality", localityObject.getString("value"));
				}*/
			}
			
			
		} catch (JSONException e) {			
			e.printStackTrace();
		}		
		return place;
	}
}
