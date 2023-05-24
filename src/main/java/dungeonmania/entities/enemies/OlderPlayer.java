package dungeonmania.entities.enemies;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Game;
import dungeonmania.GameState;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.enemies.movements.PlayerHistoryMovement;
import dungeonmania.util.Position;

public class OlderPlayer extends Enemy {
    private static final double DEFAULT_HEALTH = 0;
    private static final double DEFAULT_ATTACK = 0;

    private ArrayList<GameState> gameStates;
    private Game game;
    private int endTick;
    private BattleStatistics baseStatistics;
    private BattleStatistics battleStatistics;

    public OlderPlayer(Position position, Game game, List<GameState> gameStates, int endTick) {
        super(position, DEFAULT_HEALTH, DEFAULT_ATTACK);
        this.gameStates = new ArrayList<>(gameStates);
        this.game = game;
        this.endTick = endTick;
        this.baseStatistics = gameStates.get(game.getCurrentTick()).getPlayer().getBattleStatistics();
        this.battleStatistics = this.baseStatistics;
        setMovement(new PlayerHistoryMovement(gameStates, endTick));
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        baseStatistics.setHealth(battleStatistics.getHealth());

        Player player = gameStates.get(game.getCurrentTick()).getPlayer();

        BattleStatistics playerBuff = new BattleStatistics(0, 0, 0, 1,
                BattleStatistics.DEFAULT_ENEMY_DAMAGE_REDUCER / BattleStatistics.DEFAULT_PLAYER_DAMAGE_REDUCER);
        Potion effectivePotion = player.getEffectivePotion();
        if (effectivePotion != null) {
            playerBuff = player.applyBuff(playerBuff);
        } else {
            for (BattleItem item : player.getItems(BattleItem.class))
                playerBuff = item.applyBuff(playerBuff);
        }

        battleStatistics = BattleStatistics.applyBuff(baseStatistics, playerBuff);

        return battleStatistics;
    }
}
