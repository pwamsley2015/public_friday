package io.friday.implementations;

import java.util.HashSet;

import Friday.Friday;
import gui.FridayConsole;
import io.friday.FridayIO;

public class GuiIO extends FridayIO {
	
	private FridayConsole console = new FridayConsole(this);
	private boolean isWaitingForResponse = true;
	
	public GuiIO() {
		super(Friday.primaryUser);
		super.currentOutput = new GuiWidgetOutput(this);
		super.inputs = new HashSet<>();
		super.inputs.add(new GuiWidgetInput());
	}
	
	public void recieveInput(String input) {
		isWaitingForResponse = false;
		inputs.stream().filter(x -> x instanceof GuiWidgetInput).findFirst().ifPresent(is -> ((GuiWidgetInput)is).recieveInput(input));
	}
	
	public void init() {
		isWaitingForResponse = true;
	}

	public void sendOutput(String output) {
		if (isWaitingForResponse) {
			console.println("*\n" + output);
		} else {
			console.println(output);
		}
		isWaitingForResponse = true;
	}
	
	public FridayConsole getConsole() {
		return console;
	}

}
