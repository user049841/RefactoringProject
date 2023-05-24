package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.enemies.movements.FollowPlayerMovement;
import dungeonmania.entities.enemies.movements.PreviousSquareMovement;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Mercenary extends Enemy implements Interactable {
    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 10.0;
    public static final double DEFAULT_ALLY_ATTACK = 3;
    public static final double DEFAULT_ALLY_DEFENCE = 3;

    private int bribeAmount = Mercenary.DEFAULT_BRIBE_AMOUNT;
    private int bribeRadius = Mercenary.DEFAULT_BRIBE_RADIUS;
    private double allyAttack = Mercenary.DEFAULT_ALLY_ATTACK;
    private double allyDefence = Mercenary.DEFAULT_ALLY_DEFENCE;
    private boolean allied = false;
    private boolean isMindControlled = false;
    private int allyDuration = 0;

    public Mercenary(Position position, double health, double attack, int bribeAmount, int bribeRadius,
            double allyAttack, double allyDefence) {
        super(position, health, attack);
        setMovement(new FollowPlayerMovement());
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
        this.allyAttack = allyAttack;
        this.allyDefence = allyDefence;
    }

    public boolean isAllied() {
        return allied;
    }

    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(
            0,
            allyAttack,
            allyDefence,
            1,
            1));
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (allied) return;
        super.onOverlap(map, entity);
    }

    @Override
    public void move(Game game) {
        changeAdjacentAllyMovement(game.getPlayer());
        super.move(game);
        changeAdjacentAllyMovement(game.getPlayer());
        if (allyDuration > 0 && isMindControlled) allyDuration--;
        if (allyDuration == 0 && isMindControlled) {
            isMindControlled = false;
            allied = false;
            setMovement(new FollowPlayerMovement());
        }
    }

    private void changeAdjacentAllyMovement(Player player) {
        if (isAllied() && getCardinallyAdjacentPositions().contains(player.getPosition())) {
            setMovement(new PreviousSquareMovement());
        }
    }

    /**
     * check whether the current merc can be bribed
     * @param player
     * @return
     */
    private boolean canBeBribed(Player player) {
        return isInBribeRadius(player) && player.countEntityOfType(Treasure.class) >= bribeAmount;
    }

    private boolean isInBribeRadius(Player player) {
        return Math.abs(player.getXPosition() - this.getXPosition()) <= bribeRadius
                && Math.abs(player.getYPosition() - this.getYPosition()) <= bribeRadius;
    }

    /**
     * bribe the merc
     */
    protected void bribe(Player player) {
        for (int i = 0; i < bribeAmount; i++) {
            player.use(Treasure.class);
        }
    }

    @Override
    public void interact(Player player, Game game) {
        allied = true;
        if (player.countEntityOfType(Sceptre.class) > 0) {
            allyDuration = player.getItems(Sceptre.class).get(0).getMindControlDuration();
            isMindControlled = true;
        } else {
            bribe(player);
        }
    }

    @Override
    public boolean isInteractable(Player player) {
        return !allied && (canBeBribed(player) || player.countEntityOfType(Sceptre.class) > 0);
    }
}
