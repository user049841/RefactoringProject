package dungeonmania.entities.inventory.cost;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.buildables.BuildableCost;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;

public class SingleItemCost implements ItemCost {
    private Class<? extends InventoryItem> itemType;
    private boolean alternativeExists;
    private int amount;

    public SingleItemCost(Class<? extends InventoryItem> itemType, int amount) {
        this.itemType = itemType;
        this.alternativeExists = (BuildableCost.getAlternativeIfExists(itemType) != null);
        this.amount = amount;
    }

    public Class<? extends InventoryItem> getItem() {
        return itemType;
    }

    @Override
    public boolean isSatisfied(Inventory inventory) {
        return inventory.count(itemType)
                + ((alternativeExists) ? inventory.count(BuildableCost.getAlternativeIfExists(itemType)) : 0) >= amount;
    }

    @Override
    public void removeSpend(Inventory inventory) {
        ArrayList<InventoryItem> items = new ArrayList<>(inventory.getEntities(itemType));
        if (alternativeExists) {
            List<? extends InventoryItem> alternativeItems = inventory
                    .getEntities(BuildableCost.getAlternativeIfExists(itemType));
            // Note that by appending alternatives to the end of the array, we force non-alternatives to be higher
            // priority.
            for (InventoryItem item : alternativeItems) {
                if (items.size() >= amount) break;
                inventory.addToRefund(item);
                items.add(item);
            }
        }
        inventory.removeAll(items.subList(0, amount));
    }
}
