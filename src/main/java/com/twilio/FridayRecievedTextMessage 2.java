package com.twilio;

import com.twilio.type.PhoneNumber;

import data.Contacts;

/**
 * A message sent to Friday.
 * 
 * @author Patrick Wamsley
 */
public class FridayRecievedTextMessage {
	
	private String contents; 
	private boolean answered;
	
	private PhoneNumber from;
	
	public FridayRecievedTextMessage(String contents, String from) {
		this.contents = contents;
		this.answered = false;
		this.from	  = new PhoneNumber(from);
	}
	
	public void updateContents(String newContents) {
		contents = newContents;
	}
	
	public String getContents() {
		return contents;
	}
	
	public boolean isFromUser() {
		return Contacts.isMe(from);
	}
	
	public PhoneNumber getFrom() {
		return from;
	}
	
	@Override
	public String toString() {
		return getContents();
	}

}
