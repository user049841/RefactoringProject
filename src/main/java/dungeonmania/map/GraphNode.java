package dungeonmania.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class GraphNode implements Serializable {
    private Position position;
    private HashSet<Entity> entities = new HashSet<>();

    public GraphNode(Entity entity) {
        this(entity, entity.getPosition());
    }

    public GraphNode(Entity entity, Position p) {
        this.position = p;
        this.entities.add(entity);
    }

    public boolean canMoveOnto(GameMap map, Entity entity) {
        return entities.stream().allMatch(e -> e.canMoveOnto(map, entity));
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public int size() {
        return entities.size();
    }

    public void mergeNode(GraphNode node) {
        entities.addAll(node.entities);
    }

    public List<Entity> getEntities() {
        return new ArrayList<>(entities);
    }

    public Position getPosition() {
        return position;
    }
}
