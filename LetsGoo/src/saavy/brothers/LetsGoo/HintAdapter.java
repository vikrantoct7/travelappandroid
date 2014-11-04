package saavy.brothers.LetsGoo;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ArrayAdapter;

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
}