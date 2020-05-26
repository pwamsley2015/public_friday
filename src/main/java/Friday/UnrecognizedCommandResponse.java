package Friday;

public class UnrecognizedCommandResponse implements FridayAction {

	@Override
	public String execute() {
		return "Sorry, I don't understand that command. Make sure to use appropriate keywords.";
	}

}
