package io.friday;

import users.User;

public abstract class FridayOutputSource {
	
	protected boolean isInitalized;
	
	public abstract void init();
	public abstract void close();

	public abstract boolean send(User user, FridayOutput<?> output);
	
	public abstract String toString();
}
