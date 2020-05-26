package data.training;

import Friday.FridayAction;
import io.Logger;

public class New_Variation implements FridayAction {
	
	private String response = "Sorry, I don't understand your request. Make sure you're using underscores. The format to add a new variation is the following:\n" +
								"new <primary_movement> variation <variation_name>\n\n The primary movements are: \n" + PrimaryMovement.getPrimaries();
	
	private PrimaryMovement primary = null;
	private String variation = null;
	
	protected New_Variation(String message) {
		String[] words = message.split(" ");
		if (words.length < 4) {
			return; 
		} else if (!(words[0].equalsIgnoreCase("new") && words[2].equalsIgnoreCase("variation"))) {
			return;
		} else {
			primary = PrimaryMovement.valueOf(words[1].toUpperCase());
			variation = words[3];
		}
	}

	@Override
	public String execute() {
		if (variation != null && primary != null) {
			Logger.log("Attempting to log new Variation: " + variation + "(" + primary + ").", this.getClass());
			Variations.newVariation(new TrainingMovement(variation, primary, false));
			response = "Okay, new variation added.";
		}
		return response;
	}

}
