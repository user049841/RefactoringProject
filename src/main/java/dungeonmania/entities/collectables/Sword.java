package dungeonmania.entities.collectables;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;
import dungeonmania.entities.Weapon;
import dungeonmania.util.Position;

public class Sword extends Collectable implements Weapon {
    public static final double DEFAULT_ATTACK = 1;
    public static final double DEFAULT_ATTACK_SCALE_FACTOR = 1;
    public static final int DEFAULT_DURABILITY = 5;
    public static final double DEFAULT_DEFENCE = 0;
    public static final double DEFAULT_DEFENCE_SCALE_FACTOR = 1;

    private int durability;
    private double attack;

    public Sword(Position position, double attack, int durability) {
        super(position);
        this.attack = attack;
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
            attack,
            0,
            1,
            1));
    }
}
