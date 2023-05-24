package dungeonmania.entities;

import dungeonmania.map.GameMap;

import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.enemies.Spider;
import dungeonmania.util.Position;

public class Door extends Entity implements DoorType {
    private boolean open = false;
    private int number;

    public Door(Position position, int number) {
        super(position.asLayer(Position.DOOR_LAYER));
        this.number = number;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return open || entity instanceof Spider
                || (entity instanceof Player && playerCanEnter((Player) entity));
    }

    private boolean playerCanEnter(Player player) {
        return player.hasMatchingKey(this) || player.countEntityOfType(SunStone.class) > 0;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (!(entity instanceof Player))
            return;

        Player player = (Player) entity;
        if (player.hasMatchingKey(this)) {
            player.use(Key.class);
            open();
        } else if (player.countEntityOfType(SunStone.class) > 0) {
            open();
        }
    }

    public boolean isMatchingKey(Key key) {
        return key != null && key.getnumber() == number;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    public void open() {
        open = true;
    }
}
