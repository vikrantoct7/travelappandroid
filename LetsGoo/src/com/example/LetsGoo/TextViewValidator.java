package com.example.LetsGoo;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.TextView;


public class TextViewValidator extends EditTextValidator implements OnFocusChangeListener{

	public TextViewValidator(TextView textView) {
		super(textView);
		// TODO Auto-generated constructor stub
	}

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
		
	@Override
	public void onFocusChange(View textView, boolean hasFocus) {
		// TODO Auto-generated method stub
		if(!hasFocus)
		{
			validate((TextView)textView);
		}
	}

}
