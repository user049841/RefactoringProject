package dungeonmania.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.Portal;
import dungeonmania.entities.SwampTile;
import dungeonmania.entities.Switch;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.ZombieToastSpawner;
import dungeonmania.entities.logic.LogicalEntity;
import dungeonmania.entities.logic.Wire;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class GameMap implements Serializable {
    private Game game;
    private HashMap<Position, GraphNode> nodes = new HashMap<>();
    private Player player;

    public void init() {
        initPairPortals();
        initRegisterEnemies();
        initRegisterZombieToastSpawners();
        initRegisterSpiderSpawners();
        initRegisterBombsAndSwitches();
        initLogicalEntities();
    }

    private void initLogicalEntities() {
        for (Wire wire : getEntities(Wire.class)) {
            for (Position pos : wire.getCardinallyAdjacentPositions()) {
                wire.addSub(getEntities(pos, LogicalEntity.class));
                wire.addSub(getEntities(pos, Wire.class));
            }
        }
        for (LogicalEntity logicalEntity : getEntities(LogicalEntity.class)) {
            for (Position pos : logicalEntity.getCardinallyAdjacentPositions()) {
                logicalEntity.addSubCount(getEntities(pos, Switch.class).size());
                logicalEntity.addSubCount(getEntities(pos, Wire.class).size());
            }
        }
        for (Switch switchEntity : getEntities(Switch.class)) {
            for (Position pos : switchEntity.getCardinallyAdjacentPositions()) {
                switchEntity.addSubLogic(getEntities(pos, LogicalEntity.class));
                switchEntity.addSubLogic(getEntities(pos, Wire.class));
            }
        }
    }

    public void initTimeTravel() {
        initRegisterEnemies();
        initRegisterZombieToastSpawners();
    }

    private void initRegisterBombsAndSwitches() {
        List<Bomb> bombs = getEntities(Bomb.class);
        List<Switch> switches = getEntities(Switch.class);
        for (Bomb b: bombs) {
            for (Switch s: switches) {
                if (Position.isAdjacent(b.getPosition(), s.getPosition())) {
                    b.subscribe(s);
                    s.subscribe(b);
                }
            }
        }
    }

    // Pair up portals if there's any
    private void initPairPortals() {
        Map<String, Portal> portalsMap = new HashMap<>();
        getEntities(Portal.class).forEach(portal -> {
            String color = portal.getColor();
            if (portalsMap.containsKey(color)) {
                portal.bind(portalsMap.get(color));
            } else {
                portalsMap.put(color, portal);
            }
        });
    }

    private void initRegisterEnemies() {
        List<Enemy> enemies = getEntities(Enemy.class);
        enemies.forEach(e -> {
            game.register(() -> e.move(game), Game.AI_MOVEMENT, e.getId());
        });
    }

    private void initRegisterZombieToastSpawners() {
        List<ZombieToastSpawner> zts = getEntities(ZombieToastSpawner.class);
        zts.forEach(e -> {
            game.register(() -> e.spawn(game), Game.AI_MOVEMENT, e.getId());
        });
    }

    private void initRegisterSpiderSpawners() {
        game.register(() -> game.getEntityFactory().spawnSpider(game), Game.AI_MOVEMENT, "spiderSpawner");
    }

    public void moveTo(Entity entity, Position position) {
        if (!canMoveTo(entity, position)) return;

        Position previousDistinctPosition = entity.getPreviousDistinctPosition();
        Position previousPosition = entity.getPosition();

        triggerMovingAwayEvent(entity);
        removeNode(entity);
        entity.setPosition(position);
        addEntity(entity);
        triggerOverlapEvent(entity);

        entity.setPreviousPosition(previousPosition);
        if (!entity.getPosition().equals(previousPosition)) {
            entity.setPreviousDistinctPosition(previousPosition);
        } else {
            entity.setPreviousDistinctPosition(previousDistinctPosition);
        }
    }

    public void moveTo(Entity entity, Direction direction) {
        moveTo(entity, Position.translateBy(entity.getPosition(), direction));
    }

    private void triggerMovingAwayEvent(Entity entity) {
        triggerEvent(e -> e::onMovedAway, entity);
    }

    private void triggerOverlapEvent(Entity entity) {
        triggerEvent(e -> e::onOverlap, entity);
    }

    private void triggerEvent(Function<Entity, BiConsumer<GameMap, Entity>> event, Entity entity) {
        List<Runnable> callbacks = getEntities(entity.getPosition())
            .stream()
            .filter(e -> e != entity)
            .map(e -> (Runnable) () -> event.apply(e).accept(this, entity))
            .collect(Collectors.toList());
        callbacks.forEach(Runnable::run);
    }

    public boolean canMoveTo(Entity entity, Position position) {
        return !nodes.containsKey(position) || nodes.get(position).canMoveOnto(this, entity);
    }

    public Position dijkstraPathFind(Entity e1, Entity e2) {
        return dijkstraPathFind(e1.getPosition(), e2.getPosition(), e1);
    }

    public Position dijkstraPathFind(Position src, Position dest, Entity entity) {
        // if inputs are invalid, don't move
        if (!nodes.containsKey(src) || !nodes.containsKey(dest)) return src;

        Map<Position, Integer> dist = new HashMap<>();
        Map<Position, Position> prev = new HashMap<>();
        Map<Position, Boolean> visited = new HashMap<>();

        prev.put(src, null);
        dist.put(src, 0);

        PriorityQueue<Position> q = new PriorityQueue<>((x, y) ->
            Integer.compare(dist.getOrDefault(x, Integer.MAX_VALUE), dist.getOrDefault(y, Integer.MAX_VALUE)));
        q.add(src);

        while (!q.isEmpty()) {
            Position curr = q.poll();
            if (curr.equals(dest)) break;
            // check portal
            if (!getEntities(curr, Portal.class).isEmpty()) {
                updateTeleportDistance(curr, dist, prev, visited, q, entity);
                continue;
            }

            visited.put(curr, true);
            List<Position> neighbours = getNeighbours(curr, visited, entity);
            neighbours.forEach(n -> updateNeighbourDistance(n, curr, dist, prev, q));
        }
        return getNextPosition(src, dest, prev);
    }

    private void updateTeleportDistance(
            Position curr,
            Map<Position, Integer> dist,
            Map<Position, Position> prev,
            Map<Position, Boolean> visited,
            PriorityQueue<Position> q,
            Entity entity) {
        Portal portal = getEntities(curr, Portal.class).get(0);
        List<Position> teleportDest = portal.getDestPositions(this, entity);
        teleportDest.stream()
            .filter(p -> !visited.containsKey(p))
            .forEach(p -> {
                dist.put(p, dist.get(curr));
                prev.put(p, prev.get(curr));
                q.add(p);
            });
    }

    private List<Position> getNeighbours(Position curr, Map<Position, Boolean> visited, Entity entity) {
        return curr.getCardinallyAdjacentPositions()
            .stream()
            .filter(p -> !visited.containsKey(p))
            .filter(p -> !nodes.containsKey(p) || nodes.get(p).canMoveOnto(this, entity))
            .collect(Collectors.toList());
    }

    private void updateNeighbourDistance(
            Position neighbour,
            Position curr,
            Map<Position, Integer> dist,
            Map<Position, Position> prev,
            PriorityQueue<Position> q) {
        int newDist = dist.get(curr) + 1
            + getEntities(curr, SwampTile.class).stream().mapToInt(SwampTile::getMovementFactor).sum();
        if (newDist < dist.getOrDefault(neighbour, Integer.MAX_VALUE)) {
            q.remove(neighbour);
            dist.put(neighbour, newDist);
            prev.put(neighbour, curr);
            q.add(neighbour);
        }
    }

    private Position getNextPosition(Position src, Position dest, Map<Position, Position> prev) {
        Position ret = dest;
        if (prev.get(ret) == null || ret.equals(src)) return src;
        while (!prev.get(ret).equals(src)) {
            ret = prev.get(ret);
        }
        return ret;
    }

    public void removeNode(Entity entity) {
        Position p = entity.getPosition();
        if (nodes.containsKey(p)) {
            nodes.get(p).removeEntity(entity);
            if (nodes.get(p).size() == 0) {
                nodes.remove(p);
            }
        }
    }

    public void destroyEntity(Entity entity) {
        removeNode(entity);
        entity.onDestroy(game);
    }

    public void addEntity(Entity entity) {
        addNode(new GraphNode(entity));
    }

    public void addNode(GraphNode node) {
        Position p = node.getPosition();

        if (!nodes.containsKey(p))
        nodes.put(p, node);
        else {
            GraphNode curr = nodes.get(p);
            curr.mergeNode(node);
            nodes.put(p, curr);
        }
    }

    public Entity getEntity(String id) {
        return getEntities().stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Entity> getEntities(Position p) {
        GraphNode node = nodes.get(p);
        return (node != null) ? node.getEntities() : new ArrayList<>();
    }

    public List<Entity> getEntities() {
        List<Entity> entities = new ArrayList<>();
        nodes.forEach((k, v) -> entities.addAll(v.getEntities()));
        return entities;
    }

    public <T extends Entity> List<T> getEntities(Class<T> type) {
        return getEntities().stream().filter(type::isInstance).map(type::cast).collect(Collectors.toList());
    }

    public <T extends Entity> List<T> getEntities(Position p, Class<T> type) {
        List<T> entities = getEntities(type);
        entities.retainAll(getEntities(p));
        return entities;
    }

    public <T extends Entity> long countEntities(Class<T> type) {
        return getEntities().stream().filter(type::isInstance).count();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
