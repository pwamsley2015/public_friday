package com.twilio;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import Friday.FridayProperties;
import Friday.FridayRecieveMessage;
import Friday.ResponseManager;
import data.Contacts;
import io.Scrips;

import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;

import static spark.Spark.post;

import java.io.IOException; 

public class TwilioHelper {
	
	private static boolean isInit = false;
	private static boolean waitingForResponse = false;
	
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
	
	private String getProxyServerUrl() {
		return "proxy server url";
	}
	
	public static void sendText(PhoneNumber to, String message) {
		Message.creator(to, FridayProperties.getProperties().FRIDAY_NUMBER, message).create();
	}
	
	public static void recieveAndProcessText() {
		
		waitingForResponse = true;
		
		post("/receive-sms", (req, res) -> {
			
			FridayRecieveMessage recievedMessage = new FridayRecieveMessage(req.queryParams("Body"), req.queryParams("From"));
			
			waitingForResponse = false;
			
			System.out.println("Recieved text from " + req.queryParams("From") + ": " + recievedMessage.getContents());
			
			com.twilio.twiml.messaging.Message text = new com.twilio.twiml.messaging.Message.Builder()
													.body(new Body.Builder(ResponseManager.getResponse(recievedMessage)).build()).build();
			
			MessagingResponse twiml = new MessagingResponse.Builder()
										.message(text)
										.build();
			
			return twiml.toXml();
		});
	}
	
	public static boolean isWaitingForResponse() {
		return waitingForResponse;
	}

}
