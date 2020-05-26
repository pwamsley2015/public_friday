package data.training;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class TrainingSession implements TrainingWritable, Serializable {

	private static final long serialVersionUID = -4918459307461310205L;
	
	private LinkedHashMap<TrainingMovement, ArrayList<TrainingSet>> workout = new LinkedHashMap<>();
	private Date startTime, endTime;
	private float sessionRPE;
	private String notes;
	
	transient private TrainingMovement currentMovement;
	transient private Iterator<TrainingMovement> iterator;
	
	/**
	 * This session's day of the {@code TrainingWeek}
	 */
	public final int dayNum;
	
	public TrainingSession(int dayOfTheTrainingWeek) {
		this.dayNum = dayOfTheTrainingWeek;
	}
	
	//--inefficient but easy set access methods, will optimize into a queue at some point TODO--//
	public boolean currentMovementHasNextSet() {
		for (TrainingSet set : workout.get(currentMovement)) {
			if (!set.isPreformed()) {
				return true;
			}
		}
		return false;
	}
	
	public TrainingSet getNextSet() {
		for (TrainingSet set : workout.get(currentMovement)) {
			if (!set.isPreformed()) {
				return set;
			}
		}
		return null; 
	}
	
	public String getNextMovementPrescripition() {
		currentMovement = iterator.next();
		String m = currentMovement == null ? "Placeholder" : currentMovement.toString();
		return m + ": " + workout.get(currentMovement).toString();
	}
	
	public void addMovementPrescripition(TrainingMovement movement, ArrayList<TrainingSet> prescripition) {
		if (workout.get(movement) == null) {
			workout.put(movement, prescripition);
		} else {
			workout.get(movement).addAll(prescripition);
		}
	}
	
	public void addMovementPrescripition(TrainingMovement movement, TrainingSet prescripition) {
		if (workout.get(movement) == null) {
			ArrayList<TrainingSet> l = new ArrayList<>();
			l.add(prescripition);
			workout.put(movement, l);
		} else {
				workout.get(movement).add(prescripition);
		}
	}
	
	public boolean isSessionCompleted() {
		for (TrainingMovement m : workout.keySet()) {
			for (TrainingSet set : workout.get(m)) {
				if (!set.isPreformed()) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void beginSession() {
		startTime = new Date();
		iterator = workout.keySet().iterator();
	}
	
	public void sessionComplete(float sessionRpe, String notes) {
		endTime = new Date();
		this.sessionRPE = sessionRpe;
		this.notes = notes;
	}
	
	public String getNotes() {
		return notes;
	}
	
	public float getWorkloadAU() {
		return sessionRPE * getDuration();
	}
	
	public float getSessionRpe() {
		return sessionRPE;
	}
	
	public int getDuration() {
		if (startTime != null && endTime != null) {
			return milisecondsToMinutes(endTime.getTime() - startTime.getTime());
		} else {
			throw new RuntimeException("Attempted to ge the duration of a training session that hasn't begun or concluded.");
		}
	}
	
	@Override
	public String toString() {
		return workout.toString();
	}
	
	private int milisecondsToMinutes(long milis) {
		return Math.round(milis / 60000);
	}

	public String writeSession() {
		StringBuilder builder = new StringBuilder("Day " + dayNum + "\n");
		for (TrainingMovement movement : workout.keySet()) {
			List<TrainingSet> sets = workout.get(movement);
			if (movement == null) {
				TrainingMovement dummy = new TrainingMovement("placeholder", PrimaryMovement.GPP_CARDIO_LISS, false);
				movement = dummy;
			}
			builder.append("" + movement.variation + ": ");
			for (TrainingSet s : sets) {
				builder.append(s.getReps() + "@" + s.getPrescribedRpe() + " ");
			}
			builder.append("\nNotes:");
			for (TrainingSet s : sets) {
				if (s.isPreformed()) {
					builder.append(s.getWeight() + "@" + s.getActualRpe() + " " + s.getNotes() + " ");
				}
			}
			builder.append("\n");
		}
		return builder.toString();
	}
}
