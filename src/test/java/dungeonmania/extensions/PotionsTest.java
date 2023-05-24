package dungeonmania.extensions;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PotionsTest {
    @Test
    @Tag("19-1")
    @DisplayName("Test that invincibility potion does not take effect before consumed")
    public void noInvincibilityBeforeConsumed() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_potionsTest_noInvincibilityBeforeConsumed",
                "c_potionsTest_noInvincibilityBeforeConsumed");

        assertEquals(1, TestUtils.getEntities(res, "invincibility_potion").size());
        assertEquals(0, TestUtils.getInventory(res, "invincibility_potion").size());

        // pick up invincibility potion
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "invincibility_potion").size());
        assertEquals(0, TestUtils.getEntities(res, "invincibility_potion").size());

        // battle mercenary
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, res.getBattles().size());
        assertEquals(1, TestUtils.getEntities(res, "mercenary").size());
        assertEquals(0, TestUtils.getEntities(res, "player").size());
    }

    @Test
    @Tag("19-2")
    @DisplayName("Test that invisibility potion does not take effect before consumed")
    public void noInvisibilityBeforeConsumed() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_potionsTest_noInvisibilityBeforeConsumed",
                "c_potionsTest_noInvisibilityBeforeConsumed");

        assertEquals(1, TestUtils.getEntities(res, "invisibility_potion").size());
        assertEquals(0, TestUtils.getInventory(res, "invisibility_potion").size());

        // pick up invisibility potion
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "invisibility_potion").size());
        assertEquals(0, TestUtils.getEntities(res, "invisibility_potion").size());

        // battle mercenary
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, res.getBattles().size());
        assertEquals(1, TestUtils.getEntities(res, "mercenary").size());
        assertEquals(0, TestUtils.getEntities(res, "player").size());
    }
}
