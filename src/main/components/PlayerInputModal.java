package main.components;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import java.awt.image.BufferedImage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.player.Player;
import main.player.PlayerStorage;

public class PlayerInputModal {

    private static final String DEFAULT_IMAGE_PATH = PlayerInputModal.class.getResource("../tabs/avatars/defaultIcon.png").toExternalForm();

    private static String playerName;
    private static Image selectedImage;
    private static File selectedFile;
    private static TextField nameInput;
    private static CheckBox richCheckBox;
    private static ImageView imageView;
    private static PlayerStorage playerStorage;
    private static Stage modalStage;

    public static String display(PlayerStorage storage) {
        playerStorage = storage;

        // Reset the selected image and file
        selectedImage = null;
        selectedFile = null;

        modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Add Player");

        // Input field for player name
        Label nameLabel = new Label("Enter Player Name:");
        nameInput = new TextField();
        nameInput.setPromptText("Player Name");
        nameInput.setAlignment(Pos.CENTER);

        // Checkbox for "Rich"
        richCheckBox = new CheckBox("Rich");

        // Image preview
        imageView = new ImageView(new Image(DEFAULT_IMAGE_PATH));
        imageView.setFitWidth(80);
        imageView.setFitHeight(80);

        // Choose Image Button
        Button chooseImageButton = new Button("Choose Image");
        chooseImageButton.setOnAction(e -> handleChooseImage());

        // Submit button
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> playerName = handleSubmit());

        // Cancel button
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> modalStage.close());

        // Image layout
        VBox imageBox = new VBox(10);
        imageBox.setAlignment(Pos.CENTER);
        imageBox.getChildren().addAll(imageView, chooseImageButton);

        // Button layout
        HBox buttonLayout = new HBox(10);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().addAll(submitButton, cancelButton);

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(nameLabel, nameInput, richCheckBox, imageBox, buttonLayout);
        layout.setAlignment(Pos.CENTER);

        // Scene
        Scene scene = new Scene(layout, 350, 300);

        // CSS
        richCheckBox.getStyleClass().add("rich-checkbox");
        submitButton.getStyleClass().add("player-button");
        cancelButton.getStyleClass().add("player-button");
        chooseImageButton.getStyleClass().add("player-button");
        layout.getStyleClass().add("playerRoundedCorner");
        scene.getStylesheets().add(PlayerInputModal.class.getResource("../resources/styles.css").toExternalForm());

        scene.setFill(Color.TRANSPARENT);
        modalStage.initStyle(StageStyle.TRANSPARENT);
        modalStage.setScene(scene);
        modalStage.showAndWait();
        return playerName;
    }

    private static void handleChooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        selectedFile = fileChooser.showOpenDialog(modalStage);
    
        if (selectedFile != null) {
            selectedImage = new Image(selectedFile.toURI().toString());
            imageView.setImage(cropToSquare(selectedImage));
        }
    }
    
    public static Image cropToSquare(Image inputImage) {
        int originalWidth = (int) inputImage.getWidth();
        int originalHeight = (int) inputImage.getHeight();
        
        // Determine the square size
        int squareSize = Math.min(originalWidth, originalHeight);
        
        // Calculate cropping coordinates (center crop)
        int xOffset = (originalWidth - squareSize) / 2;
        int yOffset = (originalHeight - squareSize) / 2;

        // Create a cropped image
        PixelReader pixelReader = inputImage.getPixelReader();
        WritableImage croppedImage = new WritableImage(pixelReader, xOffset, yOffset, squareSize, squareSize);

        return croppedImage;
    }
    
    private static String handleSubmit() {
        String playerName = nameInput.getText().trim();
        boolean isRich = richCheckBox.isSelected();
    
        if (playerName.isEmpty()) {
            showAlert("Error", "Player name cannot be empty!");
            return "defaultIcon";
        }
    
        // Create player
        Player newPlayer = new Player(playerName);
        if (isRich) {
            newPlayer.becomeRich();
            newPlayer.setGold(30);
        } else {
            newPlayer.setGold(12);
        }
    
        if (selectedImage != null && selectedFile != null) {
            try {
                // Define the target directory inside the project
                File targetDir = new File("src/main/tabs/avatars/");
                if (!targetDir.exists()) {
                    targetDir.mkdirs(); // Create directories if they don't exist
                }

                // Always save as PNG
                File targetFile = new File(targetDir, playerName + ".png");

                // Convert JavaFX Image to BufferedImage
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(selectedImage, null);

                // Save as PNG
                ImageIO.write(bufferedImage, "png", targetFile);

                System.out.println("Image saved as PNG to: " + targetFile.getAbsolutePath());
            } catch (IOException e) {
                showAlert("Error", "Failed to save image: " + e.getMessage());
            }
        }
    
        // Store player in PlayerStorage
        playerStorage.addPlayer(newPlayer);
    
        modalStage.close(); // Close modal after submitting
        return playerName;
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
