package com.example.macalester;

import android.app.Activity;
import android.os.Bundle;

/**
 * This is a map activity which shows a google map of campus, specified in activity_mac_map.xml
 * 
 * 
 * @author Dariush, Eivind and Haroon
 *
 */

public class MacMap extends Activity {
	
	/**
	 * This activity is called when the Activity is created and sets its content to be as specified in
	 * activity_mac_map.xml.
	 */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mac_map);
    }
}