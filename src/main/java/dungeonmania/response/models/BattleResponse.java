package dungeonmania.response.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class BattleResponse implements Serializable {
    private final String enemy;
    private final double initialPlayerHealth;
    private final double initialEnemyHealth;
    private final ArrayList<ItemResponse> battleItems;
    private final ArrayList<RoundResponse> rounds;

    public BattleResponse() {
        this.initialPlayerHealth = 0;
        this.initialEnemyHealth = 0;
        this.enemy = "";
        this.battleItems = new ArrayList<>();
        this.rounds = new ArrayList<>();
    }

    public BattleResponse(String enemy, List<RoundResponse> rounds, List<ItemResponse> battleItems,
    double initialPlayerHealth, double initialEnemyHealth) {
        this.initialPlayerHealth = initialPlayerHealth;
        this.initialEnemyHealth = initialEnemyHealth;
        this.enemy = enemy;
        this.rounds = new ArrayList<>(rounds);
        this.battleItems = new ArrayList<>(battleItems);
    }

    public final String getEnemy() {
        return enemy;
    }

    public final double getInitialPlayerHealth() {
        return initialPlayerHealth;
    }

    public final double getInitialEnemyHealth() {
        return initialEnemyHealth;
    }

    public final List<RoundResponse> getRounds() {
        return rounds;
    }

    public final List<ItemResponse> getBattleItems() {
        return battleItems;
    }
}
