package gui;

import io.friday.implementations.GuiIO;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;

@SuppressWarnings("restriction")
public class FridayConsole extends ScrollPane {
	
	private TextArea textArea;
	
	private String currMessage;
	private int nextMessageIndex = 0;
	
	public FridayConsole(final GuiIO friday) {
		setPrefSize(600, 600);
		textArea = new TextArea();
		textArea.setPrefSize(600, 600);
		textArea.setEditable(true);
		textArea.setOnKeyPressed(keyPress -> {
			if (keyPress.getCode() == KeyCode.ENTER) {
				currMessage = textArea.getText().substring(nextMessageIndex);
				friday.recieveInput(currMessage);
				nextMessageIndex += currMessage.length() + 1;
			} 
		});
		setContent(textArea);
	}
	
	@SuppressWarnings("nls")
	public void println(String s/*, boolean withPrompt*/) {
		int newAppendAllignment = 3;
		textArea.appendText(s + "\n>\t");
//		if (withPrompt) {
//			textArea.appendText(">\t");
//			newAppendAllignment += 2;
//		}
		nextMessageIndex += s.length() + newAppendAllignment;
	}

}
