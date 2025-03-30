package main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import main.components.SideBarButton;
import main.player.PlayerStorage;
import main.player.PlayerStorageConsumer;
import javafx.scene.layout.HBox;

public class BaseController implements PlayerStorageConsumer {
    @FXML private Pane mainView;
    @FXML private HBox topBar;
    @FXML private HBox bottomBar;

    @FXML private Label leftStatus;
    @FXML private Label rightStatus;

    // Store loaded views to prevent reloading
    private final Map<String, Pane> loadedViews = new HashMap<>();

    private PlayerStorage playerStorage = PlayerStorage.getInstance();

    @Override
    public void setPlayerStorage(PlayerStorage playerStorage) {
        this.playerStorage = playerStorage;

        playerStorage.loadPlayers();
    }

    @FXML
    private void initialize() {
        // Set initial status
        leftStatus.setText("Welcome to the application");
        rightStatus.setText("Status: Ready");

        // Create SideBarButtons
        SideBarButton[] buttons = {
            new SideBarButton("Game", () -> loadView("Game")),
            new SideBarButton("Inventory", () -> loadView("Inventory")),
            new SideBarButton("Shop", () -> loadView("Shop")),
            new SideBarButton("Wheel", () -> loadView("Wheel")),
            new SideBarButton("Players", () -> loadView("Players"))
        };

        for (SideBarButton button : buttons) {
            HBox.setMargin(button, new Insets(6, 4, 6, 4));
            topBar.getChildren().add(button);
        }

        // Load the default view on startup (Game View on default, currently set on what I'm working right now for easier development)
        loadView("Wheel");
    }

    private void loadView(String viewName) {
        try {
            if (!loadedViews.containsKey(viewName)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("tabs/" + viewName + ".fxml"));
                Pane view = loader.load();
    
                Object controller = loader.getController();
                if (controller instanceof PlayerStorageConsumer) {
                    ((PlayerStorageConsumer) controller).setPlayerStorage(playerStorage);
                }
    
                // Store the loaded view in the cache
                loadedViews.put(viewName, view);
            }
    
            // Set the cached view
            mainView.getChildren().setAll(loadedViews.get(viewName));
            leftStatus.setText(viewName + " View Loaded");
        } catch (IOException e) {
            System.err.println("Error loading view: " + viewName);
            e.printStackTrace();
        }
    }
    
}
