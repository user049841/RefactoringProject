package dungeonmania.entities.inventory.cost;

import java.io.Serializable;

import dungeonmania.entities.inventory.Inventory;

public interface ItemCost extends Serializable {
    public boolean isSatisfied(Inventory inventory);
    public void removeSpend(Inventory inventory);
}
