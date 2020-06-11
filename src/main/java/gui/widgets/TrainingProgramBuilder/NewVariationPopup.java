package gui.widgets.TrainingProgramBuilder;

import data.training.PrimaryMovement;
import javafx.scene.*;
import data.training.TrainingMovement;
import data.training.Variations;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.*;

@SuppressWarnings("restriction")
public class NewVariationPopup extends VBox {

	private Label variationLabel, primaryLabel;
	private TextArea varInput;
	private ComboBox<PrimaryMovement> primaryInput;
	private Button closeButton, cancelButton;
	
	public NewVariationPopup(ComboBox<TrainingMovement> userBox) {
		Stage stage = new Stage();
		
		closeButton = new Button("Confirm");
		cancelButton = new Button("Cancel");
		
		closeButton.setOnAction(click -> {
			stage.hide();
			TrainingMovement m = new TrainingMovement(varInput.getText(), primaryInput.getValue(), false);
			userBox.setValue(m);
			Variations.newVariation(m);
		});
		
		cancelButton.setOnAction(click -> {
			stage.hide();
		});
		
		variationLabel = new Label("Variation name:");
		primaryLabel = new Label("Primary:");
		
		ObservableList<PrimaryMovement> p = FXCollections.observableArrayList(PrimaryMovement.values());
		primaryInput = new ComboBox<>(p);
		varInput = new TextArea();
		varInput.setEditable(true);
		varInput.setPrefSize(150, 50);
		
		HBox h1 = new HBox(), h2 = new HBox(), h3 = new HBox();
		h1.getChildren().addAll(variationLabel, varInput);
		h2.getChildren().addAll(primaryLabel, primaryInput);
		h3.getChildren().addAll(cancelButton, closeButton);
		getChildren().addAll(h1, h2, h3);
		setPrefSize(400, 200);
		
		
		Scene scene = new Scene(this);
		stage.setScene(scene);
		stage.show();
	}

}
