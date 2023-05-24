package dungeonmania.entities.buildables;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.cost.AndItemCost;
import dungeonmania.entities.inventory.cost.ItemCost;
import dungeonmania.entities.inventory.cost.OrItemCost;
import dungeonmania.entities.inventory.cost.SingleItemCost;

public class Shield extends Buildable implements BattleItem {
    public static final ItemCost COST = new AndItemCost(
        new SingleItemCost(Wood.class, 2),
        new OrItemCost(new SingleItemCost(Treasure.class, 1), new SingleItemCost(Key.class, 1)));

    private int durability;
    private double defence;

    public Shield(int durability, double defence) {
        super(null);
        this.durability = durability;
        this.defence = defence;
    }

    @Override
    public void use(Player player) {
        durability--;
        if (durability <= 0) {
            player.remove(this);
        }
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(
            0,
            0,
            defence,
            1,
            1));
    }
}
