package dungeonmania.entities.enemies.movements;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.GameState;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class PlayerHistoryMovement implements Movement {
    private ArrayList<GameState> gameStates;
    private int endTick;

    public PlayerHistoryMovement(List<GameState> gameStates, int endTick) {
        this.gameStates = new ArrayList<>(gameStates);
        this.endTick = endTick;
    }

    public void move(GameMap map, Enemy enemy) {
        int tick = map.getGame().getCurrentTick();
        Player previousPlayer = gameStates.get(tick).getPlayer();
        ++tick;
        if (tick >= endTick) {
            map.destroyEntity(enemy);
            return;
        }

        Player nextPlayer = gameStates.get(tick).getPlayer();
        Position nextPosition = nextPlayer.getPosition();
        if (!nextPosition.equals(previousPlayer.getPosition())) {
            previousPlayer.setFacing(nextPlayer.getFacing());
            map.moveTo(previousPlayer, nextPosition);
            map.removeNode(previousPlayer);
            map.moveTo(enemy, nextPosition);
        }
    }
}
