package data.diet;

import java.util.Date;

import Friday.FridayAction;
import data.diet.WeightLogParser.BodyWeightEntry;
import io.Logger;
import io.WritableFile;

public class WeightLogEntry implements FridayAction {

	private String response;

	public WeightLogEntry(String message, WritableFile log) {

		BodyWeightEntry entry = WeightLogParser.parseOneEntry(message);
		if (entry == null) {
			response = "Sorry, I couldn't find your body weight in that message. Please check your formatting and send again :)";
		} else {
			Logger.log("Logged a new body weight entry: " + entry.toString(), this.getClass());
			log.println(entry.toString());
		}

		response = "Got it! I've logged that entry for you.";
	}

	@Override
	public String execute() {
		return response;
	}

}
