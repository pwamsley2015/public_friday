package Friday;

import com.twilio.FridayRecievedTextMessage;

import Friday.ChatContext.ChatType;
import io.friday.FridayInput;
import io.friday.FridayOutput;
import users.User;


/**
 * Manages Friday's responses to input from a given user.
 * 
 * @author Patrick Wamsley
 */
public class ResponseManager {
	
	private ChatContext lastActiveChatContext;
	private User user;
	
	public ResponseManager(User user) {
		this.user = user;
	}
	
	public FridayOutput<?> getResponse(FridayInput<?> input) {	
		//first get the context for this message
		ChatContext context = getContext(input);
		
		if (context == null) {
			if (lastActiveChatContext == null) {
				return new FridayOutput<String>("Sorry, I don't understand that message. Remember to use a keyword so I know what we're talking about");
			} else {
				context = lastActiveChatContext;
			}
		} else {
			lastActiveChatContext = context;
		}
		
		context.recievedMessage(input);
		return context.getResponse();
	}

	/**
	 * Gets the ChatContext of the sent message, or null if no context could be found
	 */
	private ChatContext getContext(FridayInput<?> input) {
		
		//---non-string, non-text inputs not yet supported---//
		Object val = input.getValue();
		String message;
		if (val instanceof String) {
			message = (String)val;
		} else if (val instanceof FridayRecievedTextMessage) {
			message = ((FridayRecievedTextMessage)(val)).getContents();
		} else {
			return null;
		}
		
		//get the words in the message
		String[] words = message.split(" ");
		
		//remove punctuation and look for ChatType
		ChatType type;
		for (int i = 0; i < words.length; i++) {
			words[i] = words[i].replaceAll("[^a-z\\sA-Z]", "").toLowerCase();
			type = checkWord(words[i]);
			if (type != null) {
				//check active chats
				ChatContext context = user.getChatContext(type);
				if (context != null) {
					return context;
				} else {
					//create new context
					try {
						context = ChatContextFactory.openNewContext(type, user);
						context.openContext();
						user.addAddContext(context);
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
