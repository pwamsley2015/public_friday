package data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class DataUtil {
	
	public static DataSource getMax(List<? extends DataSource> entries) {
		DataSource max = entries.get(0);
		for (int i = 1; i < entries.size(); i++) {
			if (Double.compare(max.getValue(), entries.get(i).getValue()) < 0) {
				max = entries.get(i);
			}
		}
		return max;
	}
	
	public static DataSource getMin(List<? extends DataSource> entries) {
		DataSource min = entries.get(0);
		for (int i = 1; i < entries.size(); i++) {
			if (Double.compare(min.getValue(), entries.get(i).getValue()) > 0) {
				min = entries.get(i);
			}
		}
		return min;
	}
	
	public static double getAverage(double... values) {
		return sum(values) / values.length;
	}
	
	public static double sum(double... values) {
		double sum = 0;
		for (double d : values) {
			sum += d;
		}
		return sum;
	}
	
	public static double average(List<? extends DataSource> entries) {
		OptionalDouble safeAnswer = entries.stream().mapToDouble(entry -> entry.getValue()).average(); 
		return safeAnswer.isPresent() ? safeAnswer.getAsDouble() : 0;
	}
	
	public static List<Double> getChangeInValues(List<? extends DataSource> entries) {
		List<Double> deltas = new ArrayList<>();
		if (entries.size() < 2) {
			deltas.add(0.0);
			return deltas;
		} 
		for (int i = 0; i < entries.size() - 1; i++) {
			double deltaV = entries.get(i + 1).getValue() - entries.get(i).getValue();
			deltas.add(deltaV);
		}
		return deltas;
	}
	
	public static List<DataDouble> getNthOrderChangeInValues(List<? extends DataSource> entries, int order) {
		List<DataDouble> deltas = entries.stream()
									 .map(e -> new DataDouble(e.getValue()))
									 .collect(Collectors.toList());
		if (entries.size() < 2) {
			deltas.clear();
			deltas.add(new DataDouble(0.0));
			return deltas;
		} 
		
		for (int n = 1; n <= order; n++) {
			for (int i = 0; i < deltas.size() - 1; i++) {
				deltas.set(i, new DataDouble(deltas.get(i + 1).getValue() - deltas.get(i).getValue()));
			}
		}
		
		//set last change value (which we don't have data for) to closest guess (it's left neighbor)
		deltas.set(deltas.size() - 1, deltas.get(deltas.size() - 2));
		
		return deltas;
	}
	
	public static List<DataDouble> getLocalizedRunningAverages(List<? extends DataSource> entries, int bucketSize) {
		
		if (entries.isEmpty()) {
			return Arrays.asList(new DataDouble(0.0)); 
		} else if (bucketSize < 1 || bucketSize > entries.size()) {
			throw new IllegalArgumentException("Invalid bucket size: " + bucketSize + "on list size: " + entries.size());
		}
		
		List<DataDouble> runningLocalAverages = new ArrayList<>();
		for (int i = 0; i < entries.size(); i++) {
			if (i < bucketSize) {
				double sum = 0;
				for (int j = 0; j <= i; j++) {
					sum += entries.get(j).getValue();
				}
				runningLocalAverages.add(new DataDouble(sum / (i + 1)));
			} else {
				double sum = 0;
				for (int j = 0; j < bucketSize; j++) {
					sum += entries.get(i - j).getValue();
				}
				runningLocalAverages.add(new DataDouble(sum / bucketSize));
			}
		}
		return runningLocalAverages;
	}
	
public static List<Double> rawGetLocalizedRunningAverages(List<Double> entries, int bucketSize) {
		
		if (entries.isEmpty()) {
			return Arrays.asList(0.0); 
		} else if (bucketSize < 1 || bucketSize > entries.size()) {
			throw new IllegalArgumentException("Invalid bucket size: " + bucketSize + "on list size: " + entries.size());
		}
		
		List<Double> runningLocalAverages = new ArrayList<>();
		for (int i = 0; i < entries.size(); i++) {
			if (i < bucketSize) {
				double sum = 0;
				for (int j = 0; j <= i; j++) {
					sum += entries.get(j);
				}
				runningLocalAverages.add(sum / (i + 1));
			} else {
				double sum = 0;
				for (int j = 0; j < bucketSize; j++) {
					sum += entries.get(i - j);
				}
				runningLocalAverages.add(sum / bucketSize);
			}
		}
		return runningLocalAverages;
	}

	public static class DataDouble implements DataSource {
		private Double val = null;
		public DataDouble(Double d) {
			val = d;
		}
		@Override
		public double getValue() {
			return val;
		}
		@Override
		public String toString() {
			return Double.toString(val);
		}
	}
}
