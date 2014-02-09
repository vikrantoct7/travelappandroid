package com.example.mindyourtravel;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

public abstract class EditTextValidator implements TextWatcher {

	private TextView textView;
	
	public EditTextValidator()
	{
		
	}
	
	public EditTextValidator(TextView textView) {
        this.textView = textView;
    }
	
	public abstract boolean validate(TextView textView);

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
        validate(textView);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

}
