package Friday.chat_io_types;

import Friday.FridayProperties;
import Friday.FridayRecieveMessage;
import Friday.ResponseManager;
import gui.FridayConsole;

public class GuiIO extends FridayIO {
	
	private FridayConsole console = new FridayConsole(this);
	private String recievedMessage; 
	
	public void recieveInput(String input) {
		recievedMessage = input;
		isWaitingForResponse = false;
	}

	@Override
	public void init() {
		isWaitingForResponse = true;
	}

	@Override
	public void sendOutput(String output) {
		if (isWaitingForResponse) {
			console.println("*\n" + output);
		} else {
			console.println(output);
		}
		isWaitingForResponse = true;
	}

	@Override
	public void waitAndRespond() {
		FridayRecieveMessage m = new FridayRecieveMessage(recievedMessage, FridayProperties.getProperties().USER_NUMBER.toString());
		sendOutput(ResponseManager.getResponse(m));
	}
	
	public FridayConsole getConsole() {
		return console;
	}

}
