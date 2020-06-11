package io.friday.implementations;

import java.util.Scanner;

import io.friday.FridayInput;
import io.friday.FridayInputSource;
import users.User;

public class ConsoleInput extends FridayInputSource {
	
	private Scanner s;
	private User user;
	
	public ConsoleInput(User user) {
		this.user = user;
	}

	@Override
	public void init() {
		s = new Scanner(System.in);
		isInitalized = true;
	}

	@Override
	public void close() {
		s.close();
	}

	@Override
	public boolean waitForNextInput() {
		return s.hasNext();
	}

	@Override
	public FridayInput<String> getNext() {
		return new FridayInput<>(s.nextLine(), user);
	}

	@Override
	public String toString() {
		return "System.in";
	}

}
