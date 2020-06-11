package io.friday.implementations;

import com.twilio.FridayRecievedTextMessage;
import com.twilio.TwilioHelper;

import io.friday.FridayInput;
import io.friday.FridayInputSource;
import users.UsersLookup;

public class TextInput extends FridayInputSource {

	@Override
	public void init() {
		TwilioHelper.init();
		TwilioHelper.recieveAndProcessText();
	}

	@Override
	public void close() {}

	@Override
	public boolean waitForNextInput() {
		return TwilioHelper.hasNext();
	}

	@Override
	public FridayInput<FridayRecievedTextMessage> getNext() {
		FridayRecievedTextMessage rec = TwilioHelper.getNext();
		TwilioHelper.recieveAndProcessText();
		return new FridayInput<FridayRecievedTextMessage>(rec, UsersLookup.getUser(rec.getFrom()));
	}

	@Override
	public String toString() {
		return "Texts to Friday";
	}

}
