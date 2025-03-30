package main.tabs;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import main.components.ContextMenuGame;
import main.components.ContextMenuPlayer;
import main.components.PlayerAvatar;
import main.player.Player;
import main.player.PlayerStorage;
import main.player.PlayerStorageConsumer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GameController implements PlayerStorageConsumer {

    // Row, Col sets for different space types
    private final Set<String> shopSpace = Set.of("0,4", "1,0", "3,0", "4,4");
    private final Set<String> gambleSpace = Set.of("3,1", "3,5");
    private final Set<String> goodSpace = Set.of("0,5", "1,2", "4,2");
    private final Set<String> badSpace = Set.of("1,1", "2,2", "2,4");

    @FXML private GridPane gridPane;
    @FXML private StackPane rootPane;
    @FXML private StackPane playerSpace;
    @FXML private StackPane gridHolder;
    @FXML private Pane backPane;

    @FXML private StackPane pane_0_0, pane_0_1, pane_0_2, pane_0_3, pane_0_4, pane_0_5;
    @FXML private StackPane pane_1_0, pane_1_1, pane_1_2, pane_1_3, pane_1_4, pane_1_5;
    @FXML private StackPane pane_2_0, pane_2_1, pane_2_2, pane_2_3, pane_2_4, pane_2_5;
    @FXML private StackPane pane_3_0, pane_3_1, pane_3_2, pane_3_3, pane_3_4, pane_3_5;
    @FXML private StackPane pane_4_0, pane_4_1, pane_4_2, pane_4_3, pane_4_4, pane_4_5;

    private double imageSize = 50;
    private PlayerStorage playerStorage;

    @Override
    public void setPlayerStorage(PlayerStorage playerStorage) {
        this.playerStorage = playerStorage;

        if (playerStorage != null) {
            for (Player player : playerStorage.getAllPlayers()) {
                placePlayer(player);
            }
        }
    }

    @FXML
    private void initialize() {
        System.out.println("GameController initialized.");

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 6; col++) {
                String key = row + "," + col;
                StackPane pane = getPaneById(row, col);

                if (pane != null) {
                    applyStyles(pane, key);
                }
            }
        }
        
        // Context menu
        ContextMenuGame contextMenu = new ContextMenuGame(playerStorage, this::reloadPlayers);

        setupBackgroundContextMenu(backPane, contextMenu);
        setupBackgroundContextMenu(gridHolder, contextMenu);
    }

    private void applyStyles(StackPane pane, String key) {
        // Define a map with space types and their corresponding styles
        Map<Set<String>, String[]> stylesMap = new HashMap<>();
        stylesMap.put(shopSpace, new String[]{"#5b8bba", "Shop"});
        stylesMap.put(gambleSpace, new String[]{"#5bbab4", "Gamble"});
        stylesMap.put(goodSpace, new String[]{"#5bba7f", "Good"});
        stylesMap.put(badSpace, new String[]{"#ba5b5b", "Bad"});

        for (Map.Entry<Set<String>, String[]> entry : stylesMap.entrySet()) {
            if (entry.getKey().contains(key)) {
                pane.setStyle("-fx-background-color: " + entry.getValue()[0] + "; -fx-border-color: black;");
                addLabel(pane, entry.getValue()[1]);
                return;
            }
        }
    }

    private void addLabel(StackPane pane, String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: white;");

        pane.getChildren().add(label);
    }

    private StackPane getPaneById(int row, int col) {
        try {
            return (StackPane) getClass().getDeclaredField("pane_" + row + "_" + col).get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println("Invalid Pane: " + row + "," + col);
            return null;
        }
    }

    public void placePlayer(Player player) {
        if (playerSpace != null) {
            PlayerAvatar playerAvatar = new PlayerAvatar(player, imageSize, true, false);
            playerSpace.getChildren().add(playerAvatar);
        } else {
            System.out.println("Error: playerSpace is null!");
        }
    }
    
    private void setupBackgroundContextMenu(Node node, ContextMenu contextMenu) {
    node.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(node, event.getScreenX(), event.getScreenY());
            } else {
                contextMenu.hide();
            }
            });
    }

    private void reloadPlayers(){
        System.out.println("Reloading players...");
    
        // Remove all PlayerAvatar instances
        playerSpace.getChildren().removeIf(node -> node instanceof PlayerAvatar);
    
        // Re-add updated players from storage
        for (Player player : playerStorage.getAllPlayers()) {
            placePlayer(player);
        }
    }
}
