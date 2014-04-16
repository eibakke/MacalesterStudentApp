package com.example.macalester;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class gets the text from the nodes that we want in the Mac Menu Document object. It stores this information in
 * String arrays which are mirrored so that the first day in the cafeMacDays is the day for the first menu string in the
 * daysMenu String array. This approach relies on a standard formatting from the publishers of the RSS feed, but in turn
 * provides very quick running since we know the number of elements in the arrays and need to extract the value at any index
 * very quickly.
 * 
 * @author Dariush, Eivind and Haroon
 *
 */

public class MacMenuWeekMaker {
	
	private String[] cafeMacDays;
	private String[] daysMenu;
	private String[] tempDayAndMenu;
	private NodeList daysAtCafeMac;
	
	/**
	 * This is the constructor which takes a Document, makes a NodeList of the items in the Document and initializes the number of
	 * elements in the string arrays to be the length of this NodeList. It then calls makeACafeMacWeek and passes it the NodeList for
	 * further parsing.
	 */
	
	public MacMenuWeekMaker(Document menuDoc) {
		daysAtCafeMac = menuDoc.getElementsByTagName("item");
		cafeMacDays = new String[daysAtCafeMac.getLength()];
		daysMenu = new String[daysAtCafeMac.getLength()];
		tempDayAndMenu = new String[2];
		makeACafeMacWeek(daysAtCafeMac);
		
	}
	
	/**
	 * This method gets the string out of the nodes named title and the node named description.
	 * It assigns these values to the temporary string array tempDayAndMenu.
	 */
	
	
	private String[] getValues(Node item) {
	    NodeList n = item.getChildNodes();
	    for(int i = 0; i < n.getLength(); i++){
	    	if(n.item(i).getNodeName().equals("title")){
	    		tempDayAndMenu[0] = n.item(i).getTextContent();
	    	}
	    	if(n.item(i).getNodeName().equals("description")){
	    		tempDayAndMenu[1] = n.item(i).getTextContent();
	    	}
	    }
	    return tempDayAndMenu;
	}
	
	/**
	 * This adds values to cafeMacDays[] and daysMenu[] which are used in MainActivity to show the
	 * days in the background list and as the array that holds the menus for each day, respectively.
	 */
	
	private void makeACafeMacWeek(NodeList allTheDays) {
		for(int i = 0; i < daysAtCafeMac.getLength(); i++) {
			String[] dayMenu = getValues(allTheDays.item(i));
			cafeMacDays[i] = dayMenu[0];
			daysMenu[i] = dayMenu[1];
		}
	}
	/**
	 * Returns the string array that holds the days of cafe mac
	 */
	public String[] getCafeMacWeek() {
		return cafeMacDays;
	}
	
	/**
	 * Returns the string array that holds the menus for each day in cafe mac
	 */
	
	public String[] getTheDaysMenu() {
		return daysMenu;
	}

}
