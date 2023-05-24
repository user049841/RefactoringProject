package dungeonmania;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.UUID;

import dungeonmania.battles.BattleFacade;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.logic.LogicalEntity;
import dungeonmania.entities.logic.Wire;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.goals.Goal;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.util.SerializableRunnable;

public class Game implements Serializable {
    private String id;
    private String name;
    private Goal goals;
    private GameMap map;
    private Player player;
    private BattleFacade battleFacade;
    private int treasureCollectedCount = 0;
    private int enemiesDestroyedCount = 0;
    private EntityFactory entityFactory;
    private boolean isInTick = false;
    private ArrayList<GameState> gameStates = new ArrayList<>();
    public static final int PLAYER_MOVEMENT = 0;
    public static final int POTION_EFFECT_UPDATE = 1;
    public static final int AI_MOVEMENT = 2;

    private int tickCount = 0;
    private int currentTick = 0;
    private ArrayList<ComparableCallback> sub = new ArrayList<>();
    private ArrayList<ComparableCallback> addingSub = new ArrayList<>();
    private ArrayList<SerializableRunnable> timeTravels = new ArrayList<>();

    public Game(String dungeonName) {
        this.name = dungeonName;
        this.map = new GameMap();
        this.battleFacade = new BattleFacade();
    }

    public void init() {
        this.id = UUID.randomUUID().toString();
        map.init();
        this.tickCount = 0;
        this.currentTick = 0;
        player = map.getPlayer();
        register(() -> player.onTick(tickCount), POTION_EFFECT_UPDATE, "potionQueue");
    }

    public Game tick(Direction movementDirection) {
        registerOnce(
            () -> player.move(this.getMap(), movementDirection), PLAYER_MOVEMENT, "playerMoves");
        tick();
        return this;
    }

    public Game tick(String itemUsedId) throws InvalidActionException {
        Entity item = player.getEntity(itemUsedId);
        if (item == null)
            throw new InvalidActionException(String.format("Item with id %s doesn't exist", itemUsedId));
        if (!(item instanceof Bomb) && !(item instanceof Potion))
            throw new IllegalArgumentException(String.format("%s cannot be used", item.getClass()));

        registerOnce(() -> {
            if (item instanceof Bomb)
                player.use((Bomb) item, map);
            if (item instanceof Potion)
                player.use((Potion) item, tickCount);
        }, PLAYER_MOVEMENT, "playerUsesItem");
        tick();
        return this;
    }

    /**
     * Battle between player and enemy
     * @param player
     * @param enemy
     */
    public void battle(Player player, Enemy enemy) {
        if (this.player != player) {
            return;
        }

        battleFacade.battle(this, player, enemy);
        if (player.getHealth() <= 0) {
            map.destroyEntity(player);
        }
        if (enemy.getHealth() <= 0) {
            map.destroyEntity(enemy);
        }
    }

    public Game build(String buildable) throws InvalidActionException {
        List<String> buildables = player.getBuildables(map);
        if (!buildables.contains(buildable)) {
            throw new InvalidActionException(String.format("%s cannot be built", buildable));
        }
        registerOnce(() -> player.build(buildable, entityFactory), PLAYER_MOVEMENT, "playerBuildsItem");
        tick();
        return this;
    }

    public Game interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        Entity e = map.getEntity(entityId);
        if (e == null || !(e instanceof Interactable))
            throw new IllegalArgumentException("Entity cannot be interacted");
        if (!((Interactable) e).isInteractable(player)) {
            throw new InvalidActionException("Entity cannot be interacted");
        }
        registerOnce(
            () -> ((Interactable) e).interact(player, this), PLAYER_MOVEMENT, "playerInteractsWithEntity");
        tick();
        return this;
    }

    public Game timeTravel(int ticks) {
        currentTick = Math.max(tickCount - ticks, 0);
        map.getEntities().forEach(entity -> {
            unsubscribe(entity.getId());
            map.removeNode(entity);
        });
        GameState gameState = gameStates.get(currentTick);
        gameState.getEntities().stream()
            .filter(entity -> !(entity instanceof Player))
            .forEach(this::addEntity);
        addEntity(map.getPlayer());
        addEntity(entityFactory.createOlderPlayer(this, gameStates, currentTick, tickCount));
        map.initTimeTravel();
        return this;
    }

    public void addTimeTravel(int ticks) {
        timeTravels.add(() -> timeTravel(ticks));
    }

    public <T extends Entity> long countEntities(Class<T> type) {
        return map.countEntities(type);
    }

    public <T extends Entity> List<T> getEntities(Class<T> type) {
        return map.getEntities(type);
    }

    public List<Entity> getEntities(Position p) {
        return map.getEntities(p);
    }

    public void addEntity(Entity entity) {
        map.addEntity(entity);
    }

    public void register(SerializableRunnable r, int priority, String id) {
        if (isInTick)
            addingSub.add(new ComparableCallback(r, priority, id));
        else
            sub.add(new ComparableCallback(r, priority, id));
    }

    public void registerOnce(SerializableRunnable r, int priority, String id) {
        if (isInTick)
            addingSub.add(new ComparableCallback(r, priority, id, true));
        else
            sub.add(new ComparableCallback(r, priority, id, true));
    }

    public void unsubscribe(String id) {
        Stream.concat(sub.stream(), addingSub.stream())
            .filter(c -> id.equals(c.getId()))
            .forEach(ComparableCallback::invalidate);
    }

    public int tick() {
        if (currentTick == tickCount)
            gameStates.add(GameState.saveState(map));

        isInTick = true;
        updateLogicalEntities();
        sub.sort(null);
        sub.forEach(s -> s.run());
        isInTick = false;
        sub.addAll(addingSub);
        addingSub = new ArrayList<>();
        sub.removeIf(s -> !s.isValid());
        tickCount++;
        currentTick++;

        timeTravels.forEach(SerializableRunnable::run);
        timeTravels.clear();

        // update the weapons/potions duration
        return tickCount;
    }

    private void updateLogicalEntities() {
        for (Wire wire : map.getEntities(Wire.class)) {
            wire.setNotified(false);
        }
        for (LogicalEntity logicalEntity : map.getEntities(LogicalEntity.class)) {
            logicalEntity.setActiveOnPriorTick(logicalEntity.isActivated());
            logicalEntity.resetActivatedThisTickCount();
        }
    }

    public int getTick() {
        return this.tickCount;
    }

    public int getCurrentTick() {
        return this.currentTick;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Goal getGoals() {
        return goals;
    }

    public void setGoals(Goal goals) {
        this.goals = goals;
    }

    public GameMap getMap() {
        return map;
    }

    public void setMap(GameMap map) {
        this.map = map;
    }

    public EntityFactory getEntityFactory() {
        return entityFactory;
    }

    public void setEntityFactory(EntityFactory factory) {
        entityFactory = factory;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public BattleFacade getBattleFacade() {
        return battleFacade;
    }

    public void setBattleFacade(BattleFacade battleFacade) {
        this.battleFacade = battleFacade;
    }

    public void incrementTreasureCollectedCount() {
        ++treasureCollectedCount;
    }

    public int getTreasureCollectedCount() {
        return treasureCollectedCount;
    }

    public void incrementEnemiesDestroyed() {
        ++enemiesDestroyedCount;
    }

    public int getEnemiesDestroyedCount() {
        return enemiesDestroyedCount;
    }
}
