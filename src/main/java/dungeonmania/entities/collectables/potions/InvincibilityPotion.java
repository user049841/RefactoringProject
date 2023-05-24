package dungeonmania.entities.collectables.potions;

import java.util.function.Consumer;

import dungeonmania.entities.playerState.PlayerState;
import dungeonmania.util.Position;

public class InvincibilityPotion extends Potion {
    public static final int DEFAULT_DURATION = 8;

    public InvincibilityPotion(Position position, int duration) {
        super(position, duration);
    }

    @Override
    public Consumer<PlayerState> getTransition() {
        return PlayerState::transitionInvincible;
    }
}
