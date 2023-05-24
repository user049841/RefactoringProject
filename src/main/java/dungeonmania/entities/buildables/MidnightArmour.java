package dungeonmania.entities.buildables;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.inventory.cost.AndItemCost;
import dungeonmania.entities.inventory.cost.ItemCost;
import dungeonmania.entities.inventory.cost.SingleItemCost;

public class MidnightArmour extends Buildable implements BattleItem {
    public static final ItemCost COST = new AndItemCost(
        new SingleItemCost(Sword.class, 1),
        new SingleItemCost(SunStone.class, 1));

    public static final double DEFAULT_ATTACK = 3;
    public static final double DEFAULT_DEFENCE = 3;

    private double attack;
    private double defence;

    public MidnightArmour(double attack, double defence) {
        super(null);
        this.attack = attack;
        this.defence = defence;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(
            0,
            attack,
            defence,
            1,
            1));
    }

    @Override
    public void use(Player player) {
        return;
    }
}
