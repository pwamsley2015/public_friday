package data.training;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class TrainingWeek implements TrainingWritable, Serializable {
	
	private static final long serialVersionUID = 7508408402654954809L;

	public static enum Stress {LOW, MEDIUM, HIGH}
	public static enum Type {PIVOT, DEVELOPEMENTAL, INTENSIFICATION, GPP, PEAK}
	
	private ArrayList<TrainingSession> sessions;
	public final Stress STRESS;
	public final Type TYPE;
	private boolean completed;
	
	/**
	 * This week's num in the context of the {@code TrainingBlock}
	 */
	public final int weekNum; 
	
	public TrainingWeek(Stress stress, Type type, int weekNum) {
		this.STRESS = stress;
		this.TYPE = type;
		this.sessions = new ArrayList<>();
		this.completed = false;
		this.weekNum = weekNum;
	}
	
	public boolean addSessions(TrainingSession... sessions) {
		return this.sessions.addAll(Arrays.asList(sessions));
	}
	
	public boolean isCompleted() {
		return completed;
	}
	
	public void completed() {
		completed = true;
	}

	public String writeWeek() {
		StringBuilder builder = new StringBuilder();
		builder.append("Week " + weekNum + "[" + STRESS + " stress] [" + TYPE.toString() + "]" + (completed ? " [Completed]" : "[Not Completed]"));
		builder.append("\n\n"); 
		int i = 0;
		for (TrainingSession s : sessions) {
			builder.append("Day " + ++i + ":");
			builder.append(s.writeSession());
		}
		builder.append("\n");
		return builder.toString();
	}

	public ArrayList<TrainingSession> getSessions() {
		return sessions;
	}

}
