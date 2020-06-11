package Friday;

import java.io.Serializable;
import java.util.Date;

import users.User;

public abstract class ScheduledFridayAction implements FridayAction, Comparable<ScheduledFridayAction>, Serializable {

	private static final long serialVersionUID = -4106428777354684049L;
	
	private Date scheduledTime;
	public final User user;
	private boolean cancel = false;
	
	public ScheduledFridayAction(Date scheduledTime, User user) {
		this.scheduledTime = scheduledTime;
		this.user = user;
	}
	
	public Date getScheduledTime() {
		return scheduledTime;
	}
	
	@Override
	public int compareTo(ScheduledFridayAction other) {
		return scheduledTime.compareTo(other.scheduledTime);
	}
	
	public boolean isCanceled() {
		return cancel;
	}
	
	public void cancel() {
		cancel = true;
	}
}
