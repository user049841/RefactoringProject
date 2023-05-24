package dungeonmania.extensions;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PersistenceTest {
    @BeforeEach
    private void clearGames() {
        URL gamesPath = DungeonManiaController.class.getResource("/games");
        if (gamesPath != null) {
            File gameDirectory = new File(gamesPath.getPath());
            Arrays.asList(gameDirectory.listFiles()).forEach(File::delete);
        }
    }

    @Test
    @Tag("26-1")
    @DisplayName("Test that no games are returned when no games are saved")
    public void noSavedGames() {
        DungeonManiaController dmc = new DungeonManiaController();
        List<String> games = dmc.allGames();
        assertTrue(games.isEmpty());
    }

    @Test
    @Tag("26-2")
    @DisplayName("Test that one game is found when one game is saved")
    public void oneSavedGame() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_persistenceTest_oneSavedGame", "c_persistenceTest_oneSavedGame");

        assertDoesNotThrow(() -> dmc.saveGame("save"));
        assertEquals(List.of("save"), dmc.allGames());
    }

    @Test
    @Tag("26-3")
    @DisplayName("Test that multiple games are found when multiple games are saved")
    public void manySavedGames() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_persistenceTest_manySavedGames", "c_persistenceTest_manySavedGames");

        assertDoesNotThrow(() -> dmc.saveGame("save"));
        assertDoesNotThrow(() -> dmc.saveGame("save2"));
        assertDoesNotThrow(() -> dmc.saveGame("save3"));
        List<String> expected = List.of("save", "save2", "save3");
        assertTrue(expected.containsAll(dmc.allGames()) && dmc.allGames().containsAll(expected));
    }

    @Test
    @Tag("26-4")
    @DisplayName("Test that entity positions are restored")
    public void entityPositions() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_persistenceTest_entityPositions", "c_persistenceTest_entityPositions");

        res = dmc.tick(Direction.LEFT);
        Position zombieToastPosition = TestUtils.getEntityPos(res, "zombie_toast");
        assertDoesNotThrow(() -> dmc.saveGame("position"));

        DungeonManiaController dmcTwo = new DungeonManiaController();
        res = assertDoesNotThrow(() -> dmcTwo.loadGame("position"));
        assertEquals(new Position(0, 1), TestUtils.getPlayerPos(res));
        assertEquals(new Position(4, 1), TestUtils.getEntityPos(res, "mercenary"));
        assertEquals(new Position(6, 3), TestUtils.getEntityPos(res, "treasure"));
        assertEquals(new Position(9, 3), TestUtils.getEntityPos(res, "sword"));
        assertEquals(new Position(4, 4), TestUtils.getEntityPos(res, "wall"));
        assertEquals(new Position(4, 5), TestUtils.getEntityPos(res, "door"));
        assertEquals(new Position(5, 5), TestUtils.getEntityPos(res, "exit"));
        assertEquals(new Position(6, 5), TestUtils.getEntityPos(res, "switch"));
        assertEquals(new Position(7, 5), TestUtils.getEntityPos(res, "zombie_toast_spawner"));
        assertEquals(new Position(7, 8), TestUtils.getEntityPos(res, "spider"));
        assertEquals(zombieToastPosition, TestUtils.getEntityPos(res, "zombie_toast"));
        assertEquals(new Position(-1, 1), TestUtils.getEntityPos(res, "boulder"));
    }

    @Test
    @Tag("26-5")
    @DisplayName("Test that destroyed entities are not restored")
    public void destroyedEntities() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_persistenceTest_destroyedEntities",
                "c_persistenceTest_destroyedEntities");

        res = dmc.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> dmc.saveGame("destroy"));

        DungeonManiaController dmcTwo = new DungeonManiaController();
        res = assertDoesNotThrow(() -> dmcTwo.loadGame("destroy"));

        assertEquals(0, TestUtils.getEntities(res, "boulder").size());
        assertEquals(0, TestUtils.getEntities(res, "switch").size());
        assertEquals(0, TestUtils.getEntities(res, "bomb").size());
        assertEquals(0, TestUtils.getEntities(res, "sword").size());
        assertEquals(0, TestUtils.getEntities(res, "wall").size());
        assertEquals(0, TestUtils.getEntities(res, "door").size());
        assertEquals(0, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        assertEquals(0, TestUtils.getEntities(res, "spider").size());
        assertEquals(0, TestUtils.getEntities(res, "zombie_toast").size());
        assertEquals(0, TestUtils.getEntities(res, "mercenary").size());
        assertEquals(0, TestUtils.getEntities(res, "wood").size());
        assertEquals(new Position(2, 1), TestUtils.getPlayerPos(res));
        assertEquals(new Position(15, 15), TestUtils.getEntityPos(res, "exit"));
    }

    @Test
    @Tag("26-6")
    @DisplayName("Test that goals are restored")
    public void goals() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_persistenceTest_goals",
                "c_persistenceTest_goals");

        assertTrue(TestUtils.getGoals(res).contains(":treasure"));
        assertTrue(TestUtils.getGoals(res).contains(":exit"));

        // collect treasure
        res = dmc.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> dmc.saveGame("goals"));
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));
        assertTrue(TestUtils.getGoals(res).contains(":exit"));

        // restore game
        DungeonManiaController dmcTwo = new DungeonManiaController();
        res = assertDoesNotThrow(() -> dmcTwo.loadGame("goals"));

        // collect treasure
        res = dmcTwo.tick(Direction.RIGHT);
        assertFalse(TestUtils.getGoals(res).contains(":treasure"));
        assertTrue(TestUtils.getGoals(res).contains(":exit"));

        // move to exit
        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("26-7")
    @DisplayName("Test player health is restored")
    public void playerHealth() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_persistenceTest_playerHealth",
                "c_persistenceTest_playerHealth");

        // fight mercenary
        res = dmc.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> dmc.saveGame("health"));

        // restore game
        DungeonManiaController dmcTwo = new DungeonManiaController();
        res = assertDoesNotThrow(() -> dmcTwo.loadGame("health"));
        assertEquals(1, TestUtils.getEntities(res, "player").size());

        // fight mercenary
        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getEntities(res, "player").size());
    }

    @Test
    @Tag("26-8")
    @DisplayName("Test open doors are restored")
    public void openDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_persistenceTest_openDoor",
                "c_persistenceTest_openDoor");

        // pick up key
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(2, 1), TestUtils.getPlayerPos(res));

        // open door
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(3, 1), TestUtils.getPlayerPos(res));
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(2, 1), TestUtils.getPlayerPos(res));
        assertDoesNotThrow(() -> dmc.saveGame("door"));

        // restore game
        DungeonManiaController dmcTwo = new DungeonManiaController();
        res = assertDoesNotThrow(() -> dmcTwo.loadGame("door"));
        assertEquals(new Position(2, 1), TestUtils.getPlayerPos(res));

        // move onto open door
        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(new Position(3, 1), TestUtils.getPlayerPos(res));
        res = dmcTwo.tick(Direction.LEFT);
        assertEquals(new Position(2, 1), TestUtils.getPlayerPos(res));

        res = dmcTwo.tick(Direction.DOWN);
        assertEquals(new Position(2, 1), TestUtils.getPlayerPos(res));
    }

    @Test
    @Tag("26-9")
    @DisplayName("Test the key matching the door is the same when loaded")
    public void matchingDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_persistenceTest_matchingDoor",
                "c_persistenceTest_matchingDoor");

        // pick up key
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(2, 1), TestUtils.getPlayerPos(res));
        assertDoesNotThrow(() -> dmc.saveGame("match"));

        // restore game
        DungeonManiaController dmcTwo = new DungeonManiaController();
        res = assertDoesNotThrow(() -> dmcTwo.loadGame("match"));
        assertEquals(new Position(2, 1), TestUtils.getPlayerPos(res));

        // closed door
        res = dmcTwo.tick(Direction.DOWN);
        assertEquals(new Position(2, 1), TestUtils.getPlayerPos(res));

        // open door
        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(new Position(3, 1), TestUtils.getPlayerPos(res));
        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(new Position(4, 1), TestUtils.getPlayerPos(res));
        res = dmcTwo.tick(Direction.LEFT);
        res = dmcTwo.tick(Direction.LEFT);

        // open other door
        res = dmcTwo.tick(Direction.DOWN);
        assertEquals(new Position(2, 2), TestUtils.getPlayerPos(res));
    }

    @Test
    @Tag("26-10")
    @DisplayName("Test inventory is restored")
    public void inventory() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_persistenceTest_inventory",
                "c_persistenceTest_inventory");

        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(0, TestUtils.getInventory(res, "arrow").size());

        // pick up inventory items
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "wood").size());
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(0, TestUtils.getInventory(res, "arrow").size());
        assertDoesNotThrow(() -> dmc.saveGame("inv"));

        // restore game
        DungeonManiaController dmcTwo = new DungeonManiaController();
        res = assertDoesNotThrow(() -> dmcTwo.loadGame("inv"));
        assertEquals(2, TestUtils.getInventory(res, "wood").size());
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(0, TestUtils.getInventory(res, "arrow").size());

        // pick up inventory items
        res = dmcTwo.tick(Direction.RIGHT);
        res = dmcTwo.tick(Direction.RIGHT);
        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(3, TestUtils.getInventory(res, "wood").size());
        assertEquals(2, TestUtils.getInventory(res, "treasure").size());
        assertEquals(1, TestUtils.getInventory(res, "arrow").size());
        assertDoesNotThrow(() -> dmcTwo.saveGame("inv2"));

        // restore game
        res = assertDoesNotThrow(() -> dmc.loadGame("inv2"));
        assertEquals(3, TestUtils.getInventory(res, "wood").size());
        assertEquals(2, TestUtils.getInventory(res, "treasure").size());
        assertEquals(1, TestUtils.getInventory(res, "arrow").size());
    }

    @Test
    @Tag("26-11")
    @DisplayName("Test portals have the same correspondence when loaded")
    public void portals() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_persistenceTest_portals",
                "c_persistenceTest_portals");

        // move to portal
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 1), TestUtils.getPlayerPos(res));
        assertDoesNotThrow(() -> dmc.saveGame("portal"));

        // restore game
        DungeonManiaController dmcTwo = new DungeonManiaController();
        res = assertDoesNotThrow(() -> dmcTwo.loadGame("portal"));

        // move to portal
        assertEquals(new Position(5, 1), TestUtils.getPlayerPos(res));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 5), TestUtils.getPlayerPos(res));
    }

    @Test
    @Tag("26-12")
    @DisplayName("Test zombie toast spawner spawns at the correct tick when loaded")
    public void spawner() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_persistenceTest_spawner",
                "c_persistenceTest_spawner");

        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        assertEquals(0, TestUtils.getEntities(res, "zombie_toast").size());
        assertDoesNotThrow(() -> dmc.saveGame("spawner"));

        // restore game
        DungeonManiaController dmcTwo = new DungeonManiaController();
        res = assertDoesNotThrow(() -> dmcTwo.loadGame("spawner"));

        // spawn zombie
        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast").size());
    }

    @Test
    @Tag("26-13")
    @DisplayName("Test spider moves in the correct direction when loaded")
    public void spiderMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_persistenceTest_spiderMovement",
                "c_persistenceTest_spiderMovement");

        assertEquals(new Position(5, 6), TestUtils.getEntityPos(res, "spider"));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "spider").size());
        assertEquals(new Position(5, 5), TestUtils.getEntityPos(res, "spider"));
        assertDoesNotThrow(() -> dmc.saveGame("spider"));

        // restore game
        DungeonManiaController dmcTwo = new DungeonManiaController();
        res = assertDoesNotThrow(() -> dmcTwo.loadGame("spider"));

        assertEquals(new Position(5, 5), TestUtils.getEntityPos(res, "spider"));
        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(new Position(6, 5), TestUtils.getEntityPos(res, "spider"));
        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(new Position(6, 6), TestUtils.getEntityPos(res, "spider"));
        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(new Position(6, 7), TestUtils.getEntityPos(res, "spider"));
        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(new Position(5, 7), TestUtils.getEntityPos(res, "spider"));
        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(new Position(4, 7), TestUtils.getEntityPos(res, "spider"));
        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(new Position(4, 6), TestUtils.getEntityPos(res, "spider"));
        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(new Position(4, 5), TestUtils.getEntityPos(res, "spider"));
        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(new Position(5, 5), TestUtils.getEntityPos(res, "spider"));
    }

    @Test
    @Tag("26-14")
    @DisplayName("Test invincibility potion effects are restored")
    public void invincibilityPotion() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_persistenceTest_invincibilityPotion",
                "c_persistenceTest_invincibilityPotion");

        // pick up potion
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "invincibility_potion").size());

        // consume potion
        String potionId = TestUtils.getFirstItemId(res, "invincibility_potion");
        res = assertDoesNotThrow(() -> dmc.tick(potionId));
        assertEquals(0, TestUtils.getInventory(res, "invincibility_potion").size());
        res = dmc.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> dmc.saveGame("invincible"));

        // restore game
        DungeonManiaController dmcTwo = new DungeonManiaController();
        res = assertDoesNotThrow(() -> dmcTwo.loadGame("invincible"));

        // fight mercenary
        assertEquals(0, TestUtils.getInventory(res, "invincibility_potion").size());
        assertEquals(2, TestUtils.getEntities(res, "mercenary").size());
        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "mercenary").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());
        assertEquals(1, res.getBattles().size());
        assertEquals(1, res.getBattles().get(0).getRounds().size());

        // fight mercenary without potion effects
        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "mercenary").size());
        assertEquals(0, TestUtils.getEntities(res, "player").size());
        assertEquals(2, res.getBattles().size());
    }

    @Test
    @Tag("26-15")
    @DisplayName("Test invisibility potion effects are restored")
    public void invisibilityPotion() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_persistenceTest_invisibilityPotion",
                "c_persistenceTest_invisibilityPotion");

        // pick up potion
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "invisibility_potion").size());

        // consume potion
        String potionId = TestUtils.getFirstItemId(res, "invisibility_potion");
        res = assertDoesNotThrow(() -> dmc.tick(potionId));
        assertEquals(0, TestUtils.getInventory(res, "invisibility_potion").size());
        res = dmc.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> dmc.saveGame("invisible"));

        // restore game
        DungeonManiaController dmcTwo = new DungeonManiaController();
        res = assertDoesNotThrow(() -> dmcTwo.loadGame("invisible"));

        // do not fight mercenary
        assertEquals(0, TestUtils.getInventory(res, "invisibility_potion").size());
        assertEquals(2, TestUtils.getEntities(res, "mercenary").size());
        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "player").size());
        assertEquals(0, res.getBattles().size());

        // fight mercenary without potion effects
        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getEntities(res, "player").size());
    }

    @Test
    @Tag("26-16")
    @DisplayName("Test potion queue is restored")
    public void potionQueue() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_potionsTest_potionQueuing", "c_potionsTest_potionQueuing");

        assertEquals(1, TestUtils.getEntities(res, "invincibility_potion").size());
        assertEquals(1, TestUtils.getEntities(res, "invisibility_potion").size());
        assertEquals(1, TestUtils.getEntities(res, "spider").size());

        // buffer
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);

        // pick up invincibility potion
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getEntities(res, "invincibility_potion").size());
        assertEquals(1, TestUtils.getInventory(res, "invincibility_potion").size());

        // pick up invisibility potion
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getEntities(res, "invisibility_potion").size());
        assertEquals(1, TestUtils.getInventory(res, "invisibility_potion").size());

        // consume invisibility potion (invisibility has duration 3)
        String invisibilityPotionId = TestUtils.getFirstItemId(res, "invisibility_potion");
        res = assertDoesNotThrow(() -> dmc.tick(invisibilityPotionId));
        assertEquals(0, TestUtils.getEntities(res, "invisibility_potion").size());

        // consume invincibility potion (invisibility has duration 2)
        String invincibilityPotionId = TestUtils.getFirstItemId(res, "invincibility_potion");
        res = assertDoesNotThrow(() -> dmc.tick(invincibilityPotionId));
        assertEquals(0, TestUtils.getInventory(res, "invincibility_potion").size());
        assertDoesNotThrow(() -> dmc.saveGame("potion"));

        // restore game
        DungeonManiaController dmcTwo = new DungeonManiaController();
        res = assertDoesNotThrow(() -> dmcTwo.loadGame("potion"));

        // meet spider, but a battle does not occur (invisibility has duration 1)
        res = dmcTwo.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getEntities(res, "spider").size());
        assertEquals(0, res.getBattles().size());

        // meet spider again, battle does occur but won immediately
        // (invisibility has duration 0, invincibility in effect)
        res = dmcTwo.tick(Direction.UP);
        assertEquals(0, TestUtils.getEntities(res, "spider").size());
        assertEquals(1, res.getBattles().size());
        assertEquals(1, res.getBattles().get(0).getRounds().size());
    }

    @Test
    @Tag("26-17")
    @DisplayName("Test if bomb can be picked up when restored")
    public void bomb() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_persistenceTest_bomb",
                "c_persistenceTest_bomb");

        // pick up bomb
        assertEquals(0, TestUtils.getInventory(res, "bomb").size());
        assertEquals(1, TestUtils.getEntities(res, "bomb").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "bomb").size());
        assertEquals(0, TestUtils.getEntities(res, "bomb").size());

        // place bomb
        String bombId = TestUtils.getFirstItemId(res, "bomb");
        res = assertDoesNotThrow(() -> dmc.tick(bombId));
        assertEquals(0, TestUtils.getInventory(res, "bomb").size());
        assertEquals(1, TestUtils.getEntities(res, "bomb").size());
        res = dmc.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> dmc.saveGame("bomb"));

        // restore game
        DungeonManiaController dmcTwo = new DungeonManiaController();
        res = assertDoesNotThrow(() -> dmcTwo.loadGame("bomb"));

        // cannot pick up bomb
        assertEquals(0, TestUtils.getInventory(res, "bomb").size());
        assertEquals(1, TestUtils.getEntities(res, "bomb").size());
        res = dmcTwo.tick(Direction.LEFT);
        assertEquals(0, TestUtils.getInventory(res, "bomb").size());
        assertEquals(1, TestUtils.getEntities(res, "bomb").size());
    }

    @Test
    @Tag("26-18")
    @DisplayName("Test whether sword durability is restored")
    public void sword() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_persistenceTest_sword",
                "c_persistenceTest_sword");

        // pick up sword
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(1, TestUtils.getEntities(res, "sword").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getEntities(res, "sword").size());

        // use sword
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        assertDoesNotThrow(() -> dmc.saveGame("sword"));

        // restore game
        DungeonManiaController dmcTwo = new DungeonManiaController();
        res = assertDoesNotThrow(() -> dmcTwo.loadGame("sword"));

        // use sword
        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
    }

    @Test
    @Tag("26-19")
    @DisplayName("Test whether bow durability is restored")
    public void bow() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_persistenceTest_bow",
                "c_persistenceTest_bow");

        // build bow
        assertEquals(0, TestUtils.getInventory(res, "bow").size());
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = assertDoesNotThrow(() -> dmc.build("bow"));
        assertEquals(1, TestUtils.getInventory(res, "bow").size());

        // use bow
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "bow").size());
        assertDoesNotThrow(() -> dmc.saveGame("bow"));

        // restore game
        DungeonManiaController dmcTwo = new DungeonManiaController();
        res = assertDoesNotThrow(() -> dmcTwo.loadGame("bow"));

        // use bow
        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getInventory(res, "bow").size());
    }

    @Test
    @Tag("26-20")
    @DisplayName("Test battles are restored")
    public void battles() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_persistenceTest_battles",
                "c_persistenceTest_battles");

        // battle
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, res.getBattles().size());
        assertDoesNotThrow(() -> dmc.saveGame("battles"));

        // restore game
        DungeonManiaController dmcTwo = new DungeonManiaController();
        res = assertDoesNotThrow(() -> dmcTwo.loadGame("battles"));

        // battle
        assertEquals(1, res.getBattles().size());
        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(2, res.getBattles().size());
    }

    @Test
    @Tag("26-21")
    @DisplayName("Test allies are restored")
    public void allies() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_persistenceTest_allies",
                "c_persistenceTest_allies");

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // bribe mercenary
        String mercenaryId = TestUtils.getEntities(res, "mercenary").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.interact(mercenaryId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertDoesNotThrow(() -> dmc.saveGame("allies"));

        // restore game
        DungeonManiaController dmcTwo = new DungeonManiaController();
        res = assertDoesNotThrow(() -> dmcTwo.loadGame("allies"));

        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());
        assertEquals(1, TestUtils.getEntities(res, "mercenary").size());

        // battle
        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(1, res.getBattles().size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());
        assertEquals(1, TestUtils.getEntities(res, "mercenary").size());
        assertEquals(0, TestUtils.getEntities(res, "spider").size());
    }

    @Test
    @Tag("26-22")
    @DisplayName("Test mind control and duration of mind control is restored")
    public void mindControl() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_persistenceTest_mindControl",
                "c_persistenceTest_mindControl");

        // build sceptre
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "wood").size());
        assertEquals(1, TestUtils.getInventory(res, "key").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // mind control
        String mercenaryId = TestUtils.getEntities(res, "mercenary").get(0).getId();
        res = assertDoesNotThrow(() -> dmc.interact(mercenaryId));

        // does not fight mercenary
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());
        assertDoesNotThrow(() -> dmc.saveGame("mind"));

        // restore game
        DungeonManiaController dmcTwo = new DungeonManiaController();
        res = assertDoesNotThrow(() -> dmcTwo.loadGame("mind"));

        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());
        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());
        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());
        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());

        // battle
        res = dmcTwo.tick(Direction.RIGHT);
        assertEquals(1, res.getBattles().size());
        assertEquals(0, TestUtils.getEntities(res, "player").size());
        assertEquals(1, TestUtils.getEntities(res, "mercenary").size());
    }

    @Test
    @Tag("26-23")
    @DisplayName("Test swamp and duration of swamp is restored")
    public void swamp() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_persistenceTest_swamp",
                "c_persistenceTest_swamp");

        // move onto swamp
        assertEquals(new Position(2, 1), TestUtils.getEntityPos(res, "swamp"));
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(2, 1), TestUtils.getEntityPos(res, "mercenary"));
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(2, 1), TestUtils.getEntityPos(res, "mercenary"));
        assertDoesNotThrow(() -> dmc.saveGame("swamp"));

        // restore game
        DungeonManiaController dmcTwo = new DungeonManiaController();
        res = assertDoesNotThrow(() -> dmcTwo.loadGame("swamp"));

        assertEquals(new Position(2, 1), TestUtils.getEntityPos(res, "swamp"));
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(2, 1), TestUtils.getEntityPos(res, "mercenary"));
        res = dmc.tick(Direction.LEFT);
        assertEquals(new Position(1, 1), TestUtils.getEntityPos(res, "mercenary"));
    }

    @Test
    @Tag("26-24")
    @DisplayName("Test loading a game that does not exist")
    public void invalidGame() {
        DungeonManiaController dmc = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> dmc.loadGame("invalid"));
    }

    @Test
    @Tag("26-25")
    @DisplayName("Test time turner is restored")
    public void timeTurner() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_persistenceTest_timeTurner",
                "c_persistenceTest_timeTurner");

        assertEquals(0, TestUtils.getInventory(res, "time_turner").size());

        // pick up inventory items
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "time_turner").size());
        assertDoesNotThrow(() -> dmc.saveGame("timeTurner"));

        // restore game
        DungeonManiaController dmcTwo = new DungeonManiaController();
        res = assertDoesNotThrow(() -> dmcTwo.loadGame("timeTurner"));
        assertEquals(1, TestUtils.getInventory(res, "time_turner").size());
    }

    @Test
    @Tag("26-26")
    @DisplayName("Test time travel is restored")
    public void timeTravel() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_persistenceTest_timeTravel",
                "c_persistenceTest_timeTravel");

        // time travel
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(1, 1), TestUtils.getEntityPos(res, "older_player"));
        assertDoesNotThrow(() -> dmc.saveGame("timeTravel"));

        // restore game
        DungeonManiaController dmcTwo = new DungeonManiaController();
        res = assertDoesNotThrow(() -> dmcTwo.loadGame("timeTravel"));

        assertEquals(new Position(1, 1), TestUtils.getEntityPos(res, "older_player"));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(2, 1), TestUtils.getEntityPos(res, "older_player"));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(3, 1), TestUtils.getEntityPos(res, "older_player"));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getEntities(res, "older_player").size());
    }
}
