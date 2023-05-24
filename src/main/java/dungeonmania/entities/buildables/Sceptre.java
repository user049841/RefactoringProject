package dungeonmania.entities.buildables;

import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.cost.AndItemCost;
import dungeonmania.entities.inventory.cost.ItemCost;
import dungeonmania.entities.inventory.cost.OrItemCost;
import dungeonmania.entities.inventory.cost.SingleItemCost;

public class Sceptre extends Buildable {
    public static final int DEFAULT_DURATION = 10;
    public static final ItemCost COST = new AndItemCost(
        new SingleItemCost(SunStone.class, 1),
        new OrItemCost(new SingleItemCost(Treasure.class, 1), new SingleItemCost(Key.class, 1)),
        new OrItemCost(new SingleItemCost(Wood.class, 1), new SingleItemCost(Arrow.class, 2)));

    private int duration;

    public Sceptre(int duration) {
        super(null);
        this.duration = duration;
    }

    public int getMindControlDuration() {
        return duration;
    }
}
