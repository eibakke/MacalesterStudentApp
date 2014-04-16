package com.example.macalester;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

/**
 * About activity that shows pictures and text about us, the developers
 * 
 * 
 * @author Dariush, Eivind and Haroon
 *
 */

public class About extends Activity {
	
	
	/**
	 * This method is called when the activity is created. Because all the information the activity is
	 * supposed to show is in the activity_about.xml file, it just sets its content to be that.
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
	}
	
	
	/**
	 * This method makes a little button on the top left which sends you back to the MainActivity
	 */

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
