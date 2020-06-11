package Friday;

import java.util.Date;

import io.friday.FridayOutput;
import users.User;

public class ScheduledActionWrapper<T extends FridayAction> extends ScheduledFridayAction {
	private static final long serialVersionUID = 2772213771725975964L;
	
	final T action;

	public ScheduledActionWrapper(T action, Date time, User user) {
		super(time, user);
		this.action = action;
	}

	@Override
	public FridayOutput<?> execute() {
		return action.execute();
	}

}
