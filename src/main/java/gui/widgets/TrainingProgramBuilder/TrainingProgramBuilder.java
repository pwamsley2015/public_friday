package gui.widgets.TrainingProgramBuilder;

import javafx.scene.layout.BorderPane;

import java.io.File;

import data.training.TrainingBlock;
import data.training.TrainingProgramFileUtil;
import io.friday.FridayIO;
import io.friday.FridayOutput;
import javafx.stage.Stage;
import javafx.scene.Scene;
@SuppressWarnings("restriction")
public class TrainingProgramBuilder extends BorderPane {
	
	private Stage window;
	private FridayIO fridayIO;
	
	private TrainingBlock block;
	private File file;
	
	private TrainingBlockPane blockPane;
	private TrainingWeeksPane weeksPane;
	
	public TrainingProgramBuilder(FridayIO fio, Stage window) {
		this.window = window;
		this.fridayIO = fio;
		updateTitle();
		setPrefSize(900, 900);
		blockPane = new TrainingBlockPane();
		weeksPane = new TrainingWeeksPane();
		
//		getChildren().addAll(blockPane, weeksPane);
		setTop(blockPane);
		setCenter(weeksPane);
	}
	
	public void listenForKeyPress(Scene scene) {
		scene.setOnKeyPressed(key -> handleKeyPressed(key.getText(), key.isShortcutDown()));
	}
	
	private void handleKeyPressed(String key, boolean shortcutDown) {
		if (shortcutDown && key.equalsIgnoreCase("s")) {
			if (TrainingProgramFileUtil.saveNewBlock(createBlock(), blockPane.getBlockName())) {
				fridayIO.sendOutput(new FridayOutput<>("I saved your new training program. This one looks fun!"));
			} else {
				fridayIO.sendOutput(new FridayOutput<>("Something went wrong with saving your new training program."));
			}
		}
	}

	public TrainingBlock createBlock() {
		TrainingBlock block = new TrainingBlock(blockPane.getBlockObjective());
		weeksPane.createTrainingWeeks().stream().forEach(week -> block.addWeeks(week));
		return block;
	}
	
	private void updateTitle() {
		String title = "";
		if (block == null) {
			title += "No open block";
		} else {
			title += block.objective.toString();
		}
		if (file == null) {
			title += " (No file selected)";
		} else {
			title += " @" + file.getAbsolutePath();
		}
		
		window.setTitle(title);
	}
}
