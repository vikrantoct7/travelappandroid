package saavy.brothers.LetsGoo;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class HintAdapter extends ArrayAdapter<String> 
{     
	public HintAdapter(Context theContext, int resource,ArrayList<String> objects) 
	{    
		super(theContext, resource,  objects);    
	}
	
	public HintAdapter(Context theContext, int resource,String[] objects) 
	{    
		super(theContext, resource,  objects);    
	}     
	@Override    
	public int getCount() 
	{        
		// don't display last item. It is used as hint.        
		int count = super.getCount();        
		return count > 0 ? count - 1 : count;    
	}
	
	@Override  
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View v = super.getView(position, convertView, parent);
		int itemCount= getCount();
        if (position ==itemCount) {
        	TextView temp= (TextView)v.findViewById(android.R.id.text1);
        	temp.setHintTextColor(Color.parseColor("#949694"));
        	temp.setText("");
        	temp.setHint(getItem(itemCount)); //"Hint to be displayed"
        }
        return v;
	}
}