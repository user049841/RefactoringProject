package dungeonmania.entities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.json.JSONObject;

import dungeonmania.Game;
import dungeonmania.GameState;
import dungeonmania.entities.buildables.*;
import dungeonmania.entities.collectables.*;
import dungeonmania.entities.enemies.*;
import dungeonmania.entities.logic.AndRule;
import dungeonmania.entities.logic.CoAndRule;
import dungeonmania.entities.logic.LightBulb;
import dungeonmania.entities.logic.LogicRule;
import dungeonmania.entities.logic.OrRule;
import dungeonmania.entities.logic.SwitchDoor;
import dungeonmania.entities.logic.Wire;
import dungeonmania.entities.logic.XorRule;
import dungeonmania.map.GameMap;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.util.Position;

public class EntityFactory implements Serializable {
    private transient JSONObject config;
    private Random ranGen = new Random();

    public EntityFactory(JSONObject config) {
        this.config = config;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(config.toString());
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        config = new JSONObject((String) in.readObject());
        in.defaultReadObject();
    }

    public Entity createEntity(JSONObject jsonEntity) {
        return constructEntity(jsonEntity, config);
    }

    public Entity createOlderPlayer(Game game, List<GameState> gameStates, int startTick, int endTick) {
        Position position = gameStates.get(startTick).getPlayer().getPosition();
        return new OlderPlayer(position, game, gameStates, endTick);
    }

    public void spawnSpider(Game game) {
        GameMap map = game.getMap();
        int tick = game.getCurrentTick();
        int rate = config.optInt("spider_spawn_interval", Spider.DEFAULT_SPAWN_INTERVAL);
        if (rate == 0 || (tick + 1) % rate != 0)
            return;

        List<Position> availablePos = calculateAvailableSpiderPositions(game.getPlayer(), map);
        Position initPosition = availablePos.get(ranGen.nextInt(availablePos.size()));
        Spider spider = buildSpider(initPosition);
        map.moveTo(spider, initPosition);
        game.register(() -> spider.move(game), Game.AI_MOVEMENT, spider.getId());
    }

    private List<Position> calculateAvailableSpiderPositions(Player player, GameMap map) {
        List<Position> availablePos = new ArrayList<>();
        int radius = Spider.SPAWN_RADIUS;
        Spider dummySpider = buildSpider(new Position(0, 0)); // for checking possible positions
        for (int i = player.getXPosition() - radius; i < player.getXPosition() + radius; i++) {
            for (int j = player.getYPosition() - radius; j < player.getYPosition() + radius; j++) {
                if (Position.calculatePositionBetween(player.getPosition(), new Position(i, j)).magnitude() > radius)
                    continue;
                Position np = new Position(i, j);
                boolean spawnablePosition = map.getEntities(np).stream()
                        .noneMatch(e -> e instanceof Player || e instanceof Boulder || e instanceof Enemy);
                if (map.canMoveTo(dummySpider, np) && spawnablePosition)
                    availablePos.add(np);
            }
        }
        return availablePos;
    }

    public void spawnZombie(Game game, ZombieToastSpawner spawner) {
        GameMap map = game.getMap();
        int tick = game.getCurrentTick();
        Random randGen = new Random();
        int spawnInterval = config.optInt("zombie_spawn_interval", ZombieToastSpawner.DEFAULT_SPAWN_INTERVAL);
        if (spawnInterval == 0 || (tick + 1) % spawnInterval != 0)
            return;
        List<Position> pos = spawner.getCardinallyAdjacentPositions();
        pos = pos
                .stream()
                .filter(p -> !game.getEntities(p).stream().anyMatch(e -> (e instanceof Wall)))
                .collect(Collectors.toList());
        if (pos.size() == 0)
            return;
        ZombieToast zt = buildZombieToast(pos.get(randGen.nextInt(pos.size())));
        map.moveTo(zt, zt.getPosition());
        game.register(() -> zt.move(game), Game.AI_MOVEMENT, zt.getId());
    }

    private Spider buildSpider(Position pos) {
        double spiderHealth = config.optDouble("spider_health", Spider.DEFAULT_HEALTH);
        double spiderAttack = config.optDouble("spider_attack", Spider.DEFAULT_ATTACK);
        return new Spider(pos, spiderHealth, spiderAttack);
    }

    private Player buildPlayer(Position pos) {
        double playerHealth = config.optDouble("player_health", Player.DEFAULT_HEALTH);
        double playerAttack = config.optDouble("player_attack", Player.DEFAULT_ATTACK);
        return new Player(pos, playerHealth, playerAttack);
    }

    private ZombieToast buildZombieToast(Position pos) {
        double zombieHealth = config.optDouble("zombie_health", ZombieToast.DEFAULT_HEALTH);
        double zombieAttack = config.optDouble("zombie_attack", ZombieToast.DEFAULT_ATTACK);
        return new ZombieToast(pos, zombieHealth, zombieAttack);
    }

    private ZombieToastSpawner buildZombieToastSpawner(Position pos) {
        int zombieSpawnRate = config.optInt("zombie_spawn_interval", ZombieToastSpawner.DEFAULT_SPAWN_INTERVAL);
        return new ZombieToastSpawner(pos, zombieSpawnRate);
    }

    private Mercenary buildMercenary(Position pos) {
        double mercenaryHealth = config.optDouble("mercenary_health", Mercenary.DEFAULT_HEALTH);
        double mercenaryAttack = config.optDouble("mercenary_attack", Mercenary.DEFAULT_ATTACK);
        double mercenaryAllyAttack = config.optDouble("ally_attack", Mercenary.DEFAULT_ALLY_ATTACK);
        double mercenaryAllyDefence = config.optDouble("ally_defence", Mercenary.DEFAULT_ALLY_DEFENCE);
        int mercenaryBribeAmount = config.optInt("bribe_amount", Mercenary.DEFAULT_BRIBE_AMOUNT);
        int mercenaryBribeRadius = config.optInt("bribe_radius", Mercenary.DEFAULT_BRIBE_RADIUS);
        return new Mercenary(pos, mercenaryHealth, mercenaryAttack, mercenaryBribeAmount, mercenaryBribeRadius,
                mercenaryAllyAttack, mercenaryAllyDefence);
    }

    private Assassin buildAssassin(Position pos) {
        double assassinHealth = config.optDouble("assassin_health", Assassin.DEFAULT_HEALTH);
        double assassinAttack = config.optDouble("assassin_attack", Assassin.DEFAULT_ATTACK);
        double assassinAllyAttack = config.optDouble("ally_attack", Assassin.DEFAULT_ALLY_ATTACK);
        double assassinAllyDefence = config.optDouble("ally_defence", Assassin.DEFAULT_ALLY_DEFENCE);
        int assassinBribeAmount = config.optInt("assassin_bribe_amount", Assassin.DEFAULT_BRIBE_AMOUNT);
        int assassinBribeRadius = config.optInt("bribe_radius", Assassin.DEFAULT_BRIBE_RADIUS);
        double assassinBribeFailRate = config.optDouble("assassin_bribe_fail_rate",
                Assassin.DEFAULT_BRIBE_FAIL_RATE);
        return new Assassin(pos, assassinHealth, assassinAttack, assassinBribeAmount, assassinBribeRadius,
                assassinBribeFailRate, assassinAllyAttack, assassinAllyDefence);
    }

    private Bomb buildBomb(Position pos) {
        int bombRadius = config.optInt("bomb_radius", Bomb.DEFAULT_RADIUS);
        return new Bomb(pos, bombRadius);
    }

    private InvisibilityPotion buildInvisibilityPotion(Position pos) {
        int invisibilityPotionDuration = config.optInt(
            "invisibility_potion_duration",
            InvisibilityPotion.DEFAULT_DURATION);
        return new InvisibilityPotion(pos, invisibilityPotionDuration);
    }

    private InvincibilityPotion buildInvincibilityPotion(Position pos) {
        int invincibilityPotionDuration = config.optInt(
            "invincibility_potion_duration",
            InvincibilityPotion.DEFAULT_DURATION);
        return new InvincibilityPotion(pos, invincibilityPotionDuration);
    }

    private LogicRule createLogicRule(String logicRule) {
        switch (logicRule) {
            case "and":
                return new AndRule();
            case "or":
                return new OrRule();
            case "xor":
                return new XorRule();
            case "co_and":
                return new CoAndRule();
            default:
                return null;
        }
    }

    private Sword buildSword(Position pos) {
        double swordAttack = config.optDouble("sword_attack", Sword.DEFAULT_ATTACK);
        int swordDurability = config.optInt("sword_durability", Sword.DEFAULT_DURABILITY);
        return new Sword(pos, swordAttack, swordDurability);
    }

    private Bow buildBow() {
        int bowDurability = config.optInt("bow_durability");
        return new Bow(bowDurability);
    }

    private Shield buildShield() {
        int shieldDurability = config.optInt("shield_durability");
        double shieldDefence = config.optInt("shield_defence");
        return new Shield(shieldDurability, shieldDefence);
    }

    private Sceptre buildSceptre() {
        int mindControlDuration = config.optInt("mind_control_duration", Sceptre.DEFAULT_DURATION);
        return new Sceptre(mindControlDuration);
    }

    private MidnightArmour buildMidnightArmour() {
        double armourAttack = config.optDouble("midnight_armour_attack", MidnightArmour.DEFAULT_ATTACK);
        double armourDefence = config.optDouble("midnight_armour_defence", MidnightArmour.DEFAULT_DEFENCE);
        return new MidnightArmour(armourAttack, armourDefence);
    }

    public Buildable createBuildable(String buildable) {
        switch (buildable) {
        case "bow":
            return buildBow();
        case "shield":
            return buildShield();
        case "sceptre":
            return buildSceptre();
        case "midnight_armour":
            return buildMidnightArmour();
        default:
            return null;
        }
    }

    private Entity constructEntity(JSONObject jsonEntity, JSONObject config) {
        Position pos = new Position(jsonEntity.getInt("x"), jsonEntity.getInt("y"));

        switch (jsonEntity.getString("type")) {
        case "player":
            return buildPlayer(pos);
        case "zombie_toast":
            return buildZombieToast(pos);
        case "zombie_toast_spawner":
            return buildZombieToastSpawner(pos);
        case "mercenary":
            return buildMercenary(pos);
        case "assassin":
            return buildAssassin(pos);
        case "wall":
            return new Wall(pos);
        case "boulder":
            return new Boulder(pos);
        case "switch":
            return new Switch(pos);
        case "time_travelling_portal":
            return new TimeTravellingPortal(pos);
        case "exit":
            return new Exit(pos);
        case "treasure":
            return new Treasure(pos);
        case "wood":
            return new Wood(pos);
        case "arrow":
            return new Arrow(pos);
        case "time_turner":
            return new TimeTurner(pos);
        case "bomb":
            return buildBomb(pos);
        case "invisibility_potion":
            return buildInvisibilityPotion(pos);
        case "invincibility_potion":
            return buildInvincibilityPotion(pos);
        case "portal":
            return new Portal(pos, ColorCodedType.valueOf(jsonEntity.getString("colour")));
        case "sword":
            return buildSword(pos);
        case "spider":
            return buildSpider(pos);
        case "door":
            return new Door(pos, jsonEntity.getInt("key"));
        case "key":
            return new Key(pos, jsonEntity.getInt("key"));
        case "sun_stone":
            return new SunStone(pos);
        case "swamp_tile":
            return new SwampTile(pos, jsonEntity.optInt("movement_factor", SwampTile.DEFAULT_MOVEMENT_FACTOR));
        case "wire":
            return new Wire(pos);
        case "light_bulb_off":
            return new LightBulb(pos, createLogicRule(jsonEntity.getString("logic")));
        case "switch_door":
            return new SwitchDoor(pos, createLogicRule(jsonEntity.getString("logic")));
        default:
            return null;
        }
    }
}
