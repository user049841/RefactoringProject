package dungeonmania.entities.playerState;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;

public class BaseState extends PlayerState {
    public BaseState(Player player) {
        super(player);
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return origin;
    }

    @Override
    public void transitionBase() {
        // Do nothing
    }

    @Override
    public void transitionInvincible() {
        changePlayerState(InvincibleState::new);
    }

    @Override
    public void transitionInvisible() {
        changePlayerState(InvisibleState::new);
    }
}
