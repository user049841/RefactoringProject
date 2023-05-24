package dungeonmania.entities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.logic.CircuitEntity;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Switch extends Entity {
    private boolean activated;
    private ArrayList<Bomb> bombs = new ArrayList<>();
    private ArrayList<CircuitEntity> circuitEntities = new ArrayList<>();
    private static final int SEND_NO_SIGNAL = 0;
    private static final int SEND_ACTIVATION_SIGNAL = 1;

    public Switch(Position position) {
        super(position.asLayer(Position.ITEM_LAYER));
    }

    public void subscribe(Bomb b) {
        bombs.add(b);
    }

    public void subscribe(Bomb bomb, GameMap map) {
        bombs.add(bomb);
        if (activated) {
            bombs.stream().forEach(b -> b.notify(map));
        }
    }

    public void unsubscribe(Bomb b) {
        bombs.remove(b);
    }

    public void addSubLogic(List<? extends CircuitEntity> circuitEntities) {
        this.circuitEntities.addAll(circuitEntities);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Boulder) {
            activated = true;
            bombs.stream().forEach(b -> b.notify(map));
            circuitEntities.stream().forEach(c -> c.notify(true, SEND_ACTIVATION_SIGNAL));
        }
    }

    @Override
    public void onMovedAway(GameMap map, Entity entity) {
        if (entity instanceof Boulder) {
            activated = false;
            circuitEntities.stream().forEach(c -> c.notify(false, SEND_NO_SIGNAL));
        }
    }

    public boolean isActivated() {
        return activated;
    }
}
