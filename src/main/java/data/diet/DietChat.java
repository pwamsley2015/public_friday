package data.diet;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import Friday.ChatContext;
import Friday.FridayProperties;
import Friday.FridayRecieveMessage;
import data.DataUtil;
import data.DataUtil.DataDouble;
import data.diet.WeightLogParser.BodyWeightEntry;
import io.FileReader;
import io.WritableFile;

public class DietChat extends ChatContext {
	
	private WritableFile weightLog;
	
	public DietChat() {
		super(ChatType.DIET);
		try {
			weightLog = new WritableFile(FridayProperties.getProperties().WEIGHT_LOG_PATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getResponse() {
		FridayRecieveMessage message = super.getNextMessage();
		String extractedFloat = message.getContents().replaceAll("[^\\d.]", "");
		
		//user texted Friday a number to log
		System.out.println(extractedFloat);
		if (!extractedFloat.isEmpty()) {
			return new WeightLogEntry(message.getContents(), weightLog).execute();
		} else if (message.getContents().toLowerCase().contains("report") || message.getContents().toLowerCase().contains("view")) {
			System.out.println("Going to send report...");
			return dietReview();
		}
		
		return "Sorry, I'm not sure what you're asking me to do. You look great though.";
	}

	private String dietReview() {
		String weightLog = FileReader.getFileContents(FridayProperties.getProperties().WEIGHT_LOG_PATH);
		List<BodyWeightEntry> data = WeightLogParser.parseLog(weightLog);
		String response = "I'll get smarter about diet coaching, but I'm a slow learner. Here's some information I can provide right now:\nAverage weekly-bucketed weight change:";
//		String s = "" + DataUtil.average(DataUtil.getNthOrderChangeInValues(DataUtil.getLocalizedRunningAverages(data, 7), 1));
//		System.out.println(s);
		
		List<DataDouble> averages = DataUtil.getLocalizedRunningAverages(data, 7);
		double dailyAverage = DataUtil.average(DataUtil.getNthOrderChangeInValues(averages, 1));
		DecimalFormat df = new DecimalFormat("###.###");
		return response + df.format(dailyAverage) + " lbs/day (" + df.format(dailyAverage * 7) + " lbs/week and" + df.format(dailyAverage * 30.5) + " lbs/month)";
	}
	
	
}
