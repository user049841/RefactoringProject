package dungeonmania.extensions;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class SunStoneTest {

    @Test
    @Tag("29-1")
    @DisplayName("Test a sunstone can be picked up and added to the player’s inventory")
    public void pickUpSunstone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_pickUpSunstone", "c_sunStoneTest_pickUpSunstone");
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("29-2")
    @DisplayName("Test a sunstone can be used to open a door")
    public void openDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_openDoor", "c_sunStoneTest_openDoor");

        // pick up sun stone.
        res = dmc.tick(Direction.RIGHT);
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // walk through door
        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
    }

    @Test
    @Tag("29-3")
    @DisplayName("Test a sunstone can open multiple doors, and is retained")
    public void retainedAfterOpeningDoors() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_retainedAfterOpeningDoors",
                "c_sunStoneTest_retainedAfterOpeningDoors");

        // pick up sun stone.
        res = dmc.tick(Direction.RIGHT);
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        for (int i = 0; i < 3; i++) {
            // walk through door
            res = dmc.tick(Direction.RIGHT);
            assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
            pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        }
    }

    @Test
    @Tag("29-4")
    @DisplayName("Test a sunstone can not be used to open a switch door")
    public void canNotOpenSwitchDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_canNotOpenSwitchDoor", "c_sunStoneTest_canNotOpenSwitchDoor");

        // pick up sun stone.
        res = dmc.tick(Direction.RIGHT);
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        res = dmc.tick(Direction.RIGHT);
        assertEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
    }

    @Test
    @Tag("29-5")
    @DisplayName("Test a sunstone can not be used to bribe mercenaries/assassins")
    public void canNotBribe() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_canNotBribe", "c_sunStoneTest_canNotBribe");

        // pick up sun stone.
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        // attempt bribe
        assertThrows(InvalidActionException.class, () -> dmc.interact(mercId));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, res.getBattles().size());
    }

    @Test
    @Tag("29-6")
    @DisplayName("Test a sunstone can be used as treasure/key in building and is retained")
    public void retainedWhenInterchangedInBuilding() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_retainedWhenInterchangedInBuilding",
                "c_sunStoneTest_retainedWhenInterchangedInBuilding");

        List<String> buildables = new ArrayList<>();
        assertEquals(buildables, res.getBuildables());

        // pick up sun stone.
        res = dmc.tick(Direction.RIGHT);

        // pick up wood.
        res = dmc.tick(Direction.RIGHT);

        // pick up wood.
        res = dmc.tick(Direction.RIGHT);

        // Shield added to buildables list
        buildables.add("shield");
        assertEquals(buildables, res.getBuildables());

        // Check only a shield can be built
        assertEquals(buildables.size(), res.getBuildables().size());
        assertTrue(buildables.containsAll(res.getBuildables()));
        assertTrue(res.getBuildables().containsAll(buildables));

        // Build shield
        res = assertDoesNotThrow(() -> dmc.build("shield"));
        assertEquals(1, TestUtils.getInventory(res, "shield").size());

        // Only sun stone is in the player's inventory.
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
    }

    @Test
    @Tag("29-7")
    @DisplayName("Test a sunstone is not retained after building when a sunstone is required as an ingredient")
    public void notRetainedAfterBuildingAsSunstone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_notRetainedAfterBuildingAsSunstone",
                "c_sunStoneTest_notRetainedAfterBuildingAsSunstone");
        List<String> buildables = new ArrayList<>();
        assertEquals(buildables, res.getBuildables());

        // pick up sun stone.
        res = dmc.tick(Direction.RIGHT);

        // pick up wood.
        res = dmc.tick(Direction.RIGHT);

        // pick up key.
        res = dmc.tick(Direction.RIGHT);

        // Check only sceptre can be built
        buildables.add("sceptre");
        assertEquals(buildables.size(), res.getBuildables().size());
        assertTrue(buildables.containsAll(res.getBuildables()));
        assertTrue(res.getBuildables().containsAll(buildables));

        // Build sceptre
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        // Nothing is left in inventory.
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
    }

    @Test
    @Tag("29-8")
    @DisplayName("Test sunstones count towards the treasure goal")
    public void countsForTreasureGoal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunStoneTest_countsForTreasureGoal",
                "c_sunStoneTest_countsForTreasureGoal");

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // collect sun stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }
}
