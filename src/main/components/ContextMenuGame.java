package main.components;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import main.player.PlayerStorage;

public class ContextMenuGame extends ContextMenu {

    private final PlayerStorage playerStorage;

    public ContextMenuGame(PlayerStorage playerStorage, Runnable onReload) {
        this.playerStorage = playerStorage;

        // Create menu items
        MenuItem reloadPlayers = new MenuItem("Reload Players");

        // Set actions for menu items
        // editPlayerItem.setOnAction(e -> editPlayer());
        
        reloadPlayers.setOnAction(e -> onReloadMethod(onReload));

        // Add menu items to context menu
        this.getItems().addAll(reloadPlayers);
    }

    private void onReloadMethod(Runnable onReload){
        if (onReload != null) {
            onReload.run();
        }
    }
}