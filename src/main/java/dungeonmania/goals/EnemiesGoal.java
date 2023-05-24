package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.entities.enemies.ZombieToastSpawner;

public class EnemiesGoal implements Goal {
    public static final int DEFAULT_TARGET = 0;
    private int target;

    public EnemiesGoal(int target) {
        this.target = target;
    }

    public boolean achieved(Game game) {
        return game.getEnemiesDestroyedCount() >= target && game.countEntities(ZombieToastSpawner.class) == 0;
    }

    public String toString(Game game) {
        if (achieved(game)) return "";
        return ":enemies";
    }
}
