package saavy.brothers.LetsGoo;

import saavy.brothers.LetsGoo.R;

import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.widget.TextView;

public class TermsAndCondition extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_terms_and_condition);
		ActivityHelper.setApplicationTitle(getWindow());
		
		TextView link = (TextView) findViewById(R.id.feedbackurl);
	    link.setText(Html.fromHtml("<br>CONTACT US: <br><a href='https://www.facebook.com/pages/LetsGoo-Android-Based-Mobile-App/685251354855265'>www.facebook.com/LetsGoo</a><br><font color='#6B71FF'><u>saavy.brothers@gmail.com</u></font>"));
	    link.setMovementMethod(LinkMovementMethod.getInstance());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.terms_and_condition, menu);
		return true;
	}

}
