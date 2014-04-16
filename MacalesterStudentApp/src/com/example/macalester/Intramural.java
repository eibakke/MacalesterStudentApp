package com.example.macalester;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * This is an activity which is called when you click the Register IM Team. It shows you some
 * GUI elements where you can enter text and select what kind of team you want to make. You then
 * click the confirm registration button, which starts the LoginActivity.
 * 
 * 
 * @author Dariush, Eivind and Haroon
 *
 */
public class Intramural extends Activity {
	
	

	/**
	 * This is called when the Activity is created. Since all the GUI elements are in activity_intramural.xml
	 * it sets its content to be determined by that file.
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intramural);
	}
	
	/**
	 * This method is called when you click the confirm Registration button. It starts the LoginActivity activity.
	 * 
	 */
	public void openConfirm(View view) {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}
}
