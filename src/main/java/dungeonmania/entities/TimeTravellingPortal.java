package dungeonmania.entities;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class TimeTravellingPortal extends Entity {
    private static final int TIME_TRAVEL_TICKS = 30;

    public TimeTravellingPortal(Position position) {
        super(position);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Player)
            map.getGame().addTimeTravel(TIME_TRAVEL_TICKS);
    }
}
