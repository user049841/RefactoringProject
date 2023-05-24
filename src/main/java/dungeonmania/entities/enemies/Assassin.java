package dungeonmania.entities.enemies;

import java.util.Random;

import dungeonmania.Game;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.util.Position;

public class Assassin extends Mercenary {
    public static final int DEFAULT_BRIBE_AMOUNT = Mercenary.DEFAULT_BRIBE_AMOUNT;
    public static final int DEFAULT_BRIBE_RADIUS = Mercenary.DEFAULT_BRIBE_RADIUS;
    public static final double DEFAULT_ATTACK = 3 * Mercenary.DEFAULT_ATTACK;
    public static final double DEFAULT_HEALTH = Mercenary.DEFAULT_HEALTH;
    public static final double DEFAULT_BRIBE_FAIL_RATE = 0.5;
    public static final double DEFAULT_ALLY_ATTACK = Mercenary.DEFAULT_ALLY_ATTACK;
    public static final double DEFAULT_ALLY_DEFENCE = Mercenary.DEFAULT_ALLY_DEFENCE;

    private Random randGen = new Random();
    private double bribeFailRate;

    public Assassin(Position position, double health, double attack, int bribeAmount, int bribeRadius,
            double bribeFailRate, double allyAttack, double allyDefence) {
        super(position, health, attack, bribeAmount, bribeRadius, allyAttack, allyDefence);
        this.bribeFailRate = bribeFailRate;
    }

    @Override
    public void interact(Player player, Game game) {
        if (randGen.nextDouble() > bribeFailRate || player.countEntityOfType(Sceptre.class) > 0) {
            super.interact(player, game);
        } else {
            bribe(player);
        }
    }
}
