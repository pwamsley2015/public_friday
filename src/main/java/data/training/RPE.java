package data.training;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import data.DataUtil;

/**
 * Util class which helps with RPE math.
 * 
 * @author Patrick Wamsley
 */
public class RPE {
	
	private static final float[] rpe6	= {-1f, .87f, .84f, .80f, .79f, .75f, .74f, .70f, .67f, .66f, .63f},
								 rpe6_5 = {-1f, .88f, .85f, .82f, .8f, .77f, .75f, .72f, .69f, .67f, .64f},
								 rpe7	= {-1f, .89f, .86f, .84f, .81f, .79f, .76f, .74f, .71f, .68f, .65f},
								 rpe7_5 = {-1f, .91f, .88f, .85f, .82f, .8f, .77f, .75f, .72f, .69f, .67f}, 
								 rpe8	= {-1f, .92f, .89f, .86f, .84f, .81f, .79f, .76f, .74f, .71f, .68f},
								 rpe8_5 = {-1f, .94f, .91f, .88f, .85f, .82f, .8f, .77f, .75f, .72f, .69f},
								 rpe9	= {-1f, .96f, .92f, .89f, .86f, .84f, .81f, .79f, .76f, .74f, .71f},
								 rpe9_5 = {-1f, .98f, .94f, .91f, .88f, .85f, .82f, .8f, .77f, .75f, .72f},
								 rpe10	= {-1f, 1f, .96f, .92f, .89f, .86f, .84f, .81f, .79f, .76f, .74f};
	
	private static final LinkedHashMap<Float, float[]> RPE_TABLE; 
	static {
		RPE_TABLE = new LinkedHashMap<>();
		RPE_TABLE.put(6f, rpe6);
		RPE_TABLE.put(6.5f, rpe6_5);
		RPE_TABLE.put(7f, rpe7);
		RPE_TABLE.put(7.5f, rpe7_5);
		RPE_TABLE.put(8f, rpe8);
		RPE_TABLE.put(8.5f, rpe8_5);
		RPE_TABLE.put(9f, rpe9);
		RPE_TABLE.put(9.5f, rpe9_5);
		RPE_TABLE.put(10f, rpe10);
	}
	
	/**
	 * 
	 * @param Weight loaded on the set
	 * @param rpe of the set
	 * @param reps - the number of reps lifted this set
	 * @return Estimated one rep max, rounded to nearest multible of 5.
	 * @throws IllegalArgumentException
	 */
	public static float getEstimatedOneRepMax(float weight, float rpe, int reps) throws IllegalArgumentException {
		return Math.round(weight / getPercentage(rpe, reps));
	}
	
	public static float getPercentage(float rpe, int reps) throws IllegalArgumentException {
		if (!isOnTable(rpe, reps)) {
//			throw new IllegalArgumentException("RPE/Reps given not on RPE table");
			//TODO: better manage this lol
			System.out.println("getPercentage returning 0");
			return 0.0f;
		} 
		
		return RPE_TABLE.get(rpe)[reps];
	}
	
	public static class ObjectWithDouble {
		private Object o;
		private double d;
		private ObjectWithDouble(Object o, double d) {
			this.o = o;
			this.d = d;
		}
	}
	
	public static float getRPE(int reps, float percen) {
		return (Float)RPE_TABLE.keySet().stream()
									.map(r -> new ObjectWithDouble(r, Math.abs(RPE_TABLE.get(r)[reps] - percen)))
									.min((o1, o2) -> Double.compare(o1.d, o2.d))
									.get().o;
	}
	
	
	
	private static boolean isOnTable(float rpe, int reps) {
		return rpe >= 6 && rpe <= 10 && reps > 0 && reps <= 10;
	}

}
