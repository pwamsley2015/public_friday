package io.friday.implementations;

import io.friday.FridayIO;
import users.User;

public class TextIO extends FridayIO {
	
	public TextIO(User user) {
		super(user, new TextOutput(), new TextInput());
	}

}
