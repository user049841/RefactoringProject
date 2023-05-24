package dungeonmania.entities;

import java.io.Serializable;

import dungeonmania.Game;

public interface Interactable extends Serializable {
    public void interact(Player player, Game game);
    public boolean isInteractable(Player player);
}
