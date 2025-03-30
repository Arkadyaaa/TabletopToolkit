package main.player;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlayerStorage {
    private static PlayerStorage instance;
    private Map<String, Player> players;
    private static final String FILE_PATH = "src/main/player/players.txt";

    private PlayerStorage() {
        this.players = new HashMap<>();
        loadPlayers(); // Load players from file on startup
    }

    public static PlayerStorage getInstance() {
        if (instance == null) {
            instance = new PlayerStorage();
        }
        return instance;
    }

    public void addPlayer(Player player) {
        players.put(player.getName(), player);
        savePlayers();
    }

    public Player getPlayer(String name) {
        return players.get(name);
    }
    
    public boolean removePlayer(String name) {
        if (players.containsKey(name)) {
            players.remove(name);
            deletePlayerImage(name);
            savePlayers();
            return true;
        } else {
            System.out.println("Player not found: " + name);
            return false;
        }
    }

    private void deletePlayerImage(String playerName) {
        String imagePath = "src/main/tabs/avatars/" + playerName + ".png";
        File imageFile = new File(imagePath);

        if (imageFile.exists())
            imageFile.delete();
    }
    
    public Collection<Player> getAllPlayers() {
        return players.values();
    }


    public void listPlayers() {
        players.values().forEach(Player::displayInventory);
    }

    public int getPlayerCount() {
        return players.size();
    }

    private void savePlayers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Player player : players.values()) {
                writer.write(formatPlayerData(player));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Unable to save players.txt");
            e.printStackTrace();
        }
    }

    public void loadPlayers() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("Player list not found at: " + FILE_PATH);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Player player = parsePlayerData(line);
                if (player != null) {
                    players.put(player.getName(), player);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatPlayerData(Player player) {
        StringBuilder builder = new StringBuilder();
        builder.append(player.getName()).append(",")
               .append(player.getGold()).append(",")
               .append(player.isRich()).append(",");

        // Append inventory items
        Map<String, Integer> inventory = player.getInventory();
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            builder.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
        }

        return builder.toString();
    }

    private Player parsePlayerData(String line) {
        String[] parts = line.split(",", 4); // Split into 4 parts (name, gold, isRich, inventory)
        if (parts.length < 3) return null;

        String name = parts[0];
        int gold = Integer.parseInt(parts[1]);
        boolean isRich = Boolean.parseBoolean(parts[2]);
        Player player = new Player(name);
        player.setGold(gold);
        if (isRich) player.becomeRich();

        // Parse inventory if available
        if (parts.length == 4 && !parts[3].isEmpty()) {
            String[] items = parts[3].split(";");
            for (String item : items) {
                String[] itemParts = item.split(":");
                if (itemParts.length == 2) {
                    String itemName = itemParts[0];
                    int count = Integer.parseInt(itemParts[1]);
                    player.addItem(itemName, count);
                }
            }
        }

        return player;
    }
}
