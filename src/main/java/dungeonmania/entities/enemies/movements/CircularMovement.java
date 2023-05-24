package dungeonmania.entities.enemies.movements;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.Boulder;
import dungeonmania.entities.Entity;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class CircularMovement implements Movement {
    private ArrayList<Position> movementTrajectory;
    private int nextPositionElement;
    private boolean forward;

    public CircularMovement(List<Position> movementTrajectory, int nextPositionElement, boolean forward) {
        this.movementTrajectory = new ArrayList<>(movementTrajectory);
        this.nextPositionElement = nextPositionElement;
        this.forward = forward;
    }

    private void updateNextPosition() {
        if (forward) {
            nextPositionElement++;
            if (nextPositionElement == 8) {
                nextPositionElement = 0;
            }
        } else {
            nextPositionElement--;
            if (nextPositionElement == -1) {
                nextPositionElement = 7;
            }
        }
    }

    public void move(GameMap map, Enemy enemy) {
        Position nextPos = movementTrajectory.get(nextPositionElement);
        List<Entity> entities = map.getEntities(nextPos);
        if (entities != null && entities.size() > 0 && entities.stream().anyMatch(e -> e instanceof Boulder)) {
            forward = !forward;
            updateNextPosition();
            updateNextPosition();
        }
        nextPos = movementTrajectory.get(nextPositionElement);
        entities = map.getEntities(nextPos);
        if (entities == null
                || entities.size() == 0
                || entities.stream().allMatch(e -> e.canMoveOnto(map, enemy))) {
            map.moveTo(enemy, nextPos);
            updateNextPosition();
        }
    }
}
