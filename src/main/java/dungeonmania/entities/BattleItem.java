package dungeonmania.entities;

import java.io.Serializable;

import dungeonmania.battles.BattleStatistics;

/**
 * Item has buff in battles
 */
public interface BattleItem extends Serializable {
    public BattleStatistics applyBuff(BattleStatistics origin);
    public void use(Player player);
}
