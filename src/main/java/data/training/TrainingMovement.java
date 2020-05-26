package data.training;

import java.io.Serializable;

public class TrainingMovement implements TrainingWritable, Serializable {
	
	private static final long serialVersionUID = -1913163321517286444L;
	
	public final String variation;
	public final PrimaryMovement type;
	public final boolean isPrimary;
	
	public TrainingMovement(String variation, PrimaryMovement type, boolean isPrimary) {
		this.variation = variation;
		this.type = type;
		this.isPrimary = isPrimary;
	}
	
	@Override 
	public int hashCode() {
		return variation.toLowerCase().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TrainingMovement)) {
			return false;
		}
		return ((TrainingMovement)obj).hashCode() == this.hashCode();
	}
	
	@Override
	public String toString() {
		return variation;
	}

}
