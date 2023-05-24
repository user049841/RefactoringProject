package dungeonmania.goals;

import dungeonmania.Game;

public class TreasureGoal implements Goal {
    private int target;

    public TreasureGoal(int target) {
        this.target = target;
    }

    public boolean achieved(Game game) {
        return game.getTreasureCollectedCount() >= target;
    }

    public String toString(Game game) {
        if (achieved(game)) return "";
        return ":treasure";
    }
}
