package io.friday;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import users.User;

/**
 * Each user has one instance of FridayIO which manages input and output sources.
 * 
 * @author Patrick Wamsley
 */
public class FridayIO {
	
	protected User user;
	protected FridayOutputSource currentOutput;
	protected Set<FridayInputSource> inputs;
	private FridayInput<?> nextInput;
	
	public FridayIO(User user, FridayOutputSource initialOutput, FridayInputSource... inputs) {
		this.user = user;
		this.currentOutput = initialOutput;
		this.inputs = new HashSet<>();
		this.inputs.addAll(Arrays.asList(inputs));
	}
	
	public FridayIO(User user, FridayOutputSource initialOutput, Set<FridayInputSource> inputs) {
		this.user = user;
		this.currentOutput = initialOutput;
		this.inputs = inputs;
	}
	
	public FridayIO(User user) {
		this.user = user;
	}
	
	public void sendOutput(FridayOutput<?> output) {
		currentOutput.send(user, output);
	}
	
	public void changeOutput(FridayOutputSource output) {
		currentOutput = output;
	}
	
	public boolean addInputSource(FridayInputSource source) {
		return inputs.add(source);
	}
	
	public boolean removeInputSource(FridayInputSource source) {
		return inputs.remove(source);
	}
	
	/**
	 * @return true if an input was receieved and responded to, false otherwise
	 */
	public boolean waitForNextAndRespond() {
		for (FridayInputSource input : inputs) {
			if (input.waitForNextInput()) {
				nextInput = input.getNext();
				sendOutput(user.getResponseManager().getResponse(nextInput));
				return true;
			}
		}
		return false;
	}
	
	public Set<FridayInputSource> getInputSources() {
		return inputs;
	}
	
	public FridayOutputSource getOutputSource() {
		return currentOutput;
	}
	
	@Override
	public String toString() {
		return "FridayIO: outputing to: " + currentOutput + " and listening for inputs from: " + inputs;
	}

}
