package io.friday.implementations;

import java.io.IOException;

import com.twilio.TwilioHelper;
import com.twilio.type.PhoneNumber;

import io.Scrips;
import io.friday.FridayOutput;
import io.friday.FridayOutputSource;
import users.User;

public class TextOutput extends FridayOutputSource {

	@Override
	public void init() {
		TwilioHelper.init();
	}

	@Override
	public void close() {
		try {
			Scrips.killAllNgrok();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean send(User user, FridayOutput<?> output) {
		if (user == null || output == null) {
			return false;
		}
		PhoneNumber n = user.getPhoneNumber();
		if (n == null || !(output.get() instanceof String)) {
			return false;
		}
		TwilioHelper.sendText(n, (String)output.get());
		return false;
	}

	@Override
	public String toString() {
		return "Texts from Friday";
	}

}
