package com.example.macalester;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * This activity contains a webView which shows a url, onCreate gets from the bundle which is passed to start the activity
 * 
 * 
 * @author Dariush, Eivind and Haroon
 *
 */
@SuppressLint("SetJavaScriptEnabled")
public class MacWeb extends Activity {
	
	private String URL;
	private WebView myWebView;
	
	/**
	 * This method is called when the activity is started. It gets a url from the Intent, and uses a WebView to load the website.
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mac_web);
		
		Intent startingIntent = getIntent();
		
		Bundle b = startingIntent.getBundleExtra("android.intent.extra.INTENT");
		
		URL = b.getString("urlToOpen");
		
		myWebView = (WebView) findViewById(R.id.webview);
		myWebView.setWebViewClient(new WebViewClient());
		myWebView.loadUrl(URL);
		
		
	}
	
	/**
	 * This method is called when you press the down key on your phone and goes back in history of the webview, if there is any. If there isn't
	 * any history, it calls the supermethod. This brings you back to MainActivity.
	 */
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
	        myWebView.goBack();
	        return true;
	    }
	    else {
	    	return super.onKeyDown(keyCode, event);
	    }
	}
}
