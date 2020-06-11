package io.friday;

public class FridayOutput<T> {

	private T output;
	
	public FridayOutput(T val) {
		this.output = val;
	}
	
	public T get() {
		return output;
	}
}
