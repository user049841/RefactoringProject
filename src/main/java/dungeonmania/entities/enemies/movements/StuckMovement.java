package dungeonmania.entities.enemies.movements;

import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;

public class StuckMovement implements Movement {
    private int stuckTime = 0;
    private int movementFactor;
    private Movement previousMovement;

    public StuckMovement(int movementFactor, Movement previousMovement) {
        this.movementFactor = movementFactor;
        this.previousMovement = previousMovement;
    }

    @Override
    public void move(GameMap map, Enemy enemy) {
        ++stuckTime;
        if (stuckTime >= movementFactor) {
            enemy.setMovement(previousMovement);
        }
    }
}
