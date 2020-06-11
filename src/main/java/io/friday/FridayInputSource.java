package io.friday;

/**
 * The parent class of all Inputs to Friday.
 * 
 * @author Patrick Wamsley
 */
public abstract class FridayInputSource {
	
	protected boolean isInitalized;
	
	public abstract void init();
	public abstract void close();
	
	/**
	 * @return true if the next input has been recieved, false otherwise
	 */
	public abstract boolean waitForNextInput();

	/**
	 * @return The next available FridayInput from this source, if there is one. Null otherwise. 
	 */
	public abstract FridayInput<?> getNext();
	
	public abstract String toString();
}
