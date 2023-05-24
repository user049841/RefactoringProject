package dungeonmania.battles;

import java.io.Serializable;

/**
 * Entities implement this interface can do battles
 */
public interface Battleable extends Serializable {
    public BattleStatistics getBattleStatistics();

    default double getHealth() {
        return getBattleStatistics().getHealth();
    }

    default void setHealth(double health) {
        getBattleStatistics().setHealth(health);
    }
}
