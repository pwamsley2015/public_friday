package data.training;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class TrainingBlock implements TrainingWritable, Serializable {

	private static final long serialVersionUID = -5007762439094648986L;

	public static enum BlockObjective {STRENGTH_DEV, STRENGTH_DISPLAY, HYPERTROPHY, PIVOT, GPP}
	
	public final BlockObjective objective;
	private ArrayList<TrainingWeek> weeks;
	private boolean completed = false;
	
	public TrainingSession getCurrSession() {
		TrainingWeek currWeek = getCurrWeek();
		for (TrainingSession s : currWeek.getSessions()) {
			if (!s.isSessionCompleted()) {
				return s;
			}
		}
		return currWeek.getSessions().get(currWeek.getSessions().size() - 1);
	}
	
	public TrainingWeek getCurrWeek() {
		for (TrainingWeek week : weeks) {
			if (!week.isCompleted()) {
				return week;
			}
		}
		return weeks.get(weeks.size() - 1);
	}
	
	public TrainingBlock(BlockObjective objective) {
		this.objective = objective;
		this.weeks = new ArrayList<>();
	}
	
	public boolean addWeeks(TrainingWeek... weeks) {
		return this.weeks.addAll(Arrays.asList(weeks));
	}
	
	public boolean isCompleted() {
		return completed;
	}
	
	public void completed() {
		completed = true;
	}
	
	public String writeBlock() {
		StringBuilder builder = new StringBuilder();
		builder.append(objective.toString() + (completed ? "[Completed.]" : "[Not completed.]") + "\n\n");
		
		for (TrainingWeek week : weeks) {
			builder.append(week.writeWeek() + "\n");
		}
		return builder.toString();
	}
}
