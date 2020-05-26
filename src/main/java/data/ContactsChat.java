package data;

import static Friday.ChatContext.ChatType.CONTACTS;

import Friday.ChatContext;
import Friday.FridayRecieveMessage;

public class ContactsChat extends ChatContext {

	public ContactsChat() {
		super(CONTACTS);
	}

	@Override
	public String getResponse() {
		FridayRecieveMessage message = super.getNextMessage();
		
		
		return interpret(message.getContents());
	}
	
	private String interpret(String message) {
		message = message.toLowerCase();
		String response = "Sorry, I couldn't understand that request.";
		
		if (!message.contains("contact")) {
			return "I'm not sure what you want me to do.";
		}
		
		if (message.contains("new")) {
			if (addNewContact(message)) {
				response = "Okay, done.";
			}
		} else if (message.contains("remove")) {
			removeContact();
		} else if (message.contains("edit")) {
			editContact();
		} else if (message.contains("get")) {
			findContact();
		}
		
		return response;
		
	}
	
	private boolean addNewContact(String message) {
		
		message = message.substring(message.indexOf("contact") + "contact".length());
		String[] nameAndNum = message.split("-");
		String name = nameAndNum[0], num = nameAndNum[1];
		
		if (num.length() != 9 && num.length() != 10) {
			return false;
		}
		
		Contacts.add(name, num);
		return true;
	}
	
	private boolean removeContact() {
		return false;
	}
	
	private boolean editContact() {
		return false;
	}
	
	private boolean findContact() {
		return false;
	}

}
