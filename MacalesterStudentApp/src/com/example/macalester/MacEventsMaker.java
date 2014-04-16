package com.example.macalester;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * This class parses the Document that holds all the text in the xml file that is published
 * in the rss feed. It stores the information it gathers in three string arrays, which allow
 * for quick implementation when we need to get the information in them. Their application
 * depends on a uniform text in the rss feed published by macalester college.
 * 
 * 
 * @author Dariush, Eivind and Haroon
 *
 */
public class MacEventsMaker {
	
	private NodeList allMacEventsNodeList;
	private String[] macEventTitles;
	private String[] macEventLinks;
	private String[] macEventDescriptions;
	private String tempTitle;
	private String tempLink;
	private String tempDescription;
	
	/**
	 * This is the constructor that takes a Document containing all the text from the xml file from the rss feed,
	 * and makes a NodeList containing all the nodes tagged "item". These are the events. After that it initializes
	 * the string arrays to contain the same number of elements as the NodeList, and passes the NodeList to makeMacEvents.
	 */
	
	public MacEventsMaker(Document macEventsDoc){
		allMacEventsNodeList = macEventsDoc.getElementsByTagName("item");
		macEventTitles = new String[allMacEventsNodeList.getLength()];
		macEventLinks = new String[allMacEventsNodeList.getLength()];
		macEventDescriptions = new String[allMacEventsNodeList.getLength()];
		makeMacEvents(allMacEventsNodeList);
	}
	
	
	/*
	 * This method finds the subnodes of the "item" node which are titled "link", "title" and "description", and
	 * assigns these to the temporary values which are then added to the string arrays in makeMacEvents.
	 */
	private void getValues(Node item) {
	    NodeList n = item.getChildNodes();        
	    for(int i = 0; i < n.getLength(); i++) {
	    	if(n.item(i).getNodeName().equals("link")){
	    		tempLink = n.item(i).getTextContent();
	    	}
	    	if(n.item(i).getNodeName().equals("title")){
	    		tempTitle = n.item(i).getTextContent();
	    	}
	    	if(n.item(i).getNodeName().equals("description")) {
	    		tempDescription = n.item(i).getTextContent();
//	    		}
	    	}
	    }
	}
	
	/*
	 * This method goes through all the Nodes in the NodeList and passes them to getValues.
	 * It then assigns the links it gets to the appropriate position of macEventsLinks, the
	 * titles of the events to the appropriate position of macEventTitles, and the event
	 * description to the appropriate position of macEventDescriptions
	 */
	
	private void makeMacEvents(NodeList allTheEvents) {
		for(int i = 0; i < allMacEventsNodeList.getLength(); i++){
			getValues(allMacEventsNodeList.item(i));
			macEventLinks[i] = tempLink;
			macEventTitles[i] = tempTitle;
			macEventDescriptions[i] = tempDescription;
		}
		
	}
	
	/**
	 * Returns the string array containing the titles of the events
	 */
	
	public String[] getEventTitles(){
		return macEventTitles;
	}
	
	/**
	 * Returns the string array containing the links to the events
	 */
	
	public String[] getEventLinks(){
		return macEventLinks;
	}
	
	/**
	 * Returns the string array containing the description of the events
	 */
	
	public String[] getEventDescriptions(){
		return macEventDescriptions;
	}
}
