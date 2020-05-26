package Friday.chat_io_types;

import java.util.Scanner;

import Friday.FridayProperties;
import Friday.FridayRecieveMessage;
import Friday.ResponseManager;
import data.Contacts;

public class ConsoleIO extends FridayIO {
	
	private Scanner inputScanner;

	@Override
	public void init() {
		inputScanner = new Scanner(System.in);
	}

	@Override
	public void sendOutput(String output) {
		System.out.println(output);
	}

	@Override
	public void waitAndRespond() {
		isWaitingForResponse = true;
		String recievedInput = inputScanner.nextLine();
		FridayRecieveMessage message = new FridayRecieveMessage(recievedInput, FridayProperties.getProperties().USER_NUMBER.toString());
		sendOutput(ResponseManager.getResponse(message));
		isWaitingForResponse = false;
	}

}
