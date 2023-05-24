package dungeonmania.goals;

import dungeonmania.entities.Exit;
import dungeonmania.Game;

public class ExitGoal implements Goal {
    public boolean achieved(Game game) {
        return game.getEntities(Exit.class).stream().anyMatch(Exit::hasReached);
    }

    public String toString(Game game) {
        if (achieved(game)) return "";
        return ":exit";
    }
}
