package data.training;

import java.io.Serializable;

public class TrainingSet implements TrainingWritable, Serializable {
	
	private static final long serialVersionUID = 5386202561218308564L;
	
	private int reps;
	private float prescribedRpe;
	private float actualRpe;
	private float prescribedPercentage1Rm;
	private float estimated1Rm;
	private int weight;
	private String notes;
	
	private boolean preformed; 
	
	public TrainingSet(int reps, float prescripedRpe) {
		this.reps = reps;
		this.prescribedRpe = prescripedRpe;
		this.prescribedPercentage1Rm = RPE.getPercentage(prescripedRpe, reps);
		this.preformed = false;
	}
	
	public TrainingSet(int reps, float percentage1Rm, boolean ignore) {
		this.reps = reps;
		this.prescribedPercentage1Rm = percentage1Rm;
		this.preformed = false;
	}
	
	public void didSet(int weight, float actualRpe, String notes) {
		this.actualRpe = actualRpe;
		didSet(weight, notes);
		this.estimated1Rm = RPE.getEstimatedOneRepMax(weight, actualRpe, reps);
	}
	
	public void didSet(int weight, String notes) {
		this.weight = weight;
		this.notes = notes;
		this.estimated1Rm = weight / prescribedPercentage1Rm;
		preformed = true;
	}
	
	public void didSet(int weight, float actualRpe) {
		didSet(weight, actualRpe, "");
	}
	
	public float getE1RM() {
		return estimated1Rm;
	}
	
	@Override
	public String toString() {
		return prescripition() + (preformed ? " [actually @" + actualRpe +  "." + notes + "]" : "");
	}
	
	public String prescripition() {
		return reps + "@" + prescribedRpe;
	}

	public int getReps() {
		return reps;
	}

	public float getPrescribedRpe() {
		return prescribedRpe;
	}

	public float getActualRpe() {
		return actualRpe;
	}

	public float getPrescribedPercentage1Rm() {
		return prescribedPercentage1Rm;
	}

	public int getWeight() {
		return weight;
	}

	public String getNotes() {
		return notes;
	}

	public boolean isPreformed() {
		return preformed;
	}
	
}
