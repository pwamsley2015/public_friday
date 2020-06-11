package io.friday;

import users.User;

public class FridayInput<T> {
	
	private User user;
	private T input;
	
	public FridayInput(T value, User user) {
		this.input = value;
	}
	
	public T getValue() {
		return input;
	}

}
