package gui;

import Friday.chat_io_types.GuiIO;
import gui.widgets.TrainingProgramBuilder.NewVariationPopup;
import gui.widgets.TrainingProgramBuilder.TrainingProgramBuilder;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.event.*;

@SuppressWarnings("restriction")
public class HomeScreen extends Application {
	
	private Stage stage;
	private GuiIO fridayIO;
	
	//gross work around of JavaFX statically creating an instance of HomeScreen AND THEN HIDING THAT INSTANCE FROM US WTF
	private static HomeScreen instance;
	public HomeScreen() {
			fridayIO = new GuiIO();
			instance = this;
	}
	
	
	public void launchTrainingProgramBuilder() {
		Stage window = new Stage();
		TrainingProgramBuilder b = new TrainingProgramBuilder(fridayIO, window);
		Scene scene = new Scene(b);
		b.listenForKeyPress(scene);
		window.setScene(scene);
		window.show();
	}
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		updateTitle("Friday - Home Screen");
		
		VBox vbox = new VBox();
		vbox.getChildren().addAll(initButtonBox(), fridayIO.getConsole());
		
		Scene scene = new Scene(vbox);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public HBox initButtonBox() {
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10);
		
		Button programBuilderButton = initButton("Training Program Builder", 100, 20, press -> {
			updateTitle("Training Program Builder");
			launchTrainingProgramBuilder();
			System.out.println("ProgramBuidlerButton Pressed");
		});
	
		
		Button homeButton = initButton("Home", 100, 20, press -> {
				updateTitle("Friday - Home Screen");
		});
		
		Button weightPlannerButton = initButton("Weight Planner", 100, 20, press -> {
			updateTitle("Weight planner");
		});
		
		Button calandarButton = initButton("Calandar", 100, 20, press -> {
			updateTitle("Calandar");
		});
		
		hbox.getChildren().addAll(homeButton, calandarButton, programBuilderButton, weightPlannerButton);
		return hbox;
	}
	
	private Button initButton(String label, int length, int height, EventHandler<ActionEvent> onClick) {
		Button b = new Button(label);
		b.setPrefSize(length, height);
		b.setOnAction(onClick);
		return b;
	}
	
	public void updateTitle(String title) {
		stage.setTitle(title);
	}
	
	public GuiIO getGuiIO() {
		if (fridayIO == null) {
			System.out.println("Warning: returning null GuiIO");
		}
		return fridayIO;
	}
	public static HomeScreen getHomeScreen() {
		return instance;
	}
	
	public static void makeGo() {
		launch();
	}
	
	@Override
	public void stop() {
		System.exit(0);
	}
	
//	public static void main(String[] args) {
//		launch(args);
//	}

}
