package dungeonmania.entities.playerState;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;

public class InvincibleState extends PlayerState {
    private static final BattleStatistics BUFF = new BattleStatistics(
        0,
        0,
        0,
        1,
        1,
        true,
        true);

    public InvincibleState(Player player) {
        super(player);
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, BUFF);
    }

    @Override
    public void transitionBase() {
        changePlayerState(BaseState::new);
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
