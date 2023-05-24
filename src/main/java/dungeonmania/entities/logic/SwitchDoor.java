package dungeonmania.entities.logic;

import dungeonmania.entities.DoorType;
import dungeonmania.entities.Entity;
import dungeonmania.entities.enemies.Spider;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SwitchDoor extends LogicalEntity implements DoorType {
    public SwitchDoor(Position position, LogicRule logicRule) {
        super(position.asLayer(Position.DOOR_LAYER), logicRule);
    }

    @Override
    public boolean isOpen() {
        return isActivated();
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return isOpen() || entity instanceof Spider;
    }
}
