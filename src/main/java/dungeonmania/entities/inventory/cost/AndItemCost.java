package dungeonmania.entities.inventory.cost;

import java.util.ArrayList;
import java.util.Arrays;

import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;

public class AndItemCost implements ItemCost {
    private ArrayList<ItemCost> costs;

    public AndItemCost(ItemCost... costs) {
        this.costs = new ArrayList<>(Arrays.asList(costs));
    }

    @Override
    public boolean isSatisfied(Inventory inventory) {
        // Preserve original inventory
        ArrayList<InventoryItem> items = new ArrayList<>(inventory.getEntities(InventoryItem.class));
        boolean result = true;
        // By looping through the costs and continually removing items, we guarantee that in that case of interchanging
        // a key/treasure with a sunstone, we avoid reusing the sunstone as another material.
        for (ItemCost cost : costs) {
            if (!cost.isSatisfied(inventory)) {
                result = false;
                break;
            }
            cost.removeSpend(inventory);
        }
        inventory.setInventory(items);
        inventory.clearRefundables();
        return result;
    }

    @Override
    public void removeSpend(Inventory inventory) {
        for (ItemCost cost : costs) {
            cost.removeSpend(inventory);
        }
    }
}
