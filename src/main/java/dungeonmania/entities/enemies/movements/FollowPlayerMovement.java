package dungeonmania.entities.enemies.movements;

import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class FollowPlayerMovement implements Movement {
    public void move(GameMap map, Enemy enemy) {
        Position nextPos = map.dijkstraPathFind(enemy, map.getPlayer());
        map.moveTo(enemy, nextPos);
    }
}
