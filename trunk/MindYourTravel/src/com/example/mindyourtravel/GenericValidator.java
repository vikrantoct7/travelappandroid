package com.example.mindyourtravel;

import android.widget.TextView;

public  class GenericValidator {
	
	public boolean validate(TextView textView)
	{
		boolean result;
		if(textView.getText().length()==0)
		{
			textView.setError("This is mandatory!");
			result=false;
		}
		else
		{
			textView.setError(null);
			result=true;
		}
		return result;
	}

}
