package Friday;

import io.friday.FridayOutput;

public class UnrecognizedCommandResponse implements FridayAction {

	@Override
	public FridayOutput<String> execute() {
		return new FridayOutput<>("Sorry, I don't understand that command. Make sure to use appropriate keywords.");
	}

}
