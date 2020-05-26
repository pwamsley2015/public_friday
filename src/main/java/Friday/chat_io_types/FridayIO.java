package Friday.chat_io_types;

/**
 * Defines a generic IO for Friday to use. 
 * 
 * @author Patrick Wamsley
 */
public abstract class FridayIO {
	
	protected boolean isWaitingForResponse;
	
	/**
	 * Handles any action(s) needed to prepare this object for IO.
	 */
	public abstract void init();

	/**
	 * Have Friday send output
	 */
	public abstract void sendOutput(String output);
	
	/**
	 * Have Friday wait for a message from the user, and then respond. 
	 * @return
	 */
	public abstract void waitAndRespond();
	
	public boolean isWaitingForResponse() {
		return isWaitingForResponse;
	}
}
