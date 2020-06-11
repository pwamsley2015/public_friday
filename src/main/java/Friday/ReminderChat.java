package Friday;

import static Friday.ChatContext.ChatType.REMINDER;

import java.io.Serializable;
import java.util.Date;

import io.friday.FridayOutput;
import users.User;


public class ReminderChat extends ChatContext implements Serializable {
	private static final long serialVersionUID = 4824563545571869370L;

	public ReminderChat(User user) {
		super(REMINDER, user);
	}

	@Override
	public FridayOutput<String> getResponse() {
		return new FridayOutput<>(parseAndSchedule((String)super.getNextMessage().getValue()) ? "I've scheduled that reminder for you." : "Sorry, I wasn't able to understand your request.");
	}
	
	private boolean parseAndSchedule(String message) {
		
		if (message.contains("at") || message.contains("At") || message.contains("@")) {
			return false;
		} else if (message.contains("in") || message.contains("In")) {
			ScheduledFridayAction reminder = new ScheduledFridayAction(findTimeIncriment(message), user) {
				@Override
				public FridayOutput<String> execute() {
					return new FridayOutput<String>("You asked me to remind you:\n\n \"" + message + "\"");
					
				}
			};
			
			Clock.getClock().addScheduleAction(reminder);
		} else {
			return false;
		}
		
		return true;
	}
	
	private static Date findTimeIncriment(String message) {
		message = message.toLowerCase().substring(message.indexOf("in"));
		message = message.replaceAll(".*?([\\d.]+).*", "$1");
		
		Date now = new Date();
		now.setMinutes(now.getMinutes() + Integer.parseInt(message)); 
		return now;
	}

}
