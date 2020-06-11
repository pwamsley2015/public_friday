package gui.widgets.TrainingProgramBuilder;

import java.util.Date;

import data.training.TrainingBlock.BlockObjective;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

@SuppressWarnings("restriction")
public class TrainingBlockPane extends HBox {
	
	private Label titleLabel, typeLabel, lengthLabel, daysLabel;
	
	private TextArea titleInput; 
	private ChoiceBox<Integer> daysInput, lengthInput;
	private ChoiceBox<BlockObjective> typeInput;
	
	public TrainingBlockPane() {
		
		setPrefHeight(50);
		
		//---labels---/
		titleLabel = new Label("Program Name:");
		typeLabel = new Label("Block type:");
		lengthLabel = new Label("Block length:");
		daysLabel = new Label("Sessions per week:");
		
		//---inputs---//
		titleInput = new TextArea();
		titleInput.setEditable(true);
		
		daysInput = new ChoiceBox<>(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7));
		daysInput.setValue(4); //todo: make prop
		
		ObservableList<BlockObjective> types = FXCollections.observableArrayList(BlockObjective.values());
		typeInput = new ChoiceBox<>(types);
		
		ObservableList<Integer> numWeeks = FXCollections.observableArrayList();
		for (int i = 3; i < 16; i++) {
			numWeeks.add(i);
		}
		lengthInput = new ChoiceBox<>(numWeeks);
		lengthInput.setValue(9);
		
		//---group labels and inputs---//
		VBox titleBox	= new VBox(titleLabel, titleInput), 
			 typeBox	= new VBox(typeLabel, typeInput),
			 daysBox	= new VBox(daysLabel, daysInput),
			 lenBox		= new VBox(lengthLabel, lengthInput);
		
		getChildren().addAll(titleBox, typeBox, daysBox, lenBox);
	}
	
	public BlockObjective getBlockObjective() {
		return typeInput.getValue();
	}
	
	public String getBlockName() {
		Date d = new Date();
		return titleInput.getText() + "[" + d.getMonth() + "|" + d.getYear() + "]";
	}

}
