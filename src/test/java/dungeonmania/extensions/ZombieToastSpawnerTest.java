package dungeonmania.extensions;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ZombieToastSpawnerTest {

    @Test
    @Tag("15-1")
    @DisplayName("Test that the player cannot interact with zombie toast spawner without a weapon")
    public void interactWithoutWeapon() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawnerTest_interactWithoutWeapon",
                "c_zombieToastSpawnerTest_interactWithoutWeapon");

        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        assertThrows(InvalidActionException.class, () ->
                dmc.interact(spawnerId)
        );
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());
    }

    @Test
    @Tag("15-2")
    @DisplayName("Test that the player cannot interact with the zombie toast spawner when not cardinally adjacent")
    public void interactNotCardinallyAdjacent() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawnerTest_interactNotCardinallyAdjacent",
                "c_zombieToastSpawnerTest_interactNotCardinallyAdjacent");

        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        // pick up sword
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // has sword
        assertThrows(InvalidActionException.class, () ->
                dmc.interact(spawnerId)
        );
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
    }

    @Test
    @Tag("15-3")
    @DisplayName("Test destroying a zombie toast spawner with a sword")
    public void destroySpawnerWithSword() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawnerTest_destroySpawnerWithSword",
                "c_zombieToastSpawnerTest_destroySpawnerWithSword");

        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        // pick up sword
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // destroy zombie toast spawner
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));
        assertEquals(0, TestUtils.getEntities(res, "zombie_toast_spawner").size());
    }

    @Test
    @Tag("15-4")
    @DisplayName("Test destroying a zombie toast spawner with a bow")
    public void destroySpawnerWithBow() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawnerTest_destroySpawnerWithBow",
                "c_zombieToastSpawnerTest_destroySpawnerWithBow");

        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        // create bow
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "wood").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "arrow").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "arrow").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(3, TestUtils.getInventory(res, "arrow").size());
        res = assertDoesNotThrow(() -> dmc.build("bow"));
        assertEquals(1, TestUtils.getInventory(res, "bow").size());

        // destroy zombie toast spawner
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));
        assertEquals(0, TestUtils.getEntities(res, "zombie_toast_spawner").size());
    }

    @Test
    @Tag("15-5")
    @DisplayName("Test that the weapon durability decreases when destroying a zombie toast spawner")
    public void reduceDurability() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieToastSpawnerTest_reduceDurability",
                "c_zombieToastSpawnerTest_reduceDurability");

        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        // pick up sword
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // destroy zombie toast spawner
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));
        assertEquals(0, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
    }
}
