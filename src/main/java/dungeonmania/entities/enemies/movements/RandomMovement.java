package dungeonmania.entities.enemies.movements;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class RandomMovement implements Movement {
    public void move(GameMap map, Enemy enemy) {
        Random randGen = new Random();
        List<Position> pos = enemy.getCardinallyAdjacentPositions();
        pos = pos
                .stream()
                .filter(p -> map.canMoveTo(enemy, p)).collect(Collectors.toList());

        Position nextPos = (pos.size() == 0) ? enemy.getPosition() : pos.get(randGen.nextInt(pos.size()));
        map.moveTo(enemy, nextPos);
    }
}
