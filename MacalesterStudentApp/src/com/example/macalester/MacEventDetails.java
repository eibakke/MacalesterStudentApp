package com.example.macalester;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


/**
 * This is a simple activity that is started when you click an event item from the Events screen.
 * It shows you the description of the event item which is gotten from the xml text from the mac
 * events rss feed. The description looks weird because we did not figure out how to parse things
 * inside a CDATA section. It wouldn't let us do it even though the DocumentBuilderFactory 
 * (in AsyncGetDocFromURL, in MainActivity) is set to coalesce.
 * 
 * 
 * @author Dariush, Eivind and Haroon
 *
 */

public class MacEventDetails extends Activity {
	
	TextView eventDescription;
	
	String descriptionText;
	
	/*
	 * Overriden method that is called when the Activity is created. It takes the bundle you start the
	 * activity with and gets the text to put in the TextView from that bundle.
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mac_event_details);
		
		Intent startingIntent = getIntent();
		
		Bundle b = startingIntent.getBundleExtra("android.intent.extra.INTENT");
		
		eventDescription = (TextView) findViewById(R.id.eventdescription);
		
		descriptionText = b.getString("eventDescription");
		
		eventDescription.setText(descriptionText);
	}
}
