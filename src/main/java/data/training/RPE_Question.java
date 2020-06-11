package data.training;

import Friday.FridayAction;
import io.friday.FridayOutput;

public class RPE_Question implements FridayAction {

	private String response;
	
	protected RPE_Question(String message) {
		
		int rpeGiven = -1;
		float weightLifted = -1;
		int repsPreformed = -1;
		float estimatedOneRepMax = -1;
		
		boolean weightLiftedGivenLbs = false;
		boolean weightLiftedGivenKg = false;
		
		//look for weight
		if (message.contains("kg") || message.contains("kilos")) {
			weightLiftedGivenKg = true;
		} else if (message.contains("lbs") || message.contains("#")) {
			weightLiftedGivenLbs = true;
		}
		
		if (!weightLiftedGivenKg && !weightLiftedGivenLbs) {
//			response = "I need to know how much weight you lifted on this set. I understand both kilos (kg, kilos) and lbs (#, lbs)";
//			return;
			//assume lbs
			weightLiftedGivenLbs = true;
		}
		
		//--format-- 400lbs 5@8
		message = message.replace("training", "").trim();
		String[] tokens = message.split(" ");
		try {
			weightLifted = Float.parseFloat(tokens[0].replaceAll("[^\\d.]", ""));
			String[] rpeTokens = tokens[1].split("@");
			rpeGiven = Integer.parseInt(rpeTokens[1]);
			repsPreformed = Integer.parseInt(rpeTokens[0]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			response = "Sorry, I had trouble understanding the format of your message.";
			return;
		}
		
		//calc e1rm, weight for rpe +- 1, 
		try {
		estimatedOneRepMax = RPE.getEstimatedOneRepMax(weightLifted, rpeGiven, repsPreformed);
		} catch (IllegalArgumentException e) {
			response = "I'm afraid those numbers don't make sense. You're not trying to log an RPE < 6, are you?";
			return;
		}
		
		int nextRpe = rpeGiven + 1;
		int prevRpe = rpeGiven - 1;
		
		float nextRpeWeight;
		
		try {
			nextRpeWeight = estimatedOneRepMax * RPE.getPercentage(nextRpe, repsPreformed);
		} catch (IllegalArgumentException e) {
			nextRpeWeight = weightLifted * 1.05f;
		}
		
		float prevRpeWeight;
		
		try {
			prevRpeWeight = estimatedOneRepMax * RPE.getPercentage(prevRpe, repsPreformed);
		} catch (IllegalArgumentException e) {
			prevRpeWeight = weightLifted / 1.05f;
		}
		
		response = "That set puts your estimated 1RM at " + estimatedOneRepMax + (weightLiftedGivenKg ? "kg" : "#") + ".\n" +
					"For " + repsPreformed + "@" + nextRpe + " you'll need " + nextRpeWeight + (weightLiftedGivenKg ? "kg" : "#") + ", and for " + 
					repsPreformed + "@" + prevRpe + " you'll need " + prevRpeWeight + (weightLiftedGivenKg ? "kg" : "#") + ".";
	}
	
	@Override
	public FridayOutput<String> execute() {
		return new FridayOutput<>(response);
	}

}
