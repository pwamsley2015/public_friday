package Friday;

import io.friday.FridayOutput;

public interface FridayAction {
	
	/**
	 * Executes the task and returns Friday's response
	 */
	public FridayOutput<?> execute();
}
