package dungeonmania.entities.collectables;

import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.Switch;
import dungeonmania.map.GameMap;

public class Bomb extends Collectable {
    public enum State {
        SPAWNED,
        INVENTORY,
        PLACED
    }

    public static final int DEFAULT_RADIUS = 1;
    private State state;
    private int radius;

    private ArrayList<Switch> subs = new ArrayList<>();

    public Bomb(Position position, int radius) {
        super(position);
        state = State.SPAWNED;
        this.radius = radius;
    }

    public void subscribe(Switch s) {
        this.subs.add(s);
    }

    public void notify(GameMap map) {
        explode(map);
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (state != State.SPAWNED) return;
        if (entity instanceof Player) {
            subs.stream().forEach(s -> s.unsubscribe(this));
            super.onOverlap(map, entity);
        }
        this.state = State.INVENTORY;
    }

    public void onPutDown(GameMap map, Position p) {
        setPosition(p);
        map.addEntity(this);
        this.state = State.PLACED;
        List<Position> adjPosList = getCardinallyAdjacentPositions();
        adjPosList.stream().forEach(node -> {
            List<Switch> switches = map.getEntities(node, Switch.class);
            switches.forEach(s -> s.subscribe(this, map));
            switches.forEach(this::subscribe);
        });
    }

    /**
     * it destroys all entities in diagonally and cardinally adjacent cells, except for the player
     * @param map
     */
    public void explode(GameMap map) {
        int x = getPosition().getX();
        int y = getPosition().getY();
        for (int i = x - radius; i <= x + radius; i++) {
            for (int j = y - radius; j <= y + radius; j++) {
                map.getEntities(new Position(i, j))
                    .stream()
                    .filter(e -> !(e instanceof Player))
                    .forEach(map::destroyEntity);
            }
        }
    }

    public State getState() {
        return state;
    }
}
