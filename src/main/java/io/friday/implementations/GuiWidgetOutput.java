package io.friday.implementations;

import io.friday.FridayOutput;
import io.friday.FridayOutputSource;
import users.User;

public class GuiWidgetOutput extends FridayOutputSource {

	private final GuiIO parent;
	
	public GuiWidgetOutput(GuiIO parent) {
		this.parent = parent;
	}
	
	@Override
	public void init() {}

	@Override
	public void close() {}

	@Override
	public boolean send(User user, FridayOutput<?> output) {
		try {
			parent.sendOutput(output.get().toString());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String toString() {
		return "Gui_widget output";
	}

}
