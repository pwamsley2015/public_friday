package io.friday.implementations;

import io.friday.FridayIO;
import users.User;

public class ConsoleIO extends FridayIO {
	
	public ConsoleIO(User user) {
		super(user, new ConsoleOutput(), new ConsoleInput(user));
	}

}
