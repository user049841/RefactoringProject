package dungeonmania.entities.inventory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Weapon;

public class Inventory implements Serializable {
    private ArrayList<InventoryItem> items = new ArrayList<>();
    private ArrayList<InventoryItem> refundables = new ArrayList<>();

    public void add(InventoryItem item) {
        items.add(item);
    }

    public void remove(InventoryItem item) {
        items.remove(item);
    }

    public void removeAll(List<? extends InventoryItem> items) {
        this.items.removeAll(items);
    }

    public void setInventory(List<? extends InventoryItem> items) {
        this.items.clear();
        this.items.addAll(items);
    }

    public void addToRefund(InventoryItem item) {
        refundables.add(item);
    }

    public void clearRefundables() {
        refundables.clear();
    }

    public void refundUnused() {
        items.addAll(refundables);
        refundables.clear();
    }

    public <T extends InventoryItem> T getFirst(Class<T> itemType) {
        return items.stream().filter(itemType::isInstance).map(itemType::cast).findFirst().orElse(null);
    }

    public <T extends InventoryItem> int count(Class<T> itemType) {
        return Math.toIntExact(items.stream().filter(itemType::isInstance).count());
    }

    public Entity getEntity(String itemUsedId) {
        return items
                .stream()
                .filter(Entity.class::isInstance)
                .map(Entity.class::cast)
                .filter(item -> item.getId().equals(itemUsedId))
                .findFirst()
                .orElse(null);
    }

    public List<Entity> getEntities() {
        return getEntities(Entity.class);
    }

    public <T> List<T> getEntities(Class<T> clz) {
        return items.stream().filter(clz::isInstance).map(clz::cast).collect(Collectors.toList());
    }

    public boolean hasWeapon() {
        return items.stream().anyMatch(Weapon.class::isInstance);
    }
}
