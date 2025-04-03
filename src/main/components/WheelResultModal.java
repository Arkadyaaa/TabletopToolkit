package main.components;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class WheelResultModal {
    private String resultText;
    private Stage stage;
    private Label titleLabel;
    private Label resultLabel;
    private Button closeButton;

    public WheelResultModal(String resultText) {
        this.resultText = resultText;
    }

    public void show(){
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        SplitPane root = new SplitPane();
        root.setOrientation(javafx.geometry.Orientation.VERTICAL);
        root.setPrefSize(493, 130);
        root.getStyleClass().addAll("resultSplitPane", "resultRoundedCorner");

        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10));
        topBar.getStyleClass().addAll("resultTopBar", "resultRoundedCorner");

        titleLabel = new Label("Spin Result");
        titleLabel.setPrefSize(115, 27);
        titleLabel.getStyleClass().add("resultTitleLabel");

        HBox spacer = new HBox();
        spacer.setPrefSize(272, 27);
        spacer.getStyleClass().add("resultRoundedCorner");
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        closeButton = new Button("X");
        closeButton.getStyleClass().add("resultCloseButton");
        closeButton.setOnAction(event -> handleClose());

        topBar.getChildren().addAll(titleLabel, spacer, closeButton);

        VBox bottomSection = new VBox();
        bottomSection.setAlignment(Pos.CENTER);
        bottomSection.setPadding(new Insets(20));
        bottomSection.getStyleClass().addAll("resultBottomBar", "resultRoundedCorner");

        resultLabel = new Label(resultText + "");
        resultLabel.setStyle("-fx-font-size: 24px;");

        bottomSection.getChildren().add(resultLabel);

        root.getItems().addAll(topBar, bottomSection);
        root.setDividerPositions(0.5);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../resources/styles.css").toExternalForm());

        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.showAndWait();
    }

    private void handleClose() {
        if (stage != null) {
            stage.close();
        }
    }
}
