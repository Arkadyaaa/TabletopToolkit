package main.components;

import java.util.Optional;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.DialogPane;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import main.player.Player;
import main.player.PlayerStorage;

public class ContextMenuPlayer extends ContextMenu {

    private final Player player;
    private final PlayerStorage playerStorage;
    private final MenuItem setPlayerRich;
    private PlayerAvatar playerAvatar;

    public ContextMenuPlayer(Player player, PlayerAvatar playerAvatar, PlayerStorage playerStorage) {
        this.player = player;
        this.playerStorage = playerStorage;
        this.playerAvatar = playerAvatar;

        // Create menu items
        MenuItem editPlayerItem = new MenuItem("Edit Player");
        MenuItem removePlayerItem = new MenuItem("Remove Player");
        MenuItem setGold = new MenuItem("Set Gold");
        setPlayerRich = new MenuItem(player.isRich() ? "Set as Broke" : "Set as Rich"); // Set initial text

        // Set actions for menu items
        editPlayerItem.setOnAction(e -> editPlayer());
        removePlayerItem.setOnAction(e -> removePlayer());
        setGold.setOnAction(e -> setGold());
        setPlayerRich.setOnAction(e -> togglePlayerRich());

        // Add menu items to context menu
        this.getItems().addAll(editPlayerItem, removePlayerItem, setGold, setPlayerRich);
    }

    private void removePlayer() {
        Boolean playerRemoved = playerStorage.removePlayer(player.getName());

        if(playerRemoved && playerAvatar.getParent() instanceof HBox parentBox) 
            parentBox.getChildren().remove(playerAvatar);
    }

    private void editPlayer() {
        System.out.println("Editing player: " + player.getName());
    }

    private void togglePlayerRich() {
        if (player.isRich()) {
            player.becomeBroke();
            setPlayerRich.setText("Set as Rich");
            System.out.println(player.getName() + " is now Broke");
            playerAvatar.setRichClass(false);
        } else {
            player.becomeRich();
            setPlayerRich.setText("Set as Broke");
            System.out.println(player.getName() + " is now Rich");
            playerAvatar.setRichClass(true);
        }
    }
    
    private void setGold() {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(player.getGold()));
        dialog.setTitle("Set Gold");
        dialog.setHeaderText("Enter new gold amount for " + player.getName());
        dialog.setContentText("Gold:");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(ContextMenuPlayer.class.getResource("../resources/styles.css").toExternalForm());

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(input -> {
            try {
                int newGold = Integer.parseInt(input);
                player.setGold(newGold);
                System.out.println(player.getName() + " now has " + newGold + " gold.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid gold amount entered.");
            }
        });
}

}
