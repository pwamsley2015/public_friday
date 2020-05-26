package Friday;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Abstract class representing a Chat Context.
 * 
 * A Chat Context is the context for a chat between the user and Friday.
 * Subclasses of ChatContext are used to represent the type of chat the user and Friday are having.
 * 
 * This class holds information common to all contexts--namely, the queue of messages in the chat, and whether or not the chat is active.
 * 
 * Friday can have more than one active chat with a user at a time.
 * @author Patrick Wamsley
 */
public abstract class ChatContext {
	
	public static final String[]	SCHEDULE_KEYWORDS	= {"schedule"},
									TRAINING_KEYWORDS	= {"training"},
									REMINDER_KEYWORDS	= {"remind", "reminder"},
									DATA_KEYWORDS		= {"data"},
									TEST_KEYWORDS		= {"test", "dev"},
									CONTACTS_KEYWORDS	= {"contacts", "contact"},
									DIET_KEYWORDS		= {"diet", "weight"};
	
	/*
	 * Types of ChatContexts Friday currently supports
	 */
	public static enum ChatType {
		
		SCHEDULE(SCHEDULE_KEYWORDS), TRAINING(TRAINING_KEYWORDS), DATA(DATA_KEYWORDS), TEST(TEST_KEYWORDS), REMINDER(REMINDER_KEYWORDS),
		CONTACTS(CONTACTS_KEYWORDS), DIET(DIET_KEYWORDS);
		
		String[] keywords; 
		
		ChatType(String[] keywords) {
			this.keywords = keywords;
		};		
	}

	private boolean isLive; 
	
	private LinkedList<FridayRecieveMessage> queue;
	
	public final ChatType type;
	
	protected ChatContext(ChatType type) {
		isLive = false;
		queue = new LinkedList<>();
		this.type = type;
	}
	
	public void recievedMessage(FridayRecieveMessage message) {
		queue.add(message);
	}
	
	public FridayRecieveMessage getNextMessage() {
		return queue.poll();
	}
	
	public abstract String getResponse();
	
	public void openContext() {
		isLive = true;
	}
	public void closeContext() {
		isLive = false;
	}
	
	public boolean isLive() {
		return isLive;
	}
	
	@Override
	public String toString() {
		return "Chat: " + type + (isLive ? ". Currently live." : ". Currently closed.\n");
	}
	
}
