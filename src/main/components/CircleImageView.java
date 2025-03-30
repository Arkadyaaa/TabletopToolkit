package main.components;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.File;

public class CircleImageView extends ImageView {

    private static final String DEFAULT_IMAGE = "defaultIcon.png";

    public CircleImageView(String imageName, double size) {
        String absoluteImagePath = getAbsolutePath(imageName + ".png");
        
        Image image;
        try {
            image = new Image(absoluteImagePath);
            if (image.isError()) {
                throw new IllegalArgumentException("Image not found");
            }
        } catch (Exception e) {
            image = new Image(getAbsolutePath(DEFAULT_IMAGE)); // Load default image
        }

        image = cropToSquare(image);

        Image processedImage = addWhiteBackground(image);

        // Resize first before setting it to ImageView
        Image resizedImage = resizeImage(processedImage, size, size);

        setImage(resizedImage);

        // Apply circular mask
        setClip(new Circle(size / 2, size / 2, size / 2));

        // Set the final size
        setFitWidth(size);
        setFitHeight(size);
        setPreserveRatio(true);
    }

    private Image addWhiteBackground(Image image) {
        double width = image.getWidth();
        double height = image.getHeight();

        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Fill with white background
        gc.setFill(Color.web("#323232"));
        gc.fillRect(0, 0, width, height);

        // Draw the original image centered
        gc.drawImage(image, 0, 0);

        WritableImage writableImage = new WritableImage((int) width, (int) height);
        canvas.snapshot(null, writableImage);

        return writableImage;
    }

    private Image resizeImage(Image image, double newWidth, double newHeight) {
        Canvas canvas = new Canvas(newWidth, newHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Center the image within the new dimensions
        double scale = Math.min(newWidth / image.getWidth(), newHeight / image.getHeight());
        double x = (newWidth - image.getWidth() * scale) / 2;
        double y = (newHeight - image.getHeight() * scale) / 2;

        gc.drawImage(image, x, y, image.getWidth() * scale, image.getHeight() * scale);

        WritableImage resizedImage = new WritableImage((int) newWidth, (int) newHeight);
        canvas.snapshot(null, resizedImage);

        return resizedImage;
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

    private String getAbsolutePath(String imageName) {
        File file = new File("src/main/tabs/avatars/" + imageName);

        if (!file.exists()) {
            System.out.println("File not found: " + file.getAbsolutePath());
        } else {
            String imagePath = file.toURI().toString();
            return imagePath;
        }
        return null;
    }

    public void updateImage(Image image) {
        if (image != null) {
            // Process the image with white background
            Image processedImage = addWhiteBackground(image);
            
            // Resize the image to fit the current dimensions
            Image resizedImage = resizeImage(processedImage, getFitWidth(), getFitHeight());
            
            // Set the image
            super.setImage(resizedImage);
        }
    }
}
