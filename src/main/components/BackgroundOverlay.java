package main.components;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class BackgroundOverlay {
    private final Rectangle overlay;
    private final StackPane parentRoot;

    public BackgroundOverlay(Stage parentStage) {
        this.parentRoot = (StackPane) parentStage.getScene().getRoot();
        this.overlay = new Rectangle(parentStage.getWidth(), parentStage.getHeight(), Color.rgb(0, 0, 0, 0.5));
        overlay.setMouseTransparent(true); // Ensures clicks go through to modal
    }

    public void show() {
        if (!parentRoot.getChildren().contains(overlay)) {
            parentRoot.getChildren().add(overlay);
        }
    }

    public void hide() {
        parentRoot.getChildren().remove(overlay);
    }
}
