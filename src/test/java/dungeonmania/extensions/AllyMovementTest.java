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

public class AllyMovementTest {

    @Test
    @Tag("20-1")
    @DisplayName("Test ally follows player")
    public void followsPlayer() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_allyMovementTest_followsPlayer", "c_allyMovementTest_followsPlayer");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(getMercPos(res), new Position(5, 1));

        // bribe mercenary
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(getMercPos(res), new Position(4, 1));

        for (int i = 0; i < 200; i++) {
            res = dmc.tick(Direction.LEFT);
            assertEquals(getMercPos(res), new Position(3 - i, 1));
            assertNotEquals(TestUtils.getEntities(res, "player").get(0).getPosition(), new Position(2 - i, 1));
        }
    }

    @Test
    @Tag("20-2")
    @DisplayName("Test ally follows the player through a portal")
    public void followsThroughPortal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_allyMovementTest_followsThroughPortal",
                "c_allyMovementTest_followsThroughPortal");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(getMercPos(res), new Position(4, 1));

        // bribe mercenary
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(getMercPos(res), new Position(3, 1));
        assertEquals(getPlayerPos(res), new Position(2, 1));

        // Enter Portal
        res = dmc.tick(Direction.DOWN);
        assertEquals(getMercPos(res), new Position(2, 1));
        assertEquals(getPlayerPos(res), new Position(5, 4));

        res = dmc.tick(Direction.UP);
        assertEquals(getMercPos(res), new Position(5, 4));
        assertEquals(getPlayerPos(res), new Position(5, 3));

        res = dmc.tick(Direction.RIGHT);
        assertEquals(getMercPos(res), new Position(5, 3));
        assertEquals(getPlayerPos(res), new Position(6, 3));

        res = dmc.tick(Direction.LEFT);
        assertEquals(getMercPos(res), new Position(6, 3));
        assertEquals(getPlayerPos(res), new Position(5, 3));
    }

    @Test
    @Tag("20-3")
    @DisplayName("Test ally does not move when player has no previous distinct position")
    public void noPreviousDistinctPosition() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_allyMovementTest_noPreviousDistinctPosition",
                "c_allyMovementTest_noPreviousDistinctPosition");
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        assertEquals(getMercPos(res), new Position(0, 1));
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(getMercPos(res), new Position(0, 1));
    }

    @Test
    @Tag("20-4")
    @DisplayName("Test ally does not overlap player when player moves in place")
    public void doesNotMoveOntoPlayer() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_allyMovementTest_doesNotMoveOntoPlayer",
                "c_allyMovementTest_doesNotMoveOntoPlayer");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(getMercPos(res), new Position(5, 1));

        // bribe mercenary
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(getMercPos(res), new Position(4, 1));
        assertEquals(getPlayerPos(res), new Position(2, 1));

        res = dmc.tick(Direction.LEFT);
        assertEquals(getMercPos(res), new Position(3, 1));
        assertEquals(getPlayerPos(res), new Position(1, 1));

        // Walk into wall (i.e. stay in place)
        for (int i = 0; i < 5; i++) {
            res = dmc.tick(Direction.LEFT);
            assertEquals(getMercPos(res), new Position(2, 1));
            assertEquals(getPlayerPos(res), new Position(1, 1));
        }
    }

    @Test
    @Tag("20-5")
    @DisplayName("Test ally teleports to previous distinct square when adjacent to player")
    public void teleportsToPreviousSquare() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_allyMovementTest_teleportsToPreviousSquare",
                "c_allyMovementTest_teleportsToPreviousSquare");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(getMercPos(res), new Position(5, 1));

        // bribe mercenary
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(getMercPos(res), new Position(4, 1));
        assertEquals(getPlayerPos(res), new Position(2, 1));

        // Walk into wall (i.e. stay in place)
        res = dmc.tick(Direction.DOWN);
        assertEquals(getMercPos(res), new Position(3, 1));
        assertEquals(getPlayerPos(res), new Position(2, 1));

        // Walk into wall again, mercenary should teleport to previous distinct player position
        res = dmc.tick(Direction.DOWN);
        assertEquals(getMercPos(res), new Position(1, 1));
        assertEquals(getPlayerPos(res), new Position(2, 1));
    }

    @Test
    @Tag("20-6")
    @DisplayName("Test ally follows previously distinct square when it is cardinally adjacent")
    public void followsPreviousDistinctSquare() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_allyMovementTest_followsPreviousDistinctSquare",
                "c_allyMovementTest_followsPreviousDistinctSquare");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(getMercPos(res), new Position(4, 1));

        // bribe mercenary
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(getMercPos(res), new Position(3, 1));
        assertEquals(getPlayerPos(res), new Position(2, 1));

        // Player moves right, swapping place with mercenary
        // This tests the mercenary follows previous position
        res = dmc.tick(Direction.RIGHT);
        assertEquals(getMercPos(res), new Position(2, 1));
        assertEquals(getPlayerPos(res), new Position(3, 1));

        // Player moves down into wall, mercenary stays in place.
        // This tests the mercenary follows previous distinct player position.
        res = dmc.tick(Direction.DOWN);
        assertEquals(getMercPos(res), new Position(2, 1));
        assertEquals(getPlayerPos(res), new Position(3, 1));
    }

    @Test
    @Tag("20-7")
    @DisplayName("Test multiple allies properly follow the player")
    public void multipleAlliesFollow() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_allyMovementTest_multipleAlliesFollow",
                "c_allyMovementTest_multipleAlliesFollow");
        List<EntityResponse> values = TestUtils.getEntitiesStream(res, "mercenary").collect(Collectors.toList());
        String mercId1 = getMercId(values, new Position(0, 2));
        String mercId2 = getMercId(values, new Position(8, 2));
        String mercId3 = getMercId(values, new Position(10, 2));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(3, TestUtils.getEntities(res, "mercenary").size());

        // bribe merc to left of player
        res = assertDoesNotThrow(() -> dmc.interact(mercId1));
        assertEquals(getMercPos(res, mercId1), new Position(1, 2));
        res = dmc.tick(Direction.RIGHT);

        // bribe merc to right of player
        res = assertDoesNotThrow(() -> dmc.interact(mercId2));
        assertEquals(getMercPos(res, mercId1), new Position(2, 2));
        assertEquals(getMercPos(res, mercId2), new Position(4, 2));

        // move right.
        res = dmc.tick(Direction.RIGHT);
        assertEquals(getMercPos(res, mercId1), new Position(3, 2));
        assertEquals(getMercPos(res, mercId2), new Position(3, 2));

        // bribe final merc
        res = assertDoesNotThrow(() -> dmc.interact(mercId3));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(getMercPos(res, mercId1), new Position(4, 2));
        assertEquals(getMercPos(res, mercId2), new Position(4, 2));
        assertEquals(getMercPos(res, mercId3), new Position(4, 2));
    }

    @Test
    @Tag("20-8")
    @DisplayName("Test ally moves properly when you teleport in place")
    public void teleportOntoPlace() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_allyMovementTest_teleportOntoPlace",
                "c_allyMovementTest_teleportOntoPlace");
        res = dmc.tick(Direction.RIGHT);
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        // bribe mercenary
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(new Position(1, 2), getPlayerPos(res));
        // mercenary becomes adjacent to player.
        assertEquals(new Position(2, 2), getMercPos(res));

        // mercenary goes to previous distinct player position
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(0, 2), getMercPos(res));

        // check mercenary does not move when player stays still
        res = dmc.tick(Direction.UP);
        assertEquals(new Position(0, 2), getMercPos(res));

        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(0, 2), getMercPos(res));
    }

    private String getMercId(List<EntityResponse> mercenaries, Position pos) {
        return mercenaries.stream().filter(merc -> merc.getPosition().equals(pos)).findFirst().get().getId();
    }

    private Position getMercPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "mercenary").get(0).getPosition();
    }

    private Position getMercPos(DungeonResponse res, String mercId) {
        return TestUtils.getEntities(res, "mercenary").stream()
            .filter(merc -> merc.getId().equals(mercId))
            .findFirst().get().getPosition();
    }

    private Position getPlayerPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "player").get(0).getPosition();
    }
}
