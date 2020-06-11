package gui.widgets.TrainingProgramBuilder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

import data.training.RPE;
import data.training.TrainingMovement;
import data.training.TrainingSession;
import data.training.TrainingSet;
import data.training.Variations;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
@SuppressWarnings("restriction")
public class TrainingDayPane extends VBox {
	
	private static final String SHORTCUT_START_REGEX		=	"[/\\:]",
								SINGLE_ARGS_REGEX			= "\\s*\\d*",
								RAMP_ARGS_REGEX				= "\\s*\\d*\\s+\\d*\\s+\\d*",
								PERCEN_PREV_SET_ARGS 		= "\\s+\\d+\\s+\\d+\\s+\\d+",
								ADD_NEW_VAR					= "Add New Variation...",
								PRESCRIPITION_VALID_REGEX 	= "\\d+@\\d(x\\d)?([\\s,\\d+@\\d(x\\d)?])+";
								
								
	
	private static final char	SINGLE_SHORTCUT = 's',	// /s <n> -> 1@n 
								RAMP_SHORTCUT 	= 'r',	// /r <n> <a> <b> -> n@a, n@a+1+...n@b
								PERCEN_SHORTCUT	= 'p',	// /p <p> <r> <s> -> r@RPExs; RPE = rpe_from(p * previous_set)
								END_SHORTCUT	= '\t'; 
								
								
	
	private Label dayLabel;
	private List<ComboBox<TrainingMovement>> movementSelections;
	private List<TextArea> inputs;
	
	public TrainingDayPane(int day, int movementsPerDay) {
		
		setPadding(new Insets(10, 20, 10, 20)); //todo: move to css
		
		dayLabel = new Label("Day " + day + ":");
		movementSelections = new ArrayList<>(movementsPerDay);
		inputs = new ArrayList<>(movementsPerDay);
		
		ObservableList<TrainingMovement> l = FXCollections.observableArrayList(Variations.getAllMovements());
		TrainingMovement dummyMovement = new TrainingMovement(ADD_NEW_VAR, null, false);
		l.add(dummyMovement);
		for (int i = 0; i < movementsPerDay; i++) {
			ComboBox<TrainingMovement> movementSelection = new ComboBox<>(l);
			movementSelection.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
				if (newSelection.toString().equals(ADD_NEW_VAR)) {
					new NewVariationPopup(movementSelection);
				}
			});
			final TextArea input = new TextArea();
			input.setPrefHeight(20);
			input.setPrefWidth(200);
			try {
				//keyboard shortcuts
				input.addEventFilter(KeyEvent.KEY_RELEASED, e -> {
					if (e.isShortcutDown()) {
						handleCommand(e.getCode(), input, e.isShiftDown());
						e.consume();
					}
				});
				//inline text shortcuts
				input.textProperty().addListener((observ, oldVal, newVal) -> specialKeyboardUseListener(newVal.charAt(newVal.length() - 1), input));
			} catch (StringIndexOutOfBoundsException s) {}
			movementSelections.add(movementSelection);
			inputs.add(input);
		}
		getChildren().addAll(dayLabel);
		for (int i = 0; i < movementsPerDay; i++) {
			getChildren().addAll(movementSelections.get(i), inputs.get(i));
		}
	}
	
	private void handleCommand(KeyCode code, TextArea input, boolean shiftDown) {
		String repsAtRpeRegex = "\\d+@\\d+(.d+)?";
		String repsAtRpeTimesSetRegex = repsAtRpeRegex + "x\\d+";
		switch (code) {
			//add top set
			case UP: case KP_UP:
				editTopSet(input, repsAtRpeTimesSetRegex, true);
				break;
			//remove top set
			case DOWN: case KP_DOWN:
				editTopSet(input, repsAtRpeTimesSetRegex, false);
				break;
			//shift RPE down / shift all sets RPE down
			case LEFT: case KP_LEFT:
				if (shiftDown) {
					editAllRpe(input, false);
				} else {
					editLastRpe(input, repsAtRpeTimesSetRegex, repsAtRpeRegex, false);
				}
				break;
			//shift RPE up / shift all sets RPE down
			case RIGHT: case KP_RIGHT:
				if (shiftDown) {
					editAllRpe(input, true);
				} else {
					editLastRpe(input, repsAtRpeTimesSetRegex, repsAtRpeRegex, true);
				}
				break;
				
		}
	}
	
	private void editAllRpe(TextArea input, boolean up) {
		
		final StringProperty tp = input.textProperty();
		
		StringBuilder result = new StringBuilder();
		StringBuilder rpeString = new StringBuilder();
		boolean readingRpe = false;
		DecimalFormat f = new DecimalFormat("##.#");
		for (int i = 0; i < tp.getValue().length(); i++) {
			char toAdd = tp.getValue().charAt(i);
			if (readingRpe) {
				if (Character.isDigit(toAdd) || toAdd == '.') {
					rpeString.append(toAdd);
					if (i == tp.getValue().length() - 1) {
						float rpe = Float.parseFloat(rpeString.toString()) + (up ? 1 : -1);
						String clean = f.format(rpe);
						result.append(clean.contains(".0") ? rpe : clean);
					}
				} else {
					readingRpe = false;
					float rpe = Float.parseFloat(rpeString.toString()) + (up ? 1 : -1);
					String clean = f.format(rpe);
					result.append((clean.contains(".0") ? rpe : clean) + "" + toAdd);
				}
			} else {
				result.append(toAdd);
			}
			
			if (toAdd == '@') {
				rpeString = new StringBuilder();
				readingRpe = true;
			}
		}
		
		Platform.runLater(() -> {
			tp.setValue(result.toString());
		});
	}
	
	private float extractRpe(String input) {
		String afterAt = input.substring(input.indexOf("@"));
		Pattern p = Pattern.compile("\\d+(.\\d+)");
		Matcher m = p.matcher(afterAt);
		return Float.parseFloat(m.group());
	}
	
	private void editLastRpe(TextArea input, String repsAtRpeTimesSetsRegex, String repsAtRpeRegex, boolean up) {
		final StringProperty tp = input.textProperty();
		String lastToken = getLast(tp.getValue(), repsAtRpeTimesSetsRegex);
		String xSets = "";
		//with sets 
		if (!lastToken.isEmpty()) {
			xSets = lastToken.substring(lastToken.indexOf("x"));
			lastToken = lastToken.substring(0, lastToken.length() - xSets.length());
		} else {
			lastToken = getLast(input.textProperty().getValue(), repsAtRpeRegex);
		}
		if (!lastToken.isEmpty()) {
			String sRpe = lastToken.split("@")[1];
			float rpe = Float.parseFloat(sRpe);
			
			final String fXSets = xSets;
			Platform.runLater(() -> { 
				tp.setValue((tp.getValue().substring(0, tp.getValue().length() - (sRpe.length() + fXSets.length()))
										+ (up ? (rpe + 1) : (rpe - 1)))
										.replaceAll(".0", "") + fXSets);
				});
		} 
	}
	
	private void editTopSet(TextArea input, String repsAtRpeTimesSetRegex, boolean up) {
		String lastToken = getLast(input.textProperty().getValue(), repsAtRpeTimesSetRegex);
		//no x<sets>
		if (lastToken.equals("")) {
			if (up) {
				Platform.runLater(() -> input.textProperty().setValue(input.textProperty().get() + "x2"));
			}
		} else {
			String[] split = lastToken.split("x");
			int sets = Integer.parseInt(split[1]);
			Platform.runLater(() -> input.textProperty().setValue(input.textProperty().getValue()
																	.substring(0, input.textProperty().getValue().length() - (split[1].length())) + (sets + (up ? 1 : -1))));
		}
	}
	
	private String getLast(String input, String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		String result = "";
		while (m.find()) {
			result = m.group();
		}
		
		return result;
	}
	
	private void specialKeyboardUseListener(final char newKey, final TextArea textInput) {
		if (isCommandPrompt(newKey)) {
			textInput.setStyle("-fx-text-fill: green"); 
			textInput.textProperty().addListener((observable, oldVal, newVal) -> {
				switch (newVal.charAt(newVal.length() - 1)) {
					case SINGLE_SHORTCUT:
						textInput.textProperty().addListener((observ, oldValue, text) -> handleSingle(text, textInput));
						break;
					case RAMP_SHORTCUT:
						textInput.textProperty().addListener((observ, oldValue, text) -> handleRamp(text, textInput));
						break;
					case PERCEN_SHORTCUT:
						textInput.textProperty().addListener((observ, oldValue, text) -> handlePercen(text, textInput));
						break;
				}
			});
		} 
	}
	
	// /s <n> -> 1@n 
	private void handleSingle(final String currText, final TextArea textInput) {
		if (endShortcutCheck(currText, textInput)) {
			Platform.runLater(() -> {
				textInput.textProperty().setValue(currText.replaceAll(SHORTCUT_START_REGEX + SINGLE_SHORTCUT + SINGLE_ARGS_REGEX, "1@" + currText.substring(currText.lastIndexOf(' ')  + 1, currText.length())));
			});
		}
	}
	
	private void handleRamp(final String currText, final TextArea textInput) {
		if (endShortcutCheck(currText, textInput)) {
			Pattern pattern = Pattern.compile(SHORTCUT_START_REGEX + RAMP_SHORTCUT + RAMP_ARGS_REGEX);
			Matcher matcher = pattern.matcher(currText);
			if (matcher.find()) {
				final String output = transformRampString(matcher.group());
				Platform.runLater(() -> {
					textInput.textProperty().setValue(output);
					textInput.positionCaret(textInput.textProperty().getValue().length());
				});
			}
		}
	}
	
	// <p> <r> <s> -> r@RPExs; RPE = rpe_from(p * previous_set)
	private void handlePercen(final String currText, final TextArea textInput) {
		if (endShortcutCheck(currText, textInput)) {
			Pattern prevSetPattern = Pattern.compile(SHORTCUT_START_REGEX + PERCEN_SHORTCUT + PERCEN_PREV_SET_ARGS);
			Matcher prevSetMatcher = prevSetPattern.matcher(currText);
			if (prevSetMatcher.find()) {
				String input = prevSetMatcher.group();
				final String output = transformPercenPrevSetString(input, currText);
				Platform.runLater(() -> textInput.textProperty().setValue(textInput.textProperty().getValue()
																		.substring(0, textInput.textProperty().getValue().lastIndexOf(input)) + " " + output));
			} 
		}
	}
	
	private int[] extractArgs(String args) {
		int[] extractedArgs = Stream.of(args.split("\\s+"))
				.filter(x -> {
					try {
						Integer.parseInt(x);
						return true;
					} catch (NumberFormatException e) {
						return false;
					}
				})
				.mapToInt(arg -> Integer.parseInt(arg))
				.toArray();
		return extractedArgs;
	}
	
	// // <p> <r> <s> -> r@RPExs; RPE = rpe_from(p * previous_set)
	private String transformPercenPrevSetString(String args, String currText) {
		
		int[] extractedArgs = extractArgs(args);
		float percen = extractedArgs[0] / 100.0f;
		int reps = extractedArgs[1];
		int sets = extractedArgs[2];
		
		//find previous set 
		String preShortcutText = currText.substring(0, currText.indexOf(args));
		String result = "";
		// reps@rpexsets

		Pattern repsAtRpePattern = Pattern.compile("\\d@\\d");
		Matcher matcher = repsAtRpePattern.matcher(preShortcutText);
		String lastMatch = "";
		while (matcher.find()) {
			lastMatch = matcher.group();
		}
		String[] split = lastMatch.split("@");
		String repsPrevSet = split[0];
		String rpePrevSet = split[1];
		
		// newPercen = percent1RM(repsPrevSet, rpePreSet) * percentOff 
		// rpe = get_rpe(newPercen, reps)
		
		float newPercen = RPE.getPercentage(Float.parseFloat(rpePrevSet), Integer.parseInt(repsPrevSet)) * percen;
		float newSetRpe = RPE.getRPE(reps, newPercen);
		DecimalFormat f = new DecimalFormat("##.#");
		result = reps + "@" + f.format(newSetRpe) + "x" + sets;
		return result;
	}

	
	// /<n> <a> <a> -> n@a, n@a+1+...n@b
	private String transformRampString(String args) {
		int[] extractedArgs = Stream.of(args.split("\\s+"))
										.filter(x -> {
											try {
												Integer.parseInt(x);
												return true;
											} catch (NumberFormatException e) {
												return false;
											}
										})
										.mapToInt(arg -> Integer.parseInt(arg))
										.toArray();
		
		int reps = extractedArgs[0];
		int lowestRpe = extractedArgs[1];
		int highestRpe = extractedArgs[2];
		
		StringBuilder b = new StringBuilder(reps + "@" + lowestRpe + ", ");
		while (++lowestRpe < highestRpe) {
			b.append(reps + "@" + lowestRpe + ", ");
		}
		b.append(reps + "@" + highestRpe);
		
		return b.toString();
	}
	
	@SuppressWarnings("rawtypes")
	private boolean endShortcutCheck(String currText, TextArea textInput, ChangeListener... listeners) {
		if (currText.charAt(currText.length() - 1) == END_SHORTCUT) {
			textInput.setStyle("\"-fx-text-fill: black\"");
			for (ChangeListener l : listeners) {
				textInput.textProperty().removeListener(l);
			}
			return true;
		}
		return false;
	}
	
	private boolean isCommandPrompt(char c) {
		return c == '/' || c == ':' || c == '\\';
	}
	
	public TrainingSession createTrainingSession(int dayOfTheWeek) {
		TrainingSession s = new TrainingSession(dayOfTheWeek);
		for (int i = 0; i < inputs.size(); i++) {
			TrainingMovement movement = movementSelections.get(i).getValue();
			String prescripition = inputs.get(i).getText();
			if (!validInput(prescripition)) {
				JOptionPane.showMessageDialog(null, "Invalid prescripition: " + prescripition);
			} else {
				s.addMovementPrescripition(movement, getSets(prescripition));
			}
		}
		return s;
	}
	
	private ArrayList<TrainingSet> getSets(String prescription) {
		ArrayList<TrainingSet> sets = new ArrayList<>();
		String[] tokens = prescription.split("[\\s,]"); 
		for (String t : tokens) {
			if (t.isEmpty()) {
				continue;
			}
			String[] split = t.split("@");
			String reps = split[0];
			String rpe = split[1];
			if (t.contains("x")) {
				String[] s2 = rpe.split("x");
				rpe = s2[0];
				int repeats = Integer.parseInt(s2[1]);
				for (int i = 0; i < repeats; i++) {
					sets.add(new TrainingSet(Integer.parseInt(reps), Float.parseFloat(rpe)));
				}
			} else {
				sets.add(new TrainingSet(Integer.parseInt(reps), Float.parseFloat(rpe)));
			}
		}
		return sets;
	}
	
	private boolean validInput(String s) {
		return s.replaceAll(PRESCRIPITION_VALID_REGEX, "").isEmpty();
	}
	
	@Override
	public String toString() {
		return "TrainingDayPane: " + dayLabel.getText();
	}

}
