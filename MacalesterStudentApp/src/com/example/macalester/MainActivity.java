package com.example.macalester;


import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.darvds.ribbonmenu.RibbonMenuView;
import com.darvds.ribbonmenu.iRibbonMenuCallback;

/**
 * This is the MainActivity which is started when you start up the app.
 * It contains most of the GUI elements that will be used and also contains a couple of subclasses
 * which extend AsyncTask.
 * 
 * Its most important feature is the iRibbonMenuCallback which it implements. From this you can navigate to
 * all the app's functionality.
 * 
 * 
 * @author Dariush, Eivind and Haroon
 *
 */

public class MainActivity extends Activity implements iRibbonMenuCallback {
	/*
	 * Here are all the variables used in MainActivity. They are all private because they do not need to be accessed from outside the app, or by any other classes it uses.
	 */
	private RibbonMenuView rbmView;
	private ListView mainList;
	private TextView feederror;
	private TextView loadMessage;
	private ProgressBar loadWheel;
	private ImageButton playButton;
	private ImageButton stopButton;
	private Button regIMButton;
	private Button openAthleticsWebButton;
	private MediaPlayer WMCNPlayer;
	private Boolean WMCNIsStarted = false;
	private Boolean WMCNIsPlaying = false;
	private static final String MAC_MENU_RSS = "http://www.cafebonappetit.com/rss/menu/159";
	private static final String MAC_EVENTS_RSS ="http://events.macalester.edu/RSS/";
	private String currentURL;
	
	private String[] macMenuDays;
	private String[] menuOfDay;
	
	private String[] macEventTitles;
	private String[] macEventLinks;
	private String[] macEventDescriptions;
	
	/*
	 * Called when the app is created. It makes calls to initialize the GUI elements and the background list.
	 */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeUIElements();
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        new AsyncGetDocFromURL().execute(MAC_EVENTS_RSS);
    }
    
    /*
     * clearScreen() is called every time anything is clicked in the ribbonViewMenu.
     * It makes the screen clear for when new GUI elements need room.
     */
    
    private void clearScreen() {
    	mainList.setVisibility(View.INVISIBLE);
    	openAthleticsWebButton.setVisibility(View.INVISIBLE);
    	regIMButton.setVisibility(View.INVISIBLE);
    	playButton.setVisibility(View.INVISIBLE);
        stopButton.setVisibility(View.INVISIBLE);
        feederror.setVisibility(View.INVISIBLE);
        loadMessage.setVisibility(View.INVISIBLE);
        loadWheel.setVisibility(View.INVISIBLE);
    }
    
    /*
     * Here we initialize all the GUI elements and set listeners to those that need listeners.
     */
    
    private void initializeUIElements() {
    	feederror = (TextView) findViewById(R.id.feederror);
        
        loadMessage = (TextView) findViewById(R.id.loadmessage);
        loadWheel = (ProgressBar) findViewById(R.id.loadwheel);
        
        mainList = (ListView) findViewById(R.id.newslist);
        
        rbmView = (RibbonMenuView) findViewById(R.id.ribbonMenuView1);
        rbmView.setMenuClickCallback(this);
        rbmView.setMenuItems(R.menu.activity_main);
        rbmView.bringToFront();
        loadWheel.bringToFront();
        
        openAthleticsWebButton = (Button) findViewById(R.id.athletics_web_button);
    	regIMButton = (Button) findViewById(R.id.athletics_reg_im_button);
    	
    	openAthleticsWebButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				macWeb("http://m.macalester.edu/athletics");
			}
		});
    	
    	regIMButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				regIM();
			}
		});
        
        playButton = (ImageButton) findViewById(R.id.play_button);
    	stopButton = (ImageButton) findViewById(R.id.pause_button);
    	
    	stopButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				WMCNPlayer.pause();

		        stopButton.setVisibility(View.INVISIBLE);
		        playButton.setVisibility(View.VISIBLE);
		        
		        loadWheel.setVisibility(View.VISIBLE);
		        
		        WMCNIsPlaying = false;
			}
		});
    	
    	playButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				stopButton.setVisibility(View.VISIBLE);
				playButton.setVisibility(View.INVISIBLE);
				
				if (WMCNIsStarted){
					WMCNPlayer.start();
					WMCNIsPlaying = true;
					loadWheel.setVisibility(View.INVISIBLE);
				} else {
					new AsyncWMCNPlayer().execute();
					WMCNIsStarted = true;
					WMCNIsPlaying = true;
				}
			}
		});
    	clearScreen();
    }
    
    /** 
     * This method is overridden and the visibility has to match the overridden method. It defines what will
     * happen when the built-in menu buttons are clicked. These are the buttons in the top left and top right
     * of our activity. The method takes a MenuItem as input, which is passed to it when one of the top menu-
     * buttons are clicked. 
     */
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == android.R.id.home) {
			rbmView.toggleMenu();
			return true;
		
		} 
		if (id == R.id.creator_info){
			startActivity(new Intent(this, About.class));
			return true;
		}
		else {
			return super.onOptionsItemSelected(item);
		}
	}
    
    /**
     * This creates the options menu in the top right corner. It contains one element which opens the about activity.
     * It takes a menu object as input, and returns a boolean when it is done, so that we know everything works.
     * Called when the activity is created.
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }
    
    /*
     * This here is the method which is called when the Mac Buildings item is clicked in the ribbonViewMenu.
     * It makes a ListView show up with all the buildings we could find info for show up. It also makes
     * a listener that will be called when you click one of the items in the BuildingList. It makes a new
     * bundle and attaches it to the intent it passes startActivity when it starts the MacBuilding class. In
     * this bundle we attach the description of the building which is clicked. This is possible to do very simply
     * because the String arrays are mirrored so that the first building name has the description which is first in
     * the description String array.
     */
    private void macBuildings () {
    	mainList.setVisibility(View.VISIBLE);
    	
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.mac_Buildings));
    	
    	mainList.setAdapter(adapter);
    	
    	mainList.setOnItemClickListener(new OnItemClickListener() {
   		 
   		 @Override
   			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
   		    
   		   Intent buildingIntent = new Intent(view.getContext(), MacBuilding.class);
   		   
   		   String[] buildingNameArray = getResources().getStringArray(R.array.mac_Buildings);
   		   String[] buildingDescriptionArray = getResources().getStringArray(R.array.building_descriptions);
   		   
   		   Bundle b = new Bundle();
   		   b.putString("building", buildingNameArray[position]);
   		   b.putString("buildingDescription", buildingDescriptionArray[position]);
   		   
   		   buildingIntent.putExtra("android.intent.extra.INTENT", b);
   		   
   		   startActivity(buildingIntent);
   		  }
   		 });
    	
    	mainList.setSelection(0);
    }
    
    /*
     * This is the first of two macMenu methods. This one takes a Document object as input and uses an instance of
     * the class MacMenuWeekMaker to parse the information we want out of it. It then uses that instance to assign values to the String
     * arrays macMenuDays and menuOfTheDay which are used in the second macMenu method.  
     */
    private void macMenu(Document allMacMenuTextDoc){
		MacMenuWeekMaker macMenuWeek = new MacMenuWeekMaker(allMacMenuTextDoc);
		macMenuDays = macMenuWeek.getCafeMacWeek();
		menuOfDay = macMenuWeek.getTheDaysMenu();
		macMenu();
    }
    
    
    /*
     * This is the second macMenu method, which takes no input, but uses macMenuDays and menuOfTheDay to 
     * make the background list show all the days of the week which are in the rss feed from Cafe Mac,
     * and makes them clickable so that when you click them you are sent to another activity where you can see
     * the menu for the day.
     */
    private void macMenu() {
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, macMenuDays);
    	mainList.setAdapter(adapter);
    	mainList.setVisibility(View.VISIBLE);
    	mainList.setSelection(0);
    	
    	mainList.setOnItemClickListener(new OnItemClickListener() {
      		 
    		@Override
      			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      	    
    			Intent foodIntent = new Intent(view.getContext(), MacMenuFood.class);
      	   
      	   
    			Bundle b = new Bundle();
    			b.putString("day", macMenuDays[position]);
    			b.putString("foodItems", menuOfDay[position]);
      	   
    			foodIntent.putExtra("android.intent.extra.INTENT", b);
      	   
    			startActivity(foodIntent);
      		  }
      		});
    }
    
    /*
     * Here follow two macEvents methods which are very similar to the macMenu methods. They basically do
     * the exact same thing, but for the events rss feed from Macalester
     */
    private void macEvents(Document allEventsTextDoc){
    	MacEventsMaker someMacEvents = new MacEventsMaker(allEventsTextDoc);
    	
    	macEventTitles = someMacEvents.getEventTitles();
    	macEventLinks = someMacEvents.getEventLinks();
    	macEventDescriptions = someMacEvents.getEventDescriptions();
    	macEvents();
    }
    
    /*
     * Makes a clickable list out of all the Events titles and directs you to another activity showing the Event description when you
     * click one
     */
    private void macEvents() {
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, macEventTitles);
    	mainList.setAdapter(adapter);
    	mainList.setVisibility(View.VISIBLE);
    	mainList.setSelection(0);
    	
    	mainList.setOnItemClickListener(new OnItemClickListener() {
      		 
      		 @Override
      			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      		    
      		   Intent eventIntent = new Intent(view.getContext(), MacEventDetails.class);
      		   
      		   
      		   Bundle b = new Bundle();
      		   b.putString("eventTitle", macEventTitles[position]);
      		   b.putString("eventDescription", macEventDescriptions[position] + "\n" + macEventLinks[position]);
      		   
      		   eventIntent.putExtra("android.intent.extra.INTENT", b);
      		   
      		   startActivity(eventIntent);
      		  }
      		 });
    	
    }
    
    /*
     * My favorite method. This in itself only redirects the sound we'll get from the mediaplayer to
     * be controlled by the speakers, and also makes the buttons controlling the playback of it to show up
     * Is called when you click the item in the rbmView menu
     */
    private void macRadio() {
    	setVolumeControlStream(AudioManager.STREAM_MUSIC);
    	if (!WMCNIsPlaying){
    		playButton.setEnabled(true);
    		playButton.setVisibility(View.VISIBLE);
    	}
    	else {
    		stopButton.setEnabled(true);
    		stopButton.setVisibility(View.VISIBLE);
    	}
        
        
    }
    
    /*
     * called when you click the mac Athletics button and makes the buttons connected with mac Athletics show up
     * These button in turn direct you to different activities.
     */
    private void macAthletics() {
    	openAthleticsWebButton.setVisibility(View.VISIBLE);
    	regIMButton.setVisibility(View.VISIBLE);
    }
    
    /*
     * Called when you click the mobile website item in the rbmView menu.
     * It in turn calls the macWeb method and passes it the url for macalester's mobile website as an input 
     */
    private void macMobileWebsite() {
    	macWeb("http://m.macalester.edu/");
    }
    
    /*
     * Is called when you click the register Intramural button from the mac Athletics screen.
     * Starts the regIM activity
     */
    private void regIM() {
    	Intent intent = new Intent(this, Intramural.class);
	    startActivity(intent);
    }
    
    /*
     * Takes a string which it adds to a bundle, which it again adds to the intent it passes the MacWeb class.
     */
    private void macWeb(String URL) {
    	  Intent webIntent = new Intent(this, MacWeb.class);
		   
		   
		   Bundle b = new Bundle();
		   b.putString("urlToOpen", URL);
		   
		   webIntent.putExtra("android.intent.extra.INTENT", b);
		   
		   startActivity(webIntent);
    }
    
    
    /*
     * Is called when you click the Map of Mac item in the rbmView Menu.
     * Starts the MacMap activity.
     */
    private void mapOfMac() {
    	Intent intent = new Intent(this, MacMap.class);
    	startActivity(intent);
    }
    
    /**
     * Is called when you click the downKey on your phone, and is set to toggle the rbmView Menu instead
     * of going from the app to your phone's home screen. It does not use any of the inputs it takes, but
     * had to take them, so that it successfully overrides the superclass's method.
     */
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
    		rbmView.toggleMenu();
    	}
    	else super.onKeyDown(keyCode, event);
	    return true;
	}

    
    /**
     * This method is called when you click something in the rbmView Menu. It takes one input, the itemId,
     * which represents the id of the item in the activity_main.xml's menu. Based on this it decides what
     * it should do by comparing the itemId it has been passed with the ids we know the different items have.
     */
	@Override
	public void RibbonMenuItemClick(int itemId) {
		clearScreen();
		if(itemId == R.id.ribbon_mac_radio){
			macRadio();
		}
		if(itemId == R.id.ribbon_mac_map){
			mapOfMac();
		}
		if(itemId == R.id.ribbon_mac_menu){
			if(macMenuDays == null){
				new AsyncGetDocFromURL().execute(MAC_MENU_RSS);
			} else {
			macMenu();
			}
		}
		if(itemId == R.id.ribbon_mac_events){
			if(macEventTitles == null){
				new AsyncGetDocFromURL().execute(MAC_EVENTS_RSS);
			}else{
				macEvents();
			}
		}
		if(itemId == R.id.ribbon_mac_buildings){
			macBuildings();
		}
		if(itemId == R.id.ribbon_mac_athletics){
			macAthletics();
		}
		if(itemId == R.id.ribbon_mac_mobile_website){
			macMobileWebsite();
		}
	}
	
	
	/**
	 * This class extends AsyncTask and makes a new thread for doing stuff which takes a long time,
	 * because it has to access the internet. In this case it has to initialize, prepare and start a
	 * mediaplayer. Before it begins to do this, it makes a progresswheel show up on the screen, which it
	 * takes down when it is done. It also assigns the mediaplayer it has started to a mediaplayer
	 * class variable in MainActivity, so that we more easily can pause and start it using GUIs from there.
	 * 
	 * Because it is has to interact with GUI elements in the MainActivity, we want these AsyncTasks to be
	 * subclasses.
	 * 
	 * A new instance of this AsyncTask are made when you click the play button from the WMCN screen for the
	 * first time.
	 * 
	 * @author Dariush, Eivind and Haroon
	 *
	 */
	private class AsyncWMCNPlayer extends AsyncTask<Void, Void, MediaPlayer> {
		private MediaPlayer macRadioPlayer;
		
		/*
		 * This is called when the AsyncWMCNPlayer instance is first made and basically makes the
		 * progress wheel show up and uses the superclasses method to start the doInBackground method. 
		 */
		   @Override
		   protected void onPreExecute() {
			   super.onPreExecute();
			   loadWheel.setVisibility(View.VISIBLE);
		   }
		   
		   /*
		    * This is where the magic happens. Here we do everything we need to do with the mediaplayer,
		    * which takes time! While it works, the progress wheel spins on. Takes Void as input, but
		    * does not use it, because it doesn't need it.
		    */
		   @Override
		   protected MediaPlayer doInBackground(Void... voids) {
			   macRadioPlayer = new MediaPlayer();
			   try {
				   macRadioPlayer.setDataSource("http://216.250.175.13:8000/");
				   } catch (IllegalArgumentException e) {
					   feederror.setVisibility(View.VISIBLE);
				   } catch (IllegalStateException e) {
					   feederror.setVisibility(View.VISIBLE);
			       } catch (IOException e) {
			    	   feederror.setVisibility(View.VISIBLE);
			           }
			   try {
				macRadioPlayer.prepare();
			} catch (IllegalStateException e) {
				feederror.setVisibility(View.VISIBLE);
			} catch (IOException e) {
				feederror.setVisibility(View.VISIBLE);
			}
			   publishProgress(voids);
			   macRadioPlayer.setOnPreparedListener(new OnPreparedListener() {
				   public void onPrepared(MediaPlayer mp) {
			            	macRadioPlayer.start();
			            	}
				   });
			   return macRadioPlayer;
		   }
		   
		   /*
		    * This method can be called from doInBackground to, for example update a progress bar,
		    * however we don't need it so we just pass it Void, and let it not do anything.
		    */
		 
		   @Override
		   protected void onProgressUpdate(Void... voids) {
		   }
		   
		   /*
		    * This is called when doInBackground is done. It makes the progress wheel go away and
		    * assigns the mediaplayer, which it is passed, to a class variable in MainActivity, so
		    * that we can use it from there.
		    */
		   
		   @Override
		   protected void onPostExecute(MediaPlayer macRadioPlayer) {
			   WMCNPlayer = macRadioPlayer;
			   loadWheel.setVisibility(View.INVISIBLE);
		   }
	}


	/**
	 * This AsyncTask takes a URL string as input and gets an RSS feed as a Document Object back.
	 * Again this takes time because it needs to connect to the internet, so we make another thread for
	 * it to work in.
	 * 
	 * 
	 * @author Dariush, Eivind, Haroon
	 *
	 */
	
	private class AsyncGetDocFromURL extends AsyncTask<String, Void, Document> {
		/*
		 * Before we do anything we put up the progress wheel again, so that the user knows that our
		 * app actually works, and is not lagging.
		 */
		   @Override
		   protected void onPreExecute() {
		      super.onPreExecute();
		      loadWheel.setVisibility(View.VISIBLE);
		   }
		   
		   /*
		    * Where the magic happens, we first get a string in the first try catch, then make a
		    * Document out of it in the second try catch.
		    */
		   @Override
		   protected Document doInBackground(String... urls) {
				String urlToUse = urls[0];
				currentURL = urlToUse;
				
				String xml = null;
				
				try {
					DefaultHttpClient httpClient = new DefaultHttpClient();
		            HttpGet httpGet = new HttpGet(urlToUse);
		 
		            HttpResponse httpResponse = httpClient.execute(httpGet);
		            HttpEntity httpEntity = httpResponse.getEntity();
		            xml = EntityUtils.toString(httpEntity);
		 
		        } catch (UnsupportedEncodingException e) {
		            feederror.setVisibility(View.VISIBLE);
		        } catch (ClientProtocolException e) {
		        	feederror.setVisibility(View.VISIBLE);
		        } catch (IOException e) {
		        	feederror.setVisibility(View.VISIBLE);
		        }
				
				Document doc = null;
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				dbf.setCoalescing(true);
				try {
					DocumentBuilder db = dbf.newDocumentBuilder();
					InputSource inputSource = new InputSource();
					inputSource.setCharacterStream(new StringReader(xml));
		           
					doc = db.parse(inputSource);
				   	} catch (ParserConfigurationException e) {
				   		feederror.setVisibility(View.VISIBLE);
				   	} catch (SAXException e) {
				   		feederror.setVisibility(View.VISIBLE);
				   	} catch (IOException e) {
				   		feederror.setVisibility(View.VISIBLE);
				   	}
				return doc;
				}
		   
		 /*
		  * Does nothing
		  */
		   	@Override
		   	protected void onProgressUpdate(Void... voids) {
		   	}
		   
		   	/*
		   	 * We then, depending on the input RSS url, pass the Document we made to our macEvents(Document)
		   	 * or macMenu(Document) methods above.
		   	 */
		   	@Override
		   	protected void onPostExecute(Document result) {
		   		loadWheel.setVisibility(View.INVISIBLE);
		   		if(currentURL.equals(MAC_EVENTS_RSS)){
		   			macEvents(result);
		   		}
		   		if(currentURL.equals(MAC_MENU_RSS)){
		   			macMenu(result);
		   		}
		    }
	}
}