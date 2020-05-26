package data.diet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import data.DataSource;
import data.DataUtil;
import io.FileReader;

public class WeightLogParser {
	
	//TODO: move to test
	public static void main(String[] args) {
		ArrayList<BodyWeightEntry> log = parseLog(FileReader.getFileContents("/Users/pwamsley/Desktop/log.txt"));
		System.out.println(log);
		System.out.println("----------------");
	
		System.out.println("5 Day average:");
		DataUtil.getLocalizedRunningAverages(log, 5).stream().forEach(e -> System.out.println(e));
		System.out.println("Daily change:");
		System.out.println(DataUtil.getChangeInValues(log));
		System.out.println("weekly-average of change:");
		System.out.println(DataUtil.rawGetLocalizedRunningAverages(DataUtil.getChangeInValues(log), 7));
	}
	
	public static class BodyWeightEntry implements DataSource {
		
		private double bodyWeight;
		private Date date;
		
		BodyWeightEntry(int year, int month, int day, float bodyWeight) {
			this(bodyWeight, new Date(year, month, day));
		}
		
		BodyWeightEntry(float bodyWeight, Date date) {
			this.bodyWeight = bodyWeight;
			this.date = date;
		}
		
		@Override
		public String toString() {
			return date.getMonth() + "/" + date.getDay() + "/" + date.getYear() + ": " + bodyWeight;
		}

		@Override
		public double getValue() {
			return bodyWeight;
		}
		
	}
	
	public static BodyWeightEntry parseOneEntry(String message) {
		
		String extractedFloat = message.replaceAll(".*?([\\d.]+).*", "$1");
		
		try {
			float weight = Float.parseFloat(extractedFloat);
			return new BodyWeightEntry(weight, new Date());
		} catch (NumberFormatException e) {
			System.out.println("Failed to find weight in weight log message.");
			return null;
		}
	}

	public static ArrayList<BodyWeightEntry> parseLog(String s) {
		ArrayList<BodyWeightEntry> result = new ArrayList<>();
	
		String[] lines = s.split("\n");
		for (String line : lines) {
			if (line.isEmpty() || line.length() <= 1) {
				break;
			}
			
			String[] split = line.split(" ");
			String[] dateSplit = split[0].split("/");
			int month = Integer.parseInt(dateSplit[0]), day = Integer.parseInt(dateSplit[1]);
			result.add(new BodyWeightEntry(2020, month, day, Float.parseFloat(split[1])));
		}
		return result;
	}
}
