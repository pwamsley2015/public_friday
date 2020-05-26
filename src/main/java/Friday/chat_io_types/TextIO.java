package Friday.chat_io_types;

import com.twilio.TwilioHelper;

import data.Contacts;

public class TextIO extends FridayIO {
	
	@Override
	public boolean isWaitingForResponse() {
		return TwilioHelper.isWaitingForResponse();
	}
	
	@Override
	public void sendOutput(String output) {
		TwilioHelper.sendText(Contacts.getMe(), output);
	}

	@Override
	public void waitAndRespond() {
		TwilioHelper.recieveAndProcessText();
	}

	@Override
	public void init() {
		TwilioHelper.init();
	}

}
