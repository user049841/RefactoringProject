package dungeonmania.entities.logic;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Wire extends Entity implements CircuitEntity {
    private ArrayList<CircuitEntity> subs = new ArrayList<>();
    private static final int ADD_ACTIVATION = 1;
    private static final int REMOVE_ACTIVATION = -1;


    // Keeps track of the number of switches 'activating' this entity
    // E.g. if the activatedCount is 3, then 3 switches must be deactivated to unpower the wire.
    private int activatedCount;
    private boolean notified = false;

    public Wire(Position position) {
        super(position);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void notify(boolean adding, int activatedCount) {
        notified = true;
        this.activatedCount += (adding) ? ADD_ACTIVATION : REMOVE_ACTIVATION;
        for (CircuitEntity circuitEntity : subs) {
            if (!(circuitEntity instanceof Wire) || !((Wire) circuitEntity).isNotified()) {
                circuitEntity.notify(adding, this.activatedCount);
            }
        }
    }

    public void addSub(List<? extends CircuitEntity> circuitEntities) {
        subs.addAll(circuitEntities);
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public boolean isNotified() {
        return notified;
    }
}
