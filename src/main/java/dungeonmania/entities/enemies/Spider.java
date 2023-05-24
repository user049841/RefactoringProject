package dungeonmania.entities.enemies;

import dungeonmania.entities.enemies.movements.CircularMovement;
import dungeonmania.util.Position;

public class Spider extends Enemy {
    public static final int DEFAULT_SPAWN_INTERVAL = 0;
    public static final double DEFAULT_ATTACK = 5;
    public static final double DEFAULT_HEALTH = 10;
    public static final int SPAWN_RADIUS = 20;
    private static final int INITIAL_NEXT_POSITION_ELEMENT = 1;
    private static final boolean INITIAL_FORWARD = true;

    public Spider(Position position, double health, double attack) {
        super(position.asLayer(Position.DOOR_LAYER + 1), health, attack);
        /**
         * Establish spider movement trajectory Spider moves as follows:
         *  8 1 2       10/12  1/9  2/8
         *  7 S 3       11     S    3/7
         *  6 5 4       B      5    4/6
         */
        setMovement(new CircularMovement(
            position.getAdjacentPositions(), INITIAL_NEXT_POSITION_ELEMENT, INITIAL_FORWARD));
    };
}
