package dungeonmania.extensions;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TimeTravelTest {
    @Test
    @Tag("33-1")
    @DisplayName("Test rewinding negative ticks")
    public void rewindNegative() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TimeTravelTest_rewindException",
                "c_TimeTravelTest_rewindException");

        res = dmc.tick(Direction.RIGHT);
        assertThrows(IllegalArgumentException.class, () -> dmc.rewind(-1));
    }

    @Test
    @Tag("33-2")
    @DisplayName("Test rewinding zero ticks")
    public void rewindZero() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TimeTravelTest_rewindException",
                "c_TimeTravelTest_rewindException");

        res = dmc.tick(Direction.RIGHT);
        assertThrows(IllegalArgumentException.class, () -> dmc.rewind(0));
    }

    @Test
    @Tag("33-3")
    @DisplayName("Test rewinding more ticks than have occurred")
    public void rewindNotOccurred() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TimeTravelTest_rewindException",
                "c_TimeTravelTest_rewindException");

        assertThrows(IllegalArgumentException.class, () -> dmc.rewind(1));
    }

    @Test
    @Tag("33-4")
    @DisplayName("Test collecting time turner")
    public void timeTurner() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TimeTravelTest_timeTurner",
                "c_TimeTravelTest_timeTurner");

        assertEquals(1, TestUtils.getEntities(res, "time_turner").size());
        assertEquals(0, TestUtils.getInventory(res, "time_turner").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getEntities(res, "time_turner").size());
        assertEquals(1, TestUtils.getInventory(res, "time_turner").size());
    }

    @Test
    @Tag("33-5")
    @DisplayName("Test inventory persists when rewinding")
    public void rewindInventory() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TimeTravelTest_rewindInventory",
                "c_TimeTravelTest_rewindInventory");

        // pick up items
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "wood").size());
        assertEquals(2, TestUtils.getInventory(res, "arrow").size());
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        assertEquals(1, TestUtils.getInventory(res, "time_turner").size());

        // rewind
        res = dmc.rewind(5);
        assertEquals(1, TestUtils.getInventory(res, "wood").size());
        assertEquals(2, TestUtils.getInventory(res, "arrow").size());
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        assertEquals(1, TestUtils.getEntities(res, "wood").size());
        assertEquals(2, TestUtils.getEntities(res, "arrow").size());
        assertEquals(1, TestUtils.getEntities(res, "sword").size());
    }

    @Test
    @Tag("33-6")
    @DisplayName("Test entities are the same as previous time when rewinding")
    public void rewindEntities() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TimeTravelTest_rewindEntities",
                "c_TimeTravelTest_rewindEntities");

        assertEquals(new Position(3, 1), TestUtils.getEntityPos(res, "mercenary"));
        assertEquals(new Position(6, 1), TestUtils.getEntityPos(res, "time_turner"));
        assertEquals(new Position(8, 8), TestUtils.getEntityPos(res, "exit"));
        assertEquals(new Position(5, 8), TestUtils.getEntityPos(res, "wall"));
        assertEquals(new Position(5, 5), TestUtils.getEntityPos(res, "spider"));
        assertEquals(new Position(6, 3), TestUtils.getEntityPos(res, "treasure"));
        assertEquals(new Position(9, 3), TestUtils.getEntityPos(res, "boulder"));

        // pick up time turner
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // rewind
        res = dmc.rewind(5);
        assertEquals(new Position(1, 1), TestUtils.getEntityPos(res, "older_player"));
        assertEquals(new Position(6, 1), TestUtils.getEntityPos(res, "player"));
        assertEquals(new Position(3, 1), TestUtils.getEntityPos(res, "mercenary"));
        assertEquals(new Position(6, 1), TestUtils.getEntityPos(res, "time_turner"));
        assertEquals(new Position(8, 8), TestUtils.getEntityPos(res, "exit"));
        assertEquals(new Position(5, 8), TestUtils.getEntityPos(res, "wall"));
        assertEquals(new Position(5, 5), TestUtils.getEntityPos(res, "spider"));
        assertEquals(new Position(6, 3), TestUtils.getEntityPos(res, "treasure"));
        assertEquals(new Position(9, 3), TestUtils.getEntityPos(res, "boulder"));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(2, 1), TestUtils.getEntityPos(res, "older_player"));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(3, 1), TestUtils.getEntityPos(res, "older_player"));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 1), TestUtils.getEntityPos(res, "older_player"));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 1), TestUtils.getEntityPos(res, "older_player"));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getEntities(res, "older_player").size());
    }

    @Test
    @Tag("33-7")
    @DisplayName("Test travelling through time travelling portal before 30 ticks")
    public void portalBeforeThirtyTicks() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TimeTravelTest_portalBeforeThirtyTicks",
                "c_TimeTravelTest_portalBeforeThirtyTicks");

        assertEquals(new Position(6, 1), TestUtils.getEntityPos(res, "time_travelling_portal"));
        assertEquals(new Position(8, 8), TestUtils.getEntityPos(res, "exit"));
        assertEquals(new Position(5, 8), TestUtils.getEntityPos(res, "wall"));
        assertEquals(new Position(5, 5), TestUtils.getEntityPos(res, "spider"));
        assertEquals(new Position(6, 3), TestUtils.getEntityPos(res, "treasure"));
        assertEquals(new Position(9, 3), TestUtils.getEntityPos(res, "boulder"));

        // travel through time travelling portal
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // after time travel
        assertEquals(new Position(1, 1), TestUtils.getEntityPos(res, "older_player"));
        assertEquals(new Position(6, 1), TestUtils.getEntityPos(res, "player"));
        assertEquals(new Position(6, 1), TestUtils.getEntityPos(res, "time_travelling_portal"));
        assertEquals(new Position(8, 8), TestUtils.getEntityPos(res, "exit"));
        assertEquals(new Position(5, 8), TestUtils.getEntityPos(res, "wall"));
        assertEquals(new Position(5, 5), TestUtils.getEntityPos(res, "spider"));
        assertEquals(new Position(6, 3), TestUtils.getEntityPos(res, "treasure"));
        assertEquals(new Position(9, 3), TestUtils.getEntityPos(res, "boulder"));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(2, 1), TestUtils.getEntityPos(res, "older_player"));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(3, 1), TestUtils.getEntityPos(res, "older_player"));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 1), TestUtils.getEntityPos(res, "older_player"));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(5, 1), TestUtils.getEntityPos(res, "older_player"));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getEntities(res, "older_player").size());
    }

    @Test
    @Tag("33-8")
    @DisplayName("Test travelling through time travelling portal after 30 ticks")
    public void portalAfterThirtyTicks() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TimeTravelTest_portalAfterThirtyTicks",
                "c_TimeTravelTest_portalAfterThirtyTicks");

        assertEquals(new Position(36, 1), TestUtils.getEntityPos(res, "time_travelling_portal"));
        assertEquals(new Position(8, 8), TestUtils.getEntityPos(res, "exit"));
        assertEquals(new Position(5, 8), TestUtils.getEntityPos(res, "wall"));
        assertEquals(new Position(5, 5), TestUtils.getEntityPos(res, "spider"));
        assertEquals(new Position(6, 3), TestUtils.getEntityPos(res, "treasure"));
        assertEquals(new Position(9, 3), TestUtils.getEntityPos(res, "boulder"));

        // travel through time travelling portal
        for (int i = 0; i < 35; ++i) {
            res = dmc.tick(Direction.RIGHT);
        }

        // after time travel
        assertEquals(new Position(6, 1), TestUtils.getEntityPos(res, "older_player"));
        assertEquals(new Position(36, 1), TestUtils.getEntityPos(res, "player"));
        assertEquals(new Position(36, 1), TestUtils.getEntityPos(res, "time_travelling_portal"));
        assertEquals(new Position(8, 8), TestUtils.getEntityPos(res, "exit"));
        assertEquals(new Position(5, 8), TestUtils.getEntityPos(res, "wall"));
        assertEquals(new Position(5, 6), TestUtils.getEntityPos(res, "spider"));
        assertEquals(new Position(6, 3), TestUtils.getEntityPos(res, "treasure"));
        assertEquals(new Position(9, 3), TestUtils.getEntityPos(res, "boulder"));

        for (int i = 7; i < 36; ++i) {
            res = dmc.tick(Direction.RIGHT);
            assertEquals(new Position(i, 1), TestUtils.getEntityPos(res, "older_player"));
        }
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getEntities(res, "older_player").size());
    }

    @Test
    @Tag("33-9")
    @DisplayName("Test mercenary follows current player")
    public void mercenaryFollow() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TimeTravelTest_mercenaryFollow",
                "c_TimeTravelTest_mercenaryFollow");

        // rewind
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.rewind(4);

        assertEquals(new Position(3, 3), TestUtils.getEntityPos(res, "mercenary"));
        assertEquals(new Position(5, 1), TestUtils.getEntityPos(res, "player"));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 3), TestUtils.getEntityPos(res, "mercenary"));
    }

    @Test
    @Tag("33-10")
    @DisplayName("Test encountering older player when invisible")
    public void invisible() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TimeTravelTest_invisible",
                "c_TimeTravelTest_invisible");

        // pick up invisibility potion
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "invisibility_potion").size());
        res = dmc.tick(Direction.RIGHT);

        // rewind
        res = dmc.rewind(2);

        // do not battle
        assertEquals(1, TestUtils.getInventory(res, "invisibility_potion").size());
        assertEquals(new Position(1, 1), TestUtils.getEntityPos(res, "older_player"));
        assertEquals(new Position(3, 1), TestUtils.getEntityPos(res, "player"));
        String potionId = TestUtils.getFirstItemId(res, "invisibility_potion");
        res = assertDoesNotThrow(() -> dmc.tick(potionId));
        res = dmc.tick(Direction.LEFT);
        assertEquals(0, res.getBattles().size());
    }

    @Test
    @Tag("33-11")
    @DisplayName("Test encountering older player when it is invisible")
    public void olderInvisible() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TimeTravelTest_olderInvisible",
                "c_TimeTravelTest_olderInvisible");

        // pick up invisibility potion
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "invisibility_potion").size());
        String potionId = TestUtils.getFirstItemId(res, "invisibility_potion");
        res = assertDoesNotThrow(() -> dmc.tick(potionId));
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // rewind
        res = dmc.rewind(5);

        // do not battle
        assertEquals(new Position(2, 1), TestUtils.getEntityPos(res, "older_player"));
        assertEquals(new Position(6, 1), TestUtils.getEntityPos(res, "player"));
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        assertEquals(0, res.getBattles().size());
    }

    @Test
    @Tag("33-12")
    @DisplayName("Test battle with older player")
    public void olderPlayerBattle() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TimeTravelTest_olderPlayerBattle",
                "c_TimeTravelTest_olderPlayerBattle");

        res = dmc.tick(Direction.RIGHT);

        // rewind
        res = dmc.rewind(1);

        // battle
        assertEquals(new Position(1, 1), TestUtils.getEntityPos(res, "older_player"));
        assertEquals(new Position(2, 1), TestUtils.getEntityPos(res, "player"));
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, res.getBattles().size());
        assertEquals(0, TestUtils.getEntities(res, "older_player").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());
    }

    @Test
    @Tag("33-13")
    @DisplayName("Test another battle with older player")
    public void olderPlayerBattleTwo() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_TimeTravelTest_olderPlayerBattle",
                "c_TimeTravelTest_olderPlayerBattle");

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // rewind
        res = dmc.rewind(2);

        // battle
        assertEquals(new Position(1, 1), TestUtils.getEntityPos(res, "older_player"));
        assertEquals(new Position(3, 1), TestUtils.getEntityPos(res, "player"));
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, res.getBattles().size());
        assertEquals(0, TestUtils.getEntities(res, "older_player").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());
    }
}
