package dungeonmania.extensions;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EnemiesGoalTest {

    @Test
    @Tag("16-1")
    @DisplayName("Test achieving a basic enemies goal by winning battle")
    public void enemiesBattleOnly() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_EnemiesGoalTest_enemiesBattleOnly",
                "c_EnemiesGoalTest_enemiesBattleOnly");

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // move player to enemy
        res = dmc.tick(Direction.RIGHT);

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("16-2")
    @DisplayName("Test achieving a basic enemies goal by bomb")
    public void enemiesBombOnly() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_EnemiesGoalTest_enemiesBombOnly",
                "c_EnemiesGoalTest_enemiesBombOnly");

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // activate bomb
        res = dmc.tick(Direction.RIGHT);

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("16-3")
    @DisplayName("Test achieving an enemies goal by destroying all spawners")
    public void spawnersOnly()  {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_EnemiesGoalTest_spawnersOnly", "c_EnemiesGoalTest_spawnersOnly");

        assertEquals(2, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        String spawnerIdOne = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();
        String spawnerIdTwo = TestUtils.getEntities(res, "zombie_toast_spawner").get(1).getId();

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // pick up sword
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // destroy spawner
        res = assertDoesNotThrow(() -> dmc.interact(spawnerIdOne));
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // destroy last spawner
        res = assertDoesNotThrow(() -> dmc.interact(spawnerIdTwo));
        assertEquals(0, TestUtils.getEntities(res, "zombie_toast_spawner").size());

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("16-4")
    @DisplayName("Test achieving an enemies goal by destroying more enemies than necessary")
    public void destroyExtraEnemies() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_EnemiesGoalTest_destroyExtraEnemies",
                "c_EnemiesGoalTest_destroyExtraEnemies");

        // move player to right
        res = dmc.tick(Direction.RIGHT);

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // activate bomb
        res = dmc.tick(Direction.DOWN);

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("16-5")
    @DisplayName("Test achieving an enemies goal that requires enemies and spawners to be destroyed")
    public void enemiesAndSpawners() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_EnemiesGoalTest_enemiesAndSpawners",
                "c_EnemiesGoalTest_enemiesAndSpawners");

        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // pick up sword
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // destroy spawner
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));
        assertEquals(0, TestUtils.getEntities(res, "zombie_toast_spawner").size());

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // destroy enemy
        res = dmc.tick(Direction.RIGHT);

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("16-6")
    @DisplayName("Test exit must be achieved last in an enemies and exit goal")
    public void enemiesAndExitOrder() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_EnemiesGoalTest_enemiesAndExitOrder",
                "c_EnemiesGoalTest_enemiesAndExitOrder");

        assertEquals(2, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        String spawnerIdOne = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();
        String spawnerIdTwo = TestUtils.getEntities(res, "zombie_toast_spawner").get(1).getId();

        // move player to right
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        assertTrue(TestUtils.getGoals(res).contains(":exit"));

        // move player to exit
        res = dmc.tick(Direction.RIGHT);

        // destroy spawners
        res = dmc.tick(Direction.RIGHT);
        res = assertDoesNotThrow(() -> dmc.interact(spawnerIdOne));
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        res = assertDoesNotThrow(() -> dmc.interact(spawnerIdTwo));
        assertEquals(0, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        assertFalse(TestUtils.getGoals(res).contains(":enemies"));
        assertNotEquals("", TestUtils.getGoals(res));

        // move to exit
        res = dmc.tick(Direction.LEFT);

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("16-7")
    @DisplayName("Test whether enemies goal can be used in a disjunction")
    public void enemiesDisjunction() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_EnemiesGoalTest_enemiesDisjunction",
                "c_EnemiesGoalTest_enemiesDisjunction");

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        assertTrue(TestUtils.getGoals(res).contains(":exit"));

        // move player to enemy
        res = dmc.tick(Direction.RIGHT);

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }
}
