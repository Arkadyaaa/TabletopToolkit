package main.components;

import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class WheelSimpleRightPane {

    private ObservableList<String> items = FXCollections.observableArrayList();
    private Runnable drawWheel;

    private TextArea textArea = new TextArea();
    
    public WheelSimpleRightPane(Runnable drawWheel, ObservableList<String> items) {
        this.items = items;
        this.drawWheel = drawWheel;
    }

    public AnchorPane createRightPane() {
        // Create AnchorPane
        AnchorPane rightPane = new AnchorPane();

        // Set preferred sizes for the AnchorPane
        rightPane.setPrefWidth(316);
        rightPane.setPrefHeight(603);

        // Create HBox for the top section with buttons and labels
        HBox topHBox = new HBox();
        topHBox.setSpacing(10);
        topHBox.getStyleClass().add("rightPaneTop");

        // Create Label
        Label label = new Label("List");
        label.setPrefWidth(53);
        label.setPrefHeight(22);
        label.getStyleClass().add("rightPaneListLabel");

        // Create Button for Shuffle
        Button shuffleButton = new Button("Shuffle");
        shuffleButton.setPrefWidth(76);
        shuffleButton.setPrefHeight(26);
        shuffleButton.getStyleClass().add("rightPaneButton");
        shuffleButton.setOnAction(event -> shuffleButtonClicked());

        // Create Button for Sort
        Button sortButton = new Button("Sort");
        sortButton.setPrefWidth(69);
        sortButton.setPrefHeight(26);
        sortButton.getStyleClass().add("rightPaneButton");
        sortButton.setOnAction(event -> sortButtonClicked());

        // Create CheckBox for Advanced
        CheckBox advancedCheckbox = new CheckBox("Advanced");
        advancedCheckbox.setPrefWidth(95);
        advancedCheckbox.setPrefHeight(16);
        advancedCheckbox.getStyleClass().add("rightPaneCheckbox");
        advancedCheckbox.setOnAction(event -> advancedToggle());

        // Create a Pane to align buttons properly (Empty Pane for spacing, as in FXML)
        Pane spacerPane = new Pane();
        spacerPane.setPrefWidth(28);
        spacerPane.setPrefHeight(42);

        // Add children to the HBox (Label, Spacer, Buttons, Checkbox)
        topHBox.getChildren().addAll(label, spacerPane, shuffleButton, sortButton, advancedCheckbox);

        // Create TextArea
        textArea.setLayoutX(8);
        textArea.setLayoutY(52);
        textArea.setPrefWidth(301);
        textArea.setPrefHeight(537);
        textArea.getStyleClass().add("sectorsArea");

        // Add the HBox and TextArea to the AnchorPane
        rightPane.getChildren().addAll(topHBox, textArea);

        // Optionally set constraints if you need more precise positioning
        AnchorPane.setTopAnchor(topHBox, 0.0);
        AnchorPane.setLeftAnchor(topHBox, 0.0);
        AnchorPane.setRightAnchor(topHBox, 0.0);
        AnchorPane.setTopAnchor(textArea, 52.0);
        AnchorPane.setLeftAnchor(textArea, 0.0);
        AnchorPane.setRightAnchor(textArea, 0.0);

        // Listen for text changes in TextArea
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            String[] lines = newValue.split("\n"); // Split text by new lines
            items.setAll(lines);
            drawWheel.run();
        });

        return rightPane;
    }

    private void shuffleButtonClicked() {
        Collections.shuffle(items);
        updateWheelAndTextbox();
    }

    private void sortButtonClicked() {
        FXCollections.sort(items, String::compareToIgnoreCase);
        updateWheelAndTextbox();
    }

    private void advancedToggle() {
    }

    private void updateWheelAndTextbox(){
        // Join the sorted items into a single string with new lines between them
        String sortedText = String.join("\n", items);
        textArea.setText(sortedText);

        drawWheel.run();
    }
}
