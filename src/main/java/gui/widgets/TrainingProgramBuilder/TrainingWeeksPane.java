package gui.widgets.TrainingProgramBuilder;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import data.training.TrainingWeek;
import data.training.TrainingWeek.Stress;
import data.training.TrainingWeek.Type;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ChoiceBox;
import javafx.collections.*;

@SuppressWarnings("restriction")
public class TrainingWeeksPane extends ScrollPane {

	public static final int DEFAULT_SESSIONS_PER_WEEK		= 4,
			DEFAULT_NUM_WEEKS				= 9,
			DEFAULT_NUM_MOVEMENTS_PER_DAY	= 4;

	public static final ObservableList<Stress>	STRESS_VALUES	= FXCollections.observableArrayList(TrainingWeek.Stress.values()); 
	public static final ObservableList<Type>	TYPE_VALUES		= FXCollections.observableArrayList(TrainingWeek.Type.values());


	private int sessionsPerWeek;
	private int numWeeks;

	private ArrayList<ChoiceBox<Stress>> stressInputs;
	private ArrayList<ChoiceBox<Type>> typeInputs;
	private ArrayList<BorderPane> weekBoxes;

	private LinkedHashMap<Integer, ArrayList<TrainingDayPane>> weekToDaysMap;

	public TrainingWeeksPane() {
		setDimensions(DEFAULT_SESSIONS_PER_WEEK, DEFAULT_NUM_WEEKS);
	}

	public void setDimensions(int sessionsPerWeek, int numWeeks) {
		this.sessionsPerWeek = sessionsPerWeek;
		this.numWeeks = numWeeks;

		stressInputs = new ArrayList<>();
		typeInputs = new ArrayList<>();
		weekBoxes = new ArrayList<>();
		weekToDaysMap = new LinkedHashMap<>();

		for (int i = 0; i < numWeeks; i++) {
			weekBoxes.add(new BorderPane());
			Label weekLabel = new Label("Week " + (i + 1));
			ChoiceBox<Stress> stressInput = new ChoiceBox<>(STRESS_VALUES);
			stressInput.setValue(Stress.LOW); //TODO: smart autofill
			stressInputs.add(stressInput);
			ChoiceBox<Type> typeInput = new ChoiceBox<>(TYPE_VALUES);
			typeInput.setValue(Type.DEVELOPEMENTAL); //TODO: smart autofill
			typeInputs.add(typeInput);
			HBox top = new HBox();
			Label typeLabel = new Label("Type:");
			Label stressLabel = new Label("Stress:");
			top.getChildren().addAll(weekLabel, stressLabel, stressInput, typeLabel, typeInput);
			weekBoxes.get(i).setTop(top);
			HBox days = new HBox();
			for (int dayOfWeek = 1; dayOfWeek <= sessionsPerWeek; dayOfWeek++) {
				TrainingDayPane dayPane = new TrainingDayPane(dayOfWeek, DEFAULT_NUM_MOVEMENTS_PER_DAY);
//				Label t = new Label("" + j);
				days.getChildren().add(dayPane);
//				days.getChildren().add(t);
				addDayPaneToMap(weekToDaysMap, dayPane, i);
			}
			weekBoxes.get(i).setCenter(days);
		}

		VBox v = new VBox();
		v.getChildren().addAll(weekBoxes);
		setContent(v);
	}

	private void addDayPaneToMap(HashMap<Integer, ArrayList<TrainingDayPane>> map, TrainingDayPane pane, int week) {
		ArrayList<TrainingDayPane> inMap = map.get(week);
		if (inMap == null) {
			inMap = new ArrayList<>();
			inMap.add(pane);
			map.put(week, inMap);
		} else {
			map.get(week).add(pane);
		}
	}

	public List<TrainingWeek> createTrainingWeeks() {
		
		List<TrainingWeek> weeks = new ArrayList<>();

		for (int i = 0; i < numWeeks; i++) {
			TrainingWeek week = new TrainingWeek(stressInputs.get(i).getValue(), typeInputs.get(i).getValue(), i + 1);
			ArrayList<TrainingDayPane> days = weekToDaysMap.get(i);
			if (days != null) {
				int j = 1;
				for (TrainingDayPane dayPane : days) {
					week.addSessions(dayPane.createTrainingSession(j++));
				}
			}
			weeks.add(week);
		}

		return weeks;
	}

	public void addWeeks(int numAdditionalWeeks) {
		for (int i = 0; i < numAdditionalWeeks; i++) {
			addWeek();
		}
	}

	public void addWeek() {

	}

}
