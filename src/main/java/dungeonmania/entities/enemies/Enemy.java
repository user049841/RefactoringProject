package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.Battleable;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.movements.Movement;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class Enemy extends Entity implements Battleable {
    private BattleStatistics battleStatistics;
    private Movement movementStrategy;

    public Enemy(Position position, double health, double attack) {
        super(position.asLayer(Position.CHARACTER_LAYER));
        battleStatistics = new BattleStatistics(
                health,
                attack,
                0,
                BattleStatistics.DEFAULT_DAMAGE_MAGNIFIER,
                BattleStatistics.DEFAULT_ENEMY_DAMAGE_REDUCER);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return battleStatistics;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            player.startBattle(map.getGame(), this);
        }
    }

    @Override
    public void onDestroy(Game game) {
        game.unsubscribe(getId());
        game.incrementEnemiesDestroyed();
    }

    public void move(Game game) {
        movementStrategy.move(game.getMap(), this);
    }

    public Movement getMovement() {
        return movementStrategy;
    }

    public void setMovement(Movement movement) {
        movementStrategy = movement;
    }
}
