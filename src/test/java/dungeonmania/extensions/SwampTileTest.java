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

public class SwampTileTest {
    @Test
    @Tag("28-1")
    @DisplayName("Test player can move through swamp tile")
    public void playerMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SwampTileTest_playerMovement",
                "c_SwampTileTest_playerMovement");

        assertEquals(1, TestUtils.getEntities(res, "swamp_tile").size());
        Position swampTilePosition = new Position(2, 1);
        Position playerPosition = TestUtils.getEntities(res, "player").get(0).getPosition();
        assertEquals(new Position(1, 1), playerPosition);

        // move onto swamp tile
        res = dmc.tick(Direction.RIGHT);
        playerPosition = TestUtils.getEntities(res, "player").get(0).getPosition();
        assertEquals(swampTilePosition, playerPosition);

        // move off the swamp tile
        res = dmc.tick(Direction.RIGHT);
        playerPosition = TestUtils.getEntities(res, "player").get(0).getPosition();
        assertEquals(new Position(3, 1), playerPosition);
    }

    @Test
    @Tag("28-2")
    @DisplayName("Test mercenary is stuck in swamp tile")
    public void mercenaryMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SwampTileTest_mercenaryMovement",
                "c_SwampTileTest_mercenaryMovement");

        assertEquals(1, TestUtils.getEntities(res, "swamp_tile").size());
        Position swampTilePosition = new Position(2, 1);
        Position mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(new Position(1, 1), mercenaryPosition);

        // move onto swamp tile
        res = dmc.tick(Direction.RIGHT);
        mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(swampTilePosition, mercenaryPosition);
        res = dmc.tick(Direction.RIGHT);
        mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(swampTilePosition, mercenaryPosition);
        res = dmc.tick(Direction.RIGHT);
        mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(swampTilePosition, mercenaryPosition);
        res = dmc.tick(Direction.RIGHT);
        mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(swampTilePosition, mercenaryPosition);

        // move off the swamp tile
        res = dmc.tick(Direction.RIGHT);
        mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(new Position(3, 1), mercenaryPosition);
    }

    @Test
    @Tag("28-3")
    @DisplayName("Test spider is stuck in swamp tile")
    public void spiderMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SwampTileTest_spiderMovement",
                "c_SwampTileTest_spiderMovement");

        assertEquals(1, TestUtils.getEntities(res, "swamp_tile").size());
        Position swampTilePosition = new Position(2, 1);
        Position spiderPosition = TestUtils.getEntities(res, "spider").get(0).getPosition();
        assertEquals(new Position(2, 2), spiderPosition);

        // move onto swamp tile
        res = dmc.tick(Direction.RIGHT);
        spiderPosition = TestUtils.getEntities(res, "spider").get(0).getPosition();
        assertEquals(swampTilePosition, spiderPosition);
        res = dmc.tick(Direction.RIGHT);
        spiderPosition = TestUtils.getEntities(res, "spider").get(0).getPosition();
        assertEquals(swampTilePosition, spiderPosition);
        res = dmc.tick(Direction.RIGHT);
        spiderPosition = TestUtils.getEntities(res, "spider").get(0).getPosition();
        assertEquals(swampTilePosition, spiderPosition);
        res = dmc.tick(Direction.RIGHT);
        spiderPosition = TestUtils.getEntities(res, "spider").get(0).getPosition();
        assertEquals(swampTilePosition, spiderPosition);

        // move off the swamp tile
        res = dmc.tick(Direction.RIGHT);
        spiderPosition = TestUtils.getEntities(res, "spider").get(0).getPosition();
        assertEquals(new Position(3, 1), spiderPosition);
        res = dmc.tick(Direction.RIGHT);
        spiderPosition = TestUtils.getEntities(res, "spider").get(0).getPosition();
        assertEquals(new Position(3, 2), spiderPosition);
    }

    @Test
    @Tag("28-4")
    @DisplayName("Test zombie toast is stuck in swamp tile")
    public void zombieToastMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SwampTileTest_zombieToastMovement",
                "c_SwampTileTest_zombieToastMovement");

        assertEquals(1, TestUtils.getEntities(res, "swamp_tile").size());
        Position swampTilePosition = new Position(2, 1);
        Position zombieToastPosition = TestUtils.getEntities(res, "zombie_toast").get(0).getPosition();
        assertEquals(new Position(1, 1), zombieToastPosition);

        // move onto swamp tile
        res = dmc.tick(Direction.RIGHT);
        zombieToastPosition = TestUtils.getEntities(res, "zombie_toast").get(0).getPosition();
        assertEquals(swampTilePosition, zombieToastPosition);
        res = dmc.tick(Direction.RIGHT);
        zombieToastPosition = TestUtils.getEntities(res, "zombie_toast").get(0).getPosition();
        assertEquals(swampTilePosition, zombieToastPosition);
        res = dmc.tick(Direction.RIGHT);
        zombieToastPosition = TestUtils.getEntities(res, "zombie_toast").get(0).getPosition();
        assertEquals(swampTilePosition, zombieToastPosition);
        res = dmc.tick(Direction.RIGHT);
        zombieToastPosition = TestUtils.getEntities(res, "zombie_toast").get(0).getPosition();
        assertEquals(swampTilePosition, zombieToastPosition);

        // move off the swamp tile
        res = dmc.tick(Direction.RIGHT);
        zombieToastPosition = TestUtils.getEntities(res, "zombie_toast").get(0).getPosition();
        assertTrue(zombieToastPosition.equals(new Position(1, 1)) || zombieToastPosition.equals(new Position(3, 1)));
    }

    @Test
    @Tag("28-5")
    @DisplayName("Test if swamp has no effect when movement factor is zero")
    public void zeroMovementFactor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SwampTileTest_zeroMovementFactor",
                "c_SwampTileTest_zeroMovementFactor");

        assertEquals(1, TestUtils.getEntities(res, "swamp_tile").size());
        Position swampTilePosition = new Position(2, 1);
        Position mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(new Position(1, 1), mercenaryPosition);

        // move onto swamp tile
        res = dmc.tick(Direction.RIGHT);
        mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(swampTilePosition, mercenaryPosition);

        // move off the swamp tile
        res = dmc.tick(Direction.RIGHT);
        mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(new Position(3, 1), mercenaryPosition);
    }

    @Test
    @Tag("28-6")
    @DisplayName("Test ally is stuck in swamp tile")
    public void allyMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SwampTileTest_allyMovement",
                "c_SwampTileTest_allyMovement");

        assertEquals(1, TestUtils.getEntities(res, "swamp_tile").size());
        String mercId = TestUtils.getEntities(res, "mercenary").get(0).getId();
        Position swampTilePosition = new Position(2, 1);
        Position mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(new Position(0, 1), mercenaryPosition);

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        // move onto swamp tile
        mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(swampTilePosition, mercenaryPosition);
        res = dmc.tick(Direction.RIGHT);
        mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(swampTilePosition, mercenaryPosition);
        res = dmc.tick(Direction.RIGHT);
        mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(swampTilePosition, mercenaryPosition);

        // move off the swamp tile
        res = dmc.tick(Direction.RIGHT);
        mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(new Position(3, 1), mercenaryPosition);
    }

    @Test
    @Tag("28-7")
    @DisplayName("Test adjacent ally is not stuck in swamp tile")
    public void adjacentAllyMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SwampTileTest_adjacentAllyMovement",
                "c_SwampTileTest_adjacentAllyMovement");

        assertEquals(1, TestUtils.getEntities(res, "swamp_tile").size());
        String mercId = TestUtils.getEntities(res, "mercenary").get(0).getId();
        Position swampTilePosition = new Position(4, 1);
        Position mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(new Position(0, 1), mercenaryPosition);

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(new Position(2, 1), mercenaryPosition);

        // move onto swamp tile
        res = dmc.tick(Direction.RIGHT);
        mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(new Position(3, 1), mercenaryPosition);
        res = dmc.tick(Direction.RIGHT);
        mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(swampTilePosition, mercenaryPosition);

        // move off the swamp tile
        res = dmc.tick(Direction.RIGHT);
        mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(new Position(5, 1), mercenaryPosition);
    }

    @Test
    @Tag("28-8")
    @DisplayName("Test mercenary is not stuck when bribed by cardinally adjacent player")
    public void adjacentBribeOnSwamp() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SwampTileTest_adjacentBribeOnSwamp",
                "c_SwampTileTest_adjacentBribeOnSwamp");

        assertEquals(1, TestUtils.getEntities(res, "swamp_tile").size());
        String mercId = TestUtils.getEntities(res, "mercenary").get(0).getId();
        Position swampTilePosition = new Position(2, 1);
        Position mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(new Position(3, 1), mercenaryPosition);

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(new Position(0, 1), mercenaryPosition);
    }

    @Test
    @Tag("28-9")
    @DisplayName("Test swamps with different movement factor")
    public void differentMovementFactor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SwampTileTest_differentMovementFactor",
                "c_SwampTileTest_differentMovementFactor");

        assertEquals(2, TestUtils.getEntities(res, "swamp_tile").size());
        Position swampTilePositionOne = new Position(2, 1);
        Position swampTilePositionTwo = new Position(3, 1);
        Position mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(new Position(1, 1), mercenaryPosition);

        // move onto first swamp tile
        res = dmc.tick(Direction.RIGHT);
        mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(swampTilePositionOne, mercenaryPosition);
        res = dmc.tick(Direction.RIGHT);
        mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(swampTilePositionOne, mercenaryPosition);
        res = dmc.tick(Direction.RIGHT);
        mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(swampTilePositionOne, mercenaryPosition);
        res = dmc.tick(Direction.RIGHT);
        mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(swampTilePositionOne, mercenaryPosition);

        // move onto second swamp tile
        res = dmc.tick(Direction.RIGHT);
        mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(swampTilePositionTwo, mercenaryPosition);
        res = dmc.tick(Direction.RIGHT);
        mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(swampTilePositionTwo, mercenaryPosition);
        res = dmc.tick(Direction.RIGHT);
        mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(swampTilePositionTwo, mercenaryPosition);

        // move off the swamp tile
        res = dmc.tick(Direction.RIGHT);
        mercenaryPosition = TestUtils.getEntities(res, "mercenary").get(0).getPosition();
        assertEquals(new Position(4, 1), mercenaryPosition);
    }

    @Test
    @Tag("28-10")
    @DisplayName("Test mercenary moves around swamp tile with large movement factor")
    public void moveAroundSwamp() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SwampTileTest_moveAroundSwamp",
                "c_SwampTileTest_moveAroundSwamp");

        assertEquals(1, TestUtils.getEntities(res, "swamp_tile").size());
        assertEquals(new Position(2, 1), TestUtils.getEntityPos(res, "swamp_tile"));
        assertEquals(new Position(3, 1), TestUtils.getEntityPos(res, "mercenary"));

        // move around swamp
        res = dmc.tick(Direction.UP);
        assertEquals(new Position(3, 2), TestUtils.getEntityPos(res, "mercenary"));
        res = dmc.tick(Direction.UP);
        assertEquals(new Position(2, 2), TestUtils.getEntityPos(res, "mercenary"));
        res = dmc.tick(Direction.UP);
        assertEquals(new Position(1, 2), TestUtils.getEntityPos(res, "mercenary"));
        res = dmc.tick(Direction.UP);
        assertEquals(new Position(1, 1), TestUtils.getEntityPos(res, "mercenary"));
        assertEquals(1, res.getBattles().size());
        assertEquals(1, TestUtils.getEntities(res, "mercenary").size());
        assertEquals(0, TestUtils.getEntities(res, "player").size());
    }

    @Test
    @Tag("28-11")
    @DisplayName("Test mercenary moves through swamp tile with small movement factor")
    public void moveThroughSwamp() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SwampTileTest_moveThroughSwamp",
                "c_SwampTileTest_moveThroughSwamp");

        assertEquals(1, TestUtils.getEntities(res, "swamp_tile").size());
        assertEquals(new Position(2, 1), TestUtils.getEntityPos(res, "swamp_tile"));
        assertEquals(new Position(3, 1), TestUtils.getEntityPos(res, "mercenary"));

        // move through swamp
        res = dmc.tick(Direction.UP);
        assertEquals(new Position(2, 1), TestUtils.getEntityPos(res, "mercenary"));
        res = dmc.tick(Direction.UP);
        assertEquals(new Position(2, 1), TestUtils.getEntityPos(res, "mercenary"));
        res = dmc.tick(Direction.UP);
        assertEquals(new Position(1, 1), TestUtils.getEntityPos(res, "mercenary"));
        assertEquals(1, res.getBattles().size());
        assertEquals(1, TestUtils.getEntities(res, "mercenary").size());
        assertEquals(0, TestUtils.getEntities(res, "player").size());
    }

    @Test
    @Tag("28-12")
    @DisplayName("Test zombie toast is stuck when spawned on swamp tile")
    public void spawnOnSwampTile() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SwampTileTest_spawnOnSwampTile",
                "c_SwampTileTest_spawnOnSwampTile");

        // spawn zombie
        assertEquals(0, TestUtils.getEntities(res, "zombie_toast").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getEntities(res, "zombie_toast").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getEntities(res, "zombie_toast").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getEntities(res, "zombie_toast").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 1), TestUtils.getEntityPos(res, "zombie_toast"));
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 1), TestUtils.getEntityPos(res, "zombie_toast"));
        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(new Position(4, 1), TestUtils.getEntityPos(res, "zombie_toast"));
    }
}
