package dungeonmania.entities.playerState;

import java.io.Serializable;
import java.util.function.Function;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;

public abstract class PlayerState implements Serializable {
    private Player player;

    PlayerState(Player player) {
        this.player = player;
    }

    public abstract BattleStatistics applyBuff(BattleStatistics origin);

    protected void changePlayerState(Function<Player, PlayerState> newState) {
        player.changeState(newState.apply(player));
    }

    public abstract void transitionInvisible();
    public abstract void transitionInvincible();
    public abstract void transitionBase();
}
