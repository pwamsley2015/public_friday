package data.diet;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

import Friday.FridayAction;
import data.DataSource;
import io.WritableFile;
import io.friday.FridayOutput;

public class WeightLogEntry implements FridayAction, Serializable, DataSource {
	private static final long serialVersionUID = 1325166494524902520L;
	
	private String response;
	private transient final WritableFile log;
	private double weight;
	private Date date;
	private boolean valid = false;

	public WeightLogEntry(String message, WritableFile log) {

		String extractedFloat = message.replaceAll(".*?([\\d.]+).*", "$1");
		try {
			weight = Float.parseFloat(extractedFloat);
			valid = true;
		} catch (NumberFormatException e) {}
		
		if (!valid) {
			response = "Sorry, I couldn't find your body weight in that message. Please check your formatting and send again :)";
		} else {
			response = "Got it! I've logged that entry for you.";
		}
		
		this.log = log;
		this.date = new Date();
	}

	@Override
	public FridayOutput<String> execute() {
		if (valid)
			log.println(toString());
		return new FridayOutput<>(response);
	}

	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat("###.#");
		return date.getMonth() + "/" + date.getDate() + "/" + date.getYear() + ": " + df.format(weight);
	}
	@Override
	public double getValue() {
		return weight;
	}
}
