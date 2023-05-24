package dungeonmania.entities.collectables;

import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Key extends Collectable {
    private int number;

    public Key(Position position, int number) {
        super(position);
        this.number = number;
    }

    public int getnumber() {
        return number;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (map.getPlayer().countEntityOfType(Key.class) == 0)
            super.onOverlap(map, entity);
    }
}
