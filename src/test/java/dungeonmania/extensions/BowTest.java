package dungeonmania.extensions;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BowTest {

    @Test
    @Tag("22-1")
    @DisplayName("Test that a bow gives the player bonus damage")
    public void bowBonusDamage() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BowTest_bowBonusDamage", "c_BowTest_bowBonusDamage");

        // Pick up Wood
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "wood").size());

        // Pick up Arrow x3
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(3, TestUtils.getInventory(res, "arrow").size());

        // Build Bow
        assertEquals(0, TestUtils.getInventory(res, "bow").size());
        res = assertDoesNotThrow(() -> dmc.build("bow"));
        assertEquals(1, TestUtils.getInventory(res, "bow").size());

        // Fight mercenary
        res = dmc.tick(Direction.RIGHT);

        // Check battle was won
        assertEquals(1, res.getBattles().size());
        assertEquals(0, TestUtils.getEntities(res, "mercenary").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());
    }

    @Test
    @Tag("22-2")
    @DisplayName("Test that a player does not have bonus damage after the bow breaks")
    public void noBonusAfterBreaking() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BowTest_noBonusAfterBreaking", "c_BowTest_noBonusAfterBreaking");
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        // Pick up Wood
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "wood").size());

        // Pick up Arrow x3
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(3, TestUtils.getInventory(res, "arrow").size());

        // Build Bow
        assertEquals(0, TestUtils.getInventory(res, "bow").size());
        res = assertDoesNotThrow(() -> dmc.build("bow"));
        assertEquals(1, TestUtils.getInventory(res, "bow").size());

        // Destroy zombie toast spawner and thus use up bow durability and break the bow
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));
        assertEquals(0, TestUtils.getInventory(res, "bow").size());

        // Fight Mercenary
        res = dmc.tick(Direction.RIGHT);

        // Check Mercenary won
        assertEquals(1, res.getBattles().size());
        assertEquals(1, TestUtils.getEntities(res, "mercenary").size());
        assertEquals(0, TestUtils.getEntities(res, "player").size());
    }

}
