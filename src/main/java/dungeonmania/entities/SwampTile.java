package dungeonmania.entities;

import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.entities.enemies.movements.StuckMovement;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SwampTile extends Entity {
    public static final int DEFAULT_MOVEMENT_FACTOR = 1;
    private int movementFactor;

    public SwampTile(Position position, int movementFactor) {
        super(position);
        this.movementFactor = movementFactor;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Mercenary && ((Mercenary) entity).isAllied()
            && Position.isAdjacent(entity.getPosition(), map.getPlayer().getPosition())) {
            return;
        }

        if (movementFactor > 0 && entity instanceof Enemy) {
            Enemy enemy = (Enemy) entity;
            enemy.setMovement(new StuckMovement(movementFactor, enemy.getMovement()));
        }
    }

    public int getMovementFactor() {
        return movementFactor;
    }
}
