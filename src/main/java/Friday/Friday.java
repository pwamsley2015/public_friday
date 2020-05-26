 package Friday;

import java.util.ArrayList;

import com.twilio.TwilioHelper;

import Friday.ChatContext.ChatType;
import Friday.chat_io_types.ConsoleIO;
import Friday.chat_io_types.FridayIO;
import Friday.chat_io_types.TextIO;
import data.Contacts;
import data.training.Variations;

public class Friday {
	
	private static ArrayList<ChatContext> chatContexts;
	private static FridayIO io;
	
	
	public static void main(String[] args) {
		
		//init Contact Book & variations file
		Contacts.init();
		Variations.init();
		
		//init list of chat contexts
		chatContexts = new ArrayList<>();
		
		//begin Friday's clock
		new Thread(Clock.getClock()).start();
		
		//run startup confiq
		io = Startup.startup(args);
		io.init();
		io.sendOutput("Hey Boss.");
			
		//Begin waiting for texts
		while (true) {
			if (!io.isWaitingForResponse()) {
				io.waitAndRespond();
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void switchIO(String[] args) {
		io.sendOutput("Switching io...");
		io = Startup.startup(args);
		io.init();
		io.sendOutput("We can chat over here.");
	}
	
	public static void addContext(ChatContext context) {
		chatContexts.add(context);
	}
	
	/**
	 * Searches the list of contexts for an active chat with the matching type.
	 * @return the ChatContext is found and is active, null otherwise
	 */
	public static ChatContext getContext(ChatType type) {
		for (ChatContext context : chatContexts) {
			if (context.type == type && context.isLive()) {
				return context;
			}
		}
		
		return null;
	}
	
	protected static ArrayList<ChatContext> getChatContexts() {
		return chatContexts;
	}

}
