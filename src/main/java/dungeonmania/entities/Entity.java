package dungeonmania.entities;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import dungeonmania.Game;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public abstract class Entity implements Serializable {
    private Position position;
    private Position previousPosition;
    private Position previousDistinctPosition;
    private Direction facing;
    private String entityId;

    public Entity(Position position) {
        this.position = position;
        this.previousPosition = position;
        this.previousDistinctPosition = null;
        this.entityId = UUID.randomUUID().toString();
        this.facing = null;
    }

    public boolean canMoveOnto(GameMap map, Entity entity) {
        return false;
    }

    public void onOverlap(GameMap map, Entity entity) {
        return;
    }

    public void onMovedAway(GameMap map, Entity entity) {
        return;
    }

    public void onDestroy(Game game) {
        return;
    }

    public Position getPosition() {
        return position;
    }

    public int getXPosition() {
        return position.getX();
    }

    public int getYPosition() {
        return position.getY();
    }

    public List<Position> getAdjacentPositions() {
        return position.getAdjacentPositions();
    }

    public List<Position> getCardinallyAdjacentPositions() {
        return position.getCardinallyAdjacentPositions();
    }

    public Position getPreviousPosition() {
        return previousPosition;
    }

    public Position getPreviousDistinctPosition() {
        return previousDistinctPosition;
    }

    public void setPreviousDistinctPosition(Position position) {
        previousDistinctPosition = position;
    }

    public void setPreviousPosition(Position position) {
        previousPosition = position;
    }

    public String getId() {
        return entityId;
    }

    public void setId(String id) {
        entityId = id;
    }

    public void setPosition(Position position) {
        previousPosition = this.position;
        this.position = position;
        if (!previousPosition.equals(this.position)) {
            previousDistinctPosition = previousPosition;
        }
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }

    public Direction getFacing() {
        return this.facing;
    }
}
