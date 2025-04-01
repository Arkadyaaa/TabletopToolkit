package main.tabs;

import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import main.components.WheelResultModal;

public class WheelController {
    @FXML private Canvas wheelCanvas;
    @FXML private AnchorPane leftPane, middlePane, rightPane;
    @FXML private Button shuffleButton, sortButton;
    @FXML private CheckBox advancedCheckbox;
    @FXML private TextArea textBox;

    private static final int WHEEL_SIZE = 400;
    private static final int SECTORS = 50;
    private Rotate rotate;
    private Timeline timeline;
    private double angle = 0;
    private double initialSpeed;
    private double speed;
    private boolean spinning = false;
    private boolean holdingWheel = false;
    private boolean canHold = true;
    private int holdCounter = 0;
    private double holdDuration;
    private double holdDurationMin = 0.5;
    private double holdDurationMax = 1;
    private Random random = new Random();

    @FXML
    public void initialize() {
        holdDuration = randomDuration(holdDurationMin, holdDurationMax);
        drawWheel();
        rotate = new Rotate(0, WHEEL_SIZE / 2, WHEEL_SIZE / 2);
        wheelCanvas.getTransforms().add(rotate);
        wheelCanvas.setOnMouseClicked(event -> startSpin());
    }

    private double randomDuration(double rangeMin, double rangeMax) {
        return rangeMin + (rangeMax - rangeMin) * random.nextDouble();
    }

    private void drawWheel() {
        GraphicsContext gc = wheelCanvas.getGraphicsContext2D();
        double angleStep = 360.0 / SECTORS;
        
        // Wheel Theme (Add more themes in the future)
        String[] colors = {"#001f38", "#526079", "#9a7e8e", "#8f4a50", "#a06150", "#a1836b"};
        
        for (int i = 0; i < SECTORS; i++) {
            gc.setFill(Color.web(colors[i % colors.length]));
            gc.fillArc(0, 0, WHEEL_SIZE, WHEEL_SIZE, i * angleStep, angleStep, ArcType.ROUND);
        }
        
        // Add labels to each sector
        gc.setFill(Color.WHITE);
        gc.setFont(new Font(14));
        gc.setTextAlign(TextAlignment.CENTER);
        middlePane.getStyleClass().add("dropShadow");

        for (int i = 0; i < SECTORS; i++) {
            double midAngle = (i * angleStep) + (angleStep / 2);
            double radians = Math.toRadians(midAngle);
            double textX = (WHEEL_SIZE / 2) + (WHEEL_SIZE / 3 * Math.cos(radians));
            double textY = (WHEEL_SIZE / 2) + (WHEEL_SIZE / 3 * Math.sin(radians));
            gc.fillText("" + (i + 1), textX, textY);
        }
    }

    private void startSpin() {
        if (spinning) return; // Prevent multiple spins

        spinning = true;

        //Reset variables before spinning  again
        initialSpeed = randomDuration(15, 25); // Randomize initial speed
        speed = initialSpeed;
        holdCounter = 0;
        
        timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> slowSpin()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void slowSpin() {
        angle += speed;
        if (angle >= 360) angle -= 360; // Keep within 0-360 range

        rotate.setAngle(angle);

        if (holdingWheel && canHold) {
            holdCounter++;
            if (holdCounter >= holdDuration * 100) {
                holdingWheel = false;
                canHold = false;
            }
            return; // Keep speed constant while holding
        }

        if (speed <= 10 && !holdingWheel && canHold) {
            holdingWheel = true;
            holdCounter = 0;
        } else {
            // Gradual deceleration
            speed *= 0.99;
            if (speed < 0.005) {
                timeline.stop();
                spinning = false;
                canHold = true;
                
                PauseTransition delay = new PauseTransition(Duration.seconds(0.25));
                delay.setOnFinished(e -> showResultPopup());
                delay.play();
            }
        }
    }
    
    private void showResultPopup() {
        double angleStep = 360.0 / SECTORS;
        double adjustedAngle = (angle) % 360;  
        int calculatedIndex = SECTORS - (int) Math.floor(adjustedAngle / angleStep);  
        final int sectorIndex = (calculatedIndex + SECTORS) % SECTORS;

        Platform.runLater(() -> {
            new WheelResultModal(sectorIndex).show();
        });
    }

    @FXML
    private void shuffleButton(){

    }

    @FXML
    private void sortButton(){

    }

    @FXML
    private void advancedToggle(){
        
    }

}
