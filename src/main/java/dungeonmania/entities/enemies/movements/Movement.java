package dungeonmania.entities.enemies.movements;

import java.io.Serializable;

import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;

public interface Movement extends Serializable {
    public void move(GameMap map, Enemy enemy);
}
