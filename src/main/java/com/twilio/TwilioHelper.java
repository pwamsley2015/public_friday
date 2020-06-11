package com.twilio;


import static spark.Spark.post;

import java.io.IOException;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.twiml.MessagingResponse;
import com.twilio.type.PhoneNumber;

import Friday.FridayProperties;
import io.Scrips; 

public class TwilioHelper {
	
	private static boolean isInit = false;
	
	private static FridayRecievedTextMessage nextRecieved;
	
	public static void init() {
		
		if (isInit) {
			return;
		}
		
		try {
			String proxyUrl = Scrips.openProxyServer();
			System.out.println("Proxy Server started: " + proxyUrl);
			Scrips.bridgeProxyToTwilio(proxyUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		FridayProperties props = FridayProperties.getProperties();
		Twilio.init(props.TWILLIO_ACCOUNT_SID, props.TWILLIO_AUTH_TOKEN);
		isInit = true;
	}
	
	public static void sendText(PhoneNumber to, String message) {
		Message.creator(to, FridayProperties.getProperties().FRIDAY_NUMBER, message).create();
	}
	
	public static void recieveAndProcessText() {
		post("/receive-sms", (req, res) -> {
			
			nextRecieved = new FridayRecievedTextMessage(req.queryParams("Body"), req.queryParams("From"));
			
			System.out.println("Recieved text from " + req.queryParams("From") + ": " + nextRecieved.getContents());
			
			//---empty twiml response, we handle responding server-side using sendText()
			MessagingResponse twiml = new MessagingResponse.Builder()
										.build();
			
			return twiml.toXml();
		});
	}
	
	
	public static FridayRecievedTextMessage getNext() {
		FridayRecievedTextMessage next = nextRecieved;
		nextRecieved = null;
		return next;
	}

	public static boolean hasNext() {
		return nextRecieved != null;
	}

}
