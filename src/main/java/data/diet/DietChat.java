package data.diet;

import java.io.IOException;

import Friday.ChatContext;
import Friday.FridayProperties;
import io.WritableFile;
import io.friday.FridayOutput;
import users.User;

public class DietChat extends ChatContext {
	
	private WritableFile weightLog;
	
	public DietChat(User user) {
		super(ChatType.DIET, user);
		try {
			weightLog = new WritableFile(FridayProperties.getProperties().WEIGHT_LOG_PATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public FridayOutput<String> getResponse() {
		String message = (String) super.getNextMessage().getValue();
		String extractedFloat = message.replaceAll("[^\\d.]", "");
		
		//user texted Friday a number to log
		System.out.println(extractedFloat);
		if (!extractedFloat.isEmpty()) {
			return new WeightLogEntry(message, weightLog).execute();
		} else if (message.toLowerCase().contains("report") || message.toLowerCase().contains("view")) {
			return new FridayOutput<>(dietReview());
		}
		
		return new FridayOutput<>("Sorry, I'm not sure what you're asking me to do. You look great though.");
	}

	private String dietReview() {
		return "dietReview() disabled()";
	}
	
	
}
