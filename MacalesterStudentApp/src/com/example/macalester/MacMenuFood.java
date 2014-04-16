package com.example.macalester;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

/**
 * This Activity is started when you click a day item in the Mac Menu background list.
 * It works very much like the MacEventsDetail and the MacBuilding activities, in that it is passed information in
 * the bundle and extracts this and sets it to be shown in the TextViews.
 * 
 * 
 * @author Dariush, Eivind and Haroon
 *
 */

public class MacMenuFood extends Activity {
	
	TextView food;
	TextView day;
	String menuDay;
	String foodText;
	
	/**
	 * This method is called when the activity is started, and gets text from the bundle b in the intent and assigns these texts
	 * to the TextViews. It also sets a MovementMethod which allows you to scroll down the menu of the day. The menu looks weird 
	 * because we did not figure out how to parse things
	 * inside a CDATA section. It wouldn't let us do it even though the DocumentBuilderFactory 
	 * (in AsyncGetDocFromURL, in MainActivity) is set to coalesce.
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mac_menu_food);
		
		Intent startingIntent = getIntent();
		
		Bundle b = startingIntent.getBundleExtra("android.intent.extra.INTENT");
		
		day = (TextView) findViewById(R.id.day);
		food = (TextView) findViewById(R.id.food);
		
		menuDay = b.getString("day") + "\n";
		foodText = b.getString("foodItems");
		
		day.setText(menuDay);
		food.setMovementMethod(new ScrollingMovementMethod());
		food.setText(foodText);
	}
}
