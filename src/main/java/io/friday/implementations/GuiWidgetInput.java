package io.friday.implementations;

import Friday.Friday;
import io.friday.FridayInput;
import io.friday.FridayInputSource;

public class GuiWidgetInput extends FridayInputSource {
	
	private FridayInput<String> next;
	
	public GuiWidgetInput() {
	}

	@Override
	public void init() {}

	@Override
	public void close() {}

	@Override
	public boolean waitForNextInput() {
		return next != null;
	}

	@Override
	public FridayInput<?> getNext() {
		FridayInput<?> n = next;
		next = null;
		return n;
	}
	
	public void recieveInput(String text) {
		next = new FridayInput<>(text, Friday.primaryUser);
	}

	@Override
	public String toString() {
		return "Gui_widgets input";
	}

}
