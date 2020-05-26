package Friday;

import data.Contacts;

import static Friday.ChatContext.ChatType.REMINDER;

import java.io.Serializable;
import java.util.Date;


public class ReminderChat extends ChatContext implements Serializable {

	public ReminderChat() {
		super(REMINDER);
	}

	@Override
	public String getResponse() {
		return parseAndSchedule(super.getNextMessage().getContents()) ? "I've scheduled that reminder for you." : "Sorry, I wasn't able to understand your request.";
	}
	
	private static boolean parseAndSchedule(String message) {
		
		if (message.contains("at") || message.contains("At") || message.contains("@")) {
			return false;
		} else if (message.contains("in") || message.contains("In")) {
			ScheduledFridayAction reminder = new ScheduledFridayAction(findTimeIncriment(message)) {
				@Override
				public String execute() {
					return "You asked me to remind you:\n\n \"" + message + "\"";
					
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
