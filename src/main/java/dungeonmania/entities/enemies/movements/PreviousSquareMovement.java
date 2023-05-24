package dungeonmania.entities.enemies.movements;

import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class PreviousSquareMovement implements Movement {
    public void move(GameMap map, Enemy enemy) {
        Position previousDistinctPosition = map.getPlayer().getPreviousDistinctPosition();
        if (previousDistinctPosition != null)
            map.moveTo(enemy, map.getPlayer().getPreviousDistinctPosition());
    }
}
