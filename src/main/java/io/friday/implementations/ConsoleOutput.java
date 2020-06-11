package io.friday.implementations;

import io.friday.FridayOutput;
import io.friday.FridayOutputSource;
import users.User;

public class ConsoleOutput extends FridayOutputSource {

	@Override
	public void init() {}

	@Override
	public void close() {}

	@Override
	public boolean send(User user, FridayOutput<?> output) {
		System.out.println(output.get().toString());
		return true;
	}

	@Override
	public String toString() {
		return "System.out";
	}

}
