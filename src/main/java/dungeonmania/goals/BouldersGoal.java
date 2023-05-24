package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.entities.Switch;

public class BouldersGoal implements Goal {
    public boolean achieved(Game game) {
        return game.getEntities(Switch.class).stream().allMatch(Switch::isActivated);
    }

    public String toString(Game game) {
        if (achieved(game)) return "";
        return ":boulders";
    }
}
