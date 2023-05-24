package dungeonmania.entities.collectables.potions;

import java.util.function.Consumer;

import dungeonmania.entities.collectables.Collectable;
import dungeonmania.entities.playerState.PlayerState;
import dungeonmania.util.Position;

public abstract class Potion extends Collectable {
    private int duration;

    public Potion(Position position, int duration) {
        super(position);
        this.duration = duration;
    }

    public abstract Consumer<PlayerState> getTransition();

    public int getDuration() {
        return duration;
    }
}
