package Friday;

import java.util.ArrayList;
import java.util.Arrays;

import com.twilio.TwilioHelper;

import Friday.ChatContext.ChatType;
import data.Contacts;


/**
 * Creates Friday's text messages.
 * 
 * @author Patrick Wamsley
 */
public class ResponseManager {
	
	private static ChatContext lastActiveChatContext;
	
	public static String getResponse(FridayRecieveMessage message) {
		
		//check if this is from the user or from someone else
		if (!message.isFromUser()) {
			TwilioHelper.sendText(Contacts.getMe(), "I just got a message from " + message.getFrom() + "."); 
			return "I can reply to you on my own now, but currently with just this message. Don't worry, I'll get smarter when Patrick gets less lazy.";
		} else {
		
		//first get the context for this message
		ChatContext context = getContext(message);
		
		if (context == null) {
			if (lastActiveChatContext == null) {
				return "Sorry, I don't understand that message. Remember to use a keyword so I know what we're talking about";
			} else {
				context = lastActiveChatContext;
			}
		} else {
			lastActiveChatContext = context;
		}
		
		context.recievedMessage(message);
		return context.getResponse();
		}
	}

	/**
	 * Gets the ChatContext of the sent message, or null if no context could be found
	 */
	private static ChatContext getContext(FridayRecieveMessage message) {
		
		//get the words in the message
		String[] words = message.getContents().split(" ");
		
		//remove punctuation and look for ChatType
		ChatType type;
		for (int i = 0; i < words.length; i++) {
			words[i] = words[i].replaceAll("[^a-z\\sA-Z]", "").toLowerCase();
			type = checkWord(words[i]);
			if (type != null) {
				//check active chats
				ChatContext context = Friday.getContext(type);
				if (context != null) {
					return context;
				} else {
					//create new context
					try {
						context = ChatContextFactory.openNewContext(type);
						context.openContext();
						Friday.addContext(context);
						return context;
					} catch (ChatContextNotSupportedException e) {
						e.printStackTrace();
						return null;
					}
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Checks if this word is a keyword, and if so, returns the ChatType; null otherwise	
	 */
	private static ChatType checkWord(String word) {
		
		for (ChatType type : ChatType.values()) {
			for (String keyword : type.keywords) {
				if (keyword.equals(word)) {
					return type;
				} else {
//					System.out.println("Keyword " + keyword + " does not match word: " + word);
				}
			}
		}
		
		return null;
	}
}
