package users;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.twilio.type.PhoneNumber;

import Friday.ChatContext;
import Friday.ChatContext.ChatType;
import Friday.ResponseManager;
import io.friday.FridayIO;
import io.friday.FridayOutputSource;

public class User implements Serializable {
	private static final long serialVersionUID = -1062584643377674565L;

	private Set<ChatContext> chats;
	
	private String name;
	private PhoneNumber phoneNumber;
	private FridayIO fridayIO;
	private ResponseManager responseMan;
	
	public User(String name) {
		this.setName(name);
		responseMan = new ResponseManager(this);
		chats = new HashSet<>();
		UsersLookup.newUser(this);
	}
	
	public void initIO(FridayIO io) {
		this.fridayIO = io;
	}
	
	public void changeOutputSource(FridayOutputSource out) {
		fridayIO.changeOutput(out);
	}
	
	public ChatContext getChatContext(ChatType t) {
		for (ChatContext openChat : chats) {
			if (openChat.type == t) {
				return openChat;
			}
		}
		return null;
	}

	public boolean addAddContext(ChatContext context) {
		return chats.add(context);
	}
	
	public FridayIO getFridayIO() {
		return fridayIO;
	}
	
	public Set<ChatContext> getChatContexts() {
		return chats;
	}
	
	public ResponseManager getResponseManager() {
		return responseMan;
	}

	public PhoneNumber getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(PhoneNumber phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
