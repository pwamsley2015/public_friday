package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Friday.FridayProperties;

public class Scrips {
	
	private static String run(String command) throws IOException {
		Process p = Runtime.getRuntime().exec(command);
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		StringBuilder processOutput = new StringBuilder();
		reader.lines().forEach(line -> processOutput.append(line));
		reader.close();
		String output = processOutput.toString();
		return output.isEmpty() ? "(no output)" : output;
	}
	
	/**
	 * Opens a proxy server which bridges access to localhost.
	 * @return the URL of the proxy server
	 */
	public static String openProxyServer() throws IOException {
		String ngrok = "/usr/local/bin/ngrok http 4567";
		final Process cd = new ProcessBuilder("cd", "~").start();
		new ProcessBuilder(ngrok.split(" ")).start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String curl = "curl -s localhost:4040/api/tunnels";
		String response = run(curl);
		String[] tokens = response.split("\"?\"");
		for (String token : tokens) {
			if (token.contains("ngrok.io")) {
				return token;
			}
		}
		
		killAllNgrok();
		throw new RuntimeException("Failed to get proxy server");
		
	}
	
	public static void bridgeProxyToTwilio(String proxyUrl) throws IOException {
		
		FridayProperties props = FridayProperties.getProperties();
		
		String smsUrl = "SmsUrl=" + proxyUrl + "/receive-sms";
		String command = "curl -XPOST https://api.twilio.com/2010-04-01/Accounts/" 
							+ props.TWILLIO_ACCOUNT_SID + "/IncomingPhoneNumbers/" 
							+ props.TWILLIO_FRIDAY_NUM_SID + ".json " + "--data-urlencode " 
							+ smsUrl + " -u " + props.TWILLIO_ACCOUNT_SID + ":" + props.TWILLIO_AUTH_TOKEN;
		System.out.println("Proxy server: " + proxyUrl);
		System.out.println("Command: " + command);
		System.out.println(run(command));
	}
	
	public static void killAllNgrok() throws IOException {
		String command = "killall ngrok";
		run(command);
	}
	
	public static void main(String[] args) {
		try {
			String proxy = openProxyServer();
			bridgeProxyToTwilio(proxy);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
