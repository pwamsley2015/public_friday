package Friday;

import Friday.ChatContext.ChatType;
import data.ContactsChat;
import data.diet.DietChat;
import data.training.TrainingChat;

public class ChatContextFactory {
	
	public static ChatContext openNewContext(ChatType type) throws ChatContextNotSupportedException {
		
		switch (type) {
			case SCHEDULE:
				throw new ChatContextNotSupportedException("Schedule Chat not yet supported.");
			case TRAINING:
				return new TrainingChat();
			case DATA:
				throw new ChatContextNotSupportedException("Data Chat not yet supported.");
			case TEST:
				return new DevChat();
			case REMINDER:
				return new ReminderChat();
			case CONTACTS:
				return new ContactsChat();
			case DIET:
				return new DietChat();
			default:
				return null;
		}
		
	}

}

class ChatContextNotSupportedException extends RuntimeException {
	
	ChatContextNotSupportedException(String s) {
		super(s);
	}
	
}
