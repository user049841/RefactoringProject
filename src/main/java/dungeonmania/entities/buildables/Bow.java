package dungeonmania.entities.buildables;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;
import dungeonmania.entities.Weapon;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.cost.AndItemCost;
import dungeonmania.entities.inventory.cost.ItemCost;
import dungeonmania.entities.inventory.cost.SingleItemCost;

public class Bow extends Buildable implements Weapon {
    public static final ItemCost COST = new AndItemCost(
        new SingleItemCost(Wood.class, 1),
        new SingleItemCost(Arrow.class, 3));

    private int durability;

    public Bow(int durability) {
        super(null);
        this.durability = durability;
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
            0,
            2,
            1));
    }
}
