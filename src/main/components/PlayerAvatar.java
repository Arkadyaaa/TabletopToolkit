package main.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import main.player.Player;

public class PlayerAvatar extends VBox {

    private double mouseX, mouseY;
    private StackPane imageContainer;

    public PlayerAvatar(Player player, double imageSize, boolean isDraggable, boolean isLabelVisible) {
        super(5); // Spacing between elements

        // Create label with the player's name
        Label playerLabel = new Label(player.getName());
        playerLabel.getStyleClass().add("player-label");

        // Create circular image
        CircleImageView circularImage = new CircleImageView(player.getName(), imageSize);

        // Create image container
        imageContainer = new StackPane(circularImage);
        if (player.isRich()) {
            imageContainer.getStyleClass().add("player-rich");
        }

        // Set alignment and add elements
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(imageContainer);
        if(isLabelVisible) this.getChildren().add(playerLabel);

        if(isDraggable) enableDrag();
    }

    private void enableDrag() {
        imageContainer.setOnMousePressed(event -> {
            this.toFront(); // Ensure this avatar is on top
    
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
    
            event.consume(); // Stop event from bubbling up
        });
    
        imageContainer.setOnMouseDragged(event -> {
            double offsetX = event.getSceneX() - mouseX;
            double offsetY = event.getSceneY() - mouseY;
    
            this.setTranslateX(this.getTranslateX() + offsetX);
            this.setTranslateY(this.getTranslateY() + offsetY);
    
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
    
            event.consume(); // Prevent other elements from capturing the event
        });
    }

    public StackPane getImageContainer(){
        return imageContainer;
    }

    public void setRichClass(Boolean isRich){
        if(isRich){
            imageContainer.getStyleClass().add("player-rich");
        } else{
            imageContainer.getStyleClass().remove("player-rich");
        }
    }
}
