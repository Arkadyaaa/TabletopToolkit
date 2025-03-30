package main.tabs;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import main.components.ContextMenuPlayer;
import main.components.PlayerAvatar;
import main.player.Player;
import main.player.PlayerStorage;
import main.player.PlayerStorageConsumer;
import main.components.PlayerInputModal;

public class PlayersController implements PlayerStorageConsumer {

    @FXML
    private Label windowLabel;

    @FXML
    private HBox playersBox;

    @FXML
    private StackPane addPlayerButton;

    private double imageSize = 96;
    private PlayerStorage playerStorage;

    @Override
    public void setPlayerStorage(PlayerStorage playerStorage) {
        this.playerStorage = playerStorage;
        
        if (playerStorage != null) {
            for (Player player : playerStorage.getAllPlayers()) {
                addPlayerToDisplay(player);
            }
        }
    }

    @FXML
    private void initialize() {
        playersBox.setAlignment(javafx.geometry.Pos.CENTER);
        playersBox.setSpacing(20);
    }

    @FXML
    private void addPlayerButton() {
        String lastPlayerAdded = PlayerInputModal.display(playerStorage);

        Player newPlayer = playerStorage.getPlayer(lastPlayerAdded);
        if (newPlayer != null) {
            addPlayerToDisplay(newPlayer);
        }
    }

    private void addPlayerToDisplay(Player player) {
        PlayerAvatar playerAvatar = new PlayerAvatar(player, imageSize, false, true);

        // Context menu
        ContextMenuPlayer contextMenu = new ContextMenuPlayer(player, playerAvatar, playerStorage);

        playerAvatar.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(playerAvatar, event.getScreenX(), event.getScreenY());
            } else {
                contextMenu.hide();
            }
        });

        // Add the new player to the display
        playersBox.getChildren().add(playerAvatar);
    }
}
