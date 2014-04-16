package com.example.macalester;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * This activity is started when you click an item in the building list in the main activity.
 * Works very much like the MacMenuFood and MacEventDetail in that it is passed information in
 * the bundle and extracts this and sets it to be shown in the TextViews.
 * 
 * @author Dariush, Eivind and Haroon
 *
 */

public class MacBuilding extends Activity {
	
	TextView description;
	TextView building;
	String buildingName;
	String descriptionText;

	/**
	 * Is called when the Activity is created and gets info from the bundle which has been used in creating the activity. Then sets
	 * this info to the TextViews.
	 */
	
	@Override
	protected void onCreate(Bundle buildingInfo) {
		setContentView(R.layout.activity_mac_building);
		super.onCreate(buildingInfo);
		
		Intent startingIntent = getIntent();
		
		Bundle b = startingIntent.getBundleExtra("android.intent.extra.INTENT");
		
		building = (TextView) findViewById(R.id.building);
		description = (TextView) findViewById(R.id.description);
		
		buildingName = b.getString("building") + "\n";
		descriptionText = b.getString("buildingDescription");
		
		building.setText(buildingName);
		description.setText(descriptionText);
	}
}
