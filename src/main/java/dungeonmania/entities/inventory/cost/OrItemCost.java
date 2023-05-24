package dungeonmania.entities.inventory.cost;

import dungeonmania.entities.inventory.Inventory;

public class OrItemCost implements ItemCost {
    private SingleItemCost cost1;
    private SingleItemCost cost2;

    public OrItemCost(SingleItemCost cost1, SingleItemCost cost2) {
        this.cost1 = cost1;
        this.cost2 = cost2;
    }

    @Override
    public boolean isSatisfied(Inventory inventory) {
        return cost1.isSatisfied(inventory) || cost2.isSatisfied(inventory);
    }

    @Override
    public void removeSpend(Inventory inventory) {
        // Note that this method can be called by AndItemCost in the case of building a sceptre, and if we are building
        // a sceptre with 1 wood, 1 treasure, and 1 sunstone, AndItemCost may call removeSpend on the OrItemCost of a
        // key/treasure before calling it on the SingleItemCost of the sun stone. If we do not prioritise using the
        // key/treasure over the sunstone, we may not be able to build the sceptre. This means that we must prioritise
        // using the non-alternative cost in order to guarantee we have the sun stone available for the SingleItemCost
        // instance.
        // This works since SingleItemCost also prioritises using non-alternative items.

        if (inventory.count(cost1.getItem()) == 0 && cost2.isSatisfied(inventory)) {
            cost2.removeSpend(inventory);
        } else {
            cost1.removeSpend(inventory);
        }
    }
}
