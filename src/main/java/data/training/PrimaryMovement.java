package data.training;

public enum PrimaryMovement {
	SQUAT, BENCH, DEADLIFT, ROW, PRESS, PULL_UP, GPP_CARDIO_LISS, GPP_CARDIO_HIIT, GPP_RESISTANCE_TRAINING;
	
	public static String getPrimaries() {
		StringBuilder s = new StringBuilder();
		for (PrimaryMovement p : PrimaryMovement.values()) {
			s.append(p.toString() + "\n");
		}
		return s.toString();
	}
}
