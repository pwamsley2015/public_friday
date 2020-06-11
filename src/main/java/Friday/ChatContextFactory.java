package Friday;

import Friday.ChatContext.ChatType;
import data.ContactsChat;
import data.diet.DietChat;
import data.training.TrainingChat;
import users.User;

public class ChatContextFactory {
	
	public static ChatContext openNewContext(ChatType type, User user) throws ChatContextNotSupportedException {
		
		switch (type) {
			case SCHEDULE:
				throw new ChatContextNotSupportedException("Schedule Chat not yet supported.");
			case TRAINING:
				return new TrainingChat(user);
			case DATA:
				throw new ChatContextNotSupportedException("Data Chat not yet supported.");
			case TEST:
				return new DevChat(user);
			case REMINDER:
				return new ReminderChat(user);
			case CONTACTS:
				return new ContactsChat(user);
			case DIET:
				return new DietChat(user);
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
