package saavy.brothers.LetsGoo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class LetsGooTitleBar extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lets_goo_title_bar);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lets_goo_title_bar, menu);
		return true;
	}

}
