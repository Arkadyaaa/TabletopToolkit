package main.player;

import java.util.HashMap;
import java.util.Map;

public class Player {
    private String name;
    private Map<String, Integer> inventory;
    private int gold;
    private boolean isRich;

    public Player(String name) {
        this.name = name;
        this.inventory = new HashMap<>();
        this.gold = 0;
        this.isRich = false;
    }

    public String getName() {
        return name;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void addGold(int amount) {
        this.gold += amount;
    }

    public void subtractGold(int amount) {
        this.gold -= amount;
    }

    public boolean isRich() {
        return isRich;
    }

    public void becomeRich() {
        this.isRich = true;
    }

    public void becomeBroke() {
        this.isRich = false;
    }

    public void addItem(String item, int count) {
        if (count <= 0) return;
        inventory.put(item, inventory.getOrDefault(item, 0) + count);
    }

    public void removeItem(String item, int count) {
        if (!inventory.containsKey(item) || count <= 0) return;
        int newCount = inventory.get(item) - count;
        if (newCount > 0) {
            inventory.put(item, newCount);
        } else {
            inventory.remove(item);
        }
    }

    public int getItemCount(String item) {
        return inventory.getOrDefault(item, 0);
    }

    public void displayInventory() {
        System.out.println(name + "'s Inventory:");
        if (inventory.isEmpty()) {
            System.out.println("  (empty)");
        } else {
            for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue());
            }
        }
        System.out.println("Gold: " + gold);
        System.out.println("Status: " + (isRich ? "Rich" : "Poor"));
    }

    public Map<String, Integer> getInventory() {
        return new HashMap<>(inventory);
    }
}
