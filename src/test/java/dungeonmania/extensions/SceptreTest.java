package dungeonmania.extensions;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

public class SceptreTest {

    @Test
    @Tag("30-1")
    @DisplayName("Test a sceptre can be built with wood, a key, and a sunstone")
    public void woodKeySunstone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_woodKeySunstone", "c_sceptreTest_woodKeySunstone");

        // collect building materials
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        // Build sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
    }

    @Test
    @Tag("30-2")
    @DisplayName("Test a sceptre can be built with wood, treasure, and a sunstone")
    public void woodTreasureSunstone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_woodTreasureSunstone", "c_sceptreTest_woodTreasureSunstone");

        // collect building materials
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Build sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

    }

    @Test
    @Tag("30-3")
    @DisplayName("Test a sceptre can be built with arrows, a key, and a sunstone")
    public void arrowsKeySunstone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_arrowsKeySunstone", "c_sceptreTest_arrowsKeySunstone");

        // collect building materials
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Build sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "arrow").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());

    }

    @Test
    @Tag("30-4")
    @DisplayName("Test a sceptre can be built with arrows, a treasure, and a sunstone")
    public void arrowsTreasureSunstone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_arrowsTreasureSunstone",
                "c_sceptreTest_arrowsTreasureSunstone");

        // collect building materials
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Build sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(0, TestUtils.getInventory(res, "arrow").size());

    }

    @Test
    @Tag("30-5")
    @DisplayName("Test a sceptre can be built with arrows and sunstones")
    public void arrowsSunstones() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_arrowsSunstones", "c_sceptreTest_arrowsSunstones");

        // collect building materials
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Build sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "arrow").size());
    }

    @Test
    @Tag("30-6")
    @DisplayName("Test a sceptre can be built with wood and sunstones")
    public void woodSunstones() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_woodSunstones", "c_sceptreTest_woodSunstones");

        // collect building materials
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Build sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
    }

    @Test
    @Tag("30-7")
    @DisplayName("Test a sceptre can ally a mercenary")
    public void allyMercenary() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_allyMercenary", "c_sceptreTest_allyMercenary");

        // collect building materials
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Build sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "wood").size());

        // Ally mercenary
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());
    }

    @Test
    @Tag("30-8")
    @DisplayName("Test a sceptre can ally an assassin")
    public void allyAssassin() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_allyAssassin", "c_sceptreTest_allyAssassin");

        // collect building materials
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Build sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "wood").size());

        // Ally assassin
        String assassinId = TestUtils.getEntitiesStream(res, "assassin").findFirst().get().getId();
        res = assertDoesNotThrow(() -> dmc.interact(assassinId));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());
    }

    @Test
    @Tag("30-9")
    @DisplayName("Test allies made with a sceptre provide bonus stats")
    public void allyStats() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_allyStats", "c_sceptreTest_allyStats");

        // collect building materials
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Build sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "wood").size());

        // Ally mercenary
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        res = assertDoesNotThrow(() -> dmc.interact(mercId));

        // fight spider
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        // check battle was won
        assertEquals(1, res.getBattles().size());
        assertEquals(0, TestUtils.getEntities(res, "spider").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());
    }

    @Test
    @Tag("30-10")
    @DisplayName("Test a sceptre ignores bribe radius")
    public void bypassBribeRadius() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_bypassBribeRadius", "c_sceptreTest_bypassBribeRadius");


        // collect building materials
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Build sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "wood").size());

        // Ally mercenary
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());
    }

    @Test
    @Tag("30-11")
    @DisplayName("Test a sceptre ignores bribe amount")
    public void bypassBribeAmount() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_bypassBribeAmount", "c_sceptreTest_bypassBribeAmount");

        // collect building materials
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Build sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "wood").size());

        // Ally mercenary
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());
    }

    @Test
    @Tag("30-12")
    @DisplayName("Test a sceptre does not fail on assassins with bribe_fail_chance of 1")
    public void bypassBribeFailChance() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_bypassBribeFailChance", "c_sceptreTest_bypassBribeFailChance");

        // collect building materials
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Build sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "wood").size());

        // Ally assassin
        String assassinId = TestUtils.getEntitiesStream(res, "assassin").findFirst().get().getId();
        res = assertDoesNotThrow(() -> dmc.interact(assassinId));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());
    }

    @Test
    @Tag("30-13")
    @DisplayName("Test the sceptre lasts for a certain duration")
    public void temporaryDuration() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_temporaryDuration", "c_sceptreTest_temporaryDuration");

        // collect building materials
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Build sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "wood").size());

        // Ally mercenary
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        // Check sceptre lasts for exactly 3 ticks.
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());
        res = dmc.tick(Direction.LEFT);
        assertEquals(0, res.getBattles().size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, res.getBattles().size());
    }

    @Test
    @Tag("30-14")
    @DisplayName("Test a sceptre can be used to make multiple allies")
    public void multipleAllies() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_multipleAllies", "c_sceptreTest_multipleAllies");

        // get mercenary ids
        List<EntityResponse> values = TestUtils.getEntitiesStream(res, "mercenary").collect(Collectors.toList());
        String mercId1 = getMercId(values, new Position(0, 1));
        String mercId2 = getMercId(values, new Position(12, 1));
        String mercId3 = getMercId(values, new Position(13, 1));

        // collect building materials
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Build sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "wood").size());

        // ally first merc
        res = assertDoesNotThrow(() -> dmc.interact(mercId1));

        // ally second merc
        res = assertDoesNotThrow(() -> dmc.interact(mercId2));

        // ally final merc
        res = assertDoesNotThrow(() -> dmc.interact(mercId3));

        // walk into wall to let mercenaries reach player and check that there are no battles
        for (int i = 0; i < 10; i++)
            res = dmc.tick(Direction.DOWN);
        assertEquals(0, res.getBattles().size());
    }

    private String getMercId(List<EntityResponse> mercenaries, Position pos) {
        return mercenaries.stream().filter(merc -> merc.getPosition().equals(pos)).findFirst().get().getId();
    }

}
