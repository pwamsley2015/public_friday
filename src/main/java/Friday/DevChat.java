package Friday;

import com.twilio.TwilioHelper;
import com.twilio.type.PhoneNumber;

import data.Contacts;
import data.training.PrimaryMovement;
import data.training.TrainingMovement;
import data.training.Variations;
import io.Logger;

import static Friday.ChatContext.ChatType.TEST;

import java.util.ArrayList;
import java.util.Arrays;

public class DevChat extends ChatContext {
	
	private static final String[] COMMANDS = {"commands", "chats status", "log:", "clock", "contacts", "text", "vars", "new var", "clear variations", "change io <args>"};

	public DevChat() {
		super(TEST);
	}

	@Override
	public String getResponse() {
		
		FridayRecieveMessage message = super.getNextMessage();
		String response = "If you decided to program me to do something with this text, I would...";
		String request = message.getContents().toLowerCase();
		
		if (request.contains("chats status")) {
			ArrayList<ChatContext> chats = Friday.getChatContexts();
			response = "We currently have " + chats.size() + " chats going. Here they are:\n\n";
			response += chats;
		}
		
		if (request.contains("commands")) {
			response = "Right now, my dev commands are: " + Arrays.asList(COMMANDS);
		}
		
		if (request.contains("log:")) {
			Logger.log(request.substring(request.indexOf("log:") + 4), this.getClass());
			response = "Ok, I logged that for you.";
		}
		
		if (request.contains("clock")) {
			response = Clock.getClock().status();
		}
		
		if (request.contains("contacts")) {
			response = "Here's your current list of contacts:\n" + Contacts.seeBook();
		}
		
		if (request.contains("text")) {
			String[] split = message.getContents().split(":");
			String text = split[1];
			String name = split[0].replaceAll("dev", "").replaceAll("text", "").trim();
			
			PhoneNumber num = Contacts.searchForName(name);
			response = num == null ? "Sorry, I couldn't find that person in your contacts." : "Ok, I sent that text.";
			if (num != null) {
				TwilioHelper.sendText(num, text);
			}
		}
		
		if (request.contains("vars")) {
			response = "Here's the current list of variations:\n" + Variations.seeVariations();
		}
		
		//dev new var <var> <primary>
		if (request.contains("new var")) {
			String[] words = request.split(" ");
			TrainingMovement tm = new TrainingMovement(words[3], PrimaryMovement.valueOf(words[4].toUpperCase()), false);
			Variations.newVariation(tm);
			response = "Okay, new variation added";
		}
		
		if (request.contains("clear variations")) {
			Variations.clearVariations();
			response = "Variations cleared, boss";
		}
		
		if (request.contains("change io ")) {
			Friday.switchIO(new String[] { request.substring(request.indexOf("change io ") + "change io ".length()) });
			request = "I guess that worked.";
		}
		
		return response;
	}

}
