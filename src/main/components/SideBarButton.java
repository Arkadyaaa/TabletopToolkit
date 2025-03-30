package main.components;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class SideBarButton extends StackPane {

    public SideBarButton(String text, Runnable action) {
        this.setPrefSize(250, 50);
        this.getStyleClass().add("tabButton");

        Text buttonText = new Text(text);
        buttonText.getStyleClass().add("button-text");

        this.getChildren().add(buttonText);
        StackPane.setAlignment(buttonText, Pos.CENTER);

        this.setOnMouseClicked(event -> action.run());
    }
}
