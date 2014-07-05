package com.example.LetsGO;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class PlacesAutoCompleteAdapter extends ArrayAdapter<HashMap<String, String>> implements Filterable 
{    
	private ArrayList<HashMap<String, String>> resultList;    
	public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) 
	{        
		super(context, textViewResourceId);    
	}
    @Override    
    public int getCount() 
    {        return resultList.size();    
    }    
    @Override    
    public HashMap<String, String> getItem(int index) 
    {        
    	return resultList.get(index);    
    }
   
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	View view = convertView;
        if(view == null) {
            view = new TextView(getContext());
        }
        
        HashMap<String, String> place= getItem(position);
        if(place !=null)
        {
        	((TextView)view).setText(place.get("description"));
        }
        return view;

    } 
   
    @Override    
    public Filter getFilter() 
    {        
    	Filter filter = new Filter() 
    	{            
    		@Override            
    		protected FilterResults performFiltering(CharSequence constraint) 
    		{                
    			FilterResults filterResults = new FilterResults();
    			if (constraint != null) 
    			{                    
    				AutoCompleteUtils autoCompleteUtils= new AutoCompleteUtils();
    				// Retrieve the autocomplete results.                    
    				resultList = autoCompleteUtils.autoComplete(constraint.toString());
    				// Assign the data to the FilterResults                    
    				filterResults.values = resultList;
    				filterResults.count = resultList.size();                
    			}                
    			return filterResults;            
    		}            
    		@Override            
    		protected void publishResults(CharSequence constraint, FilterResults results) 
    		{                
    			if (results != null && results.count > 0) 
    			{                    
    				notifyDataSetChanged();                
    			}                
    			else 
    			{                    
    				notifyDataSetInvalidated();                
    			}            
    		}
    	};        
    	return filter;    
	}
}
