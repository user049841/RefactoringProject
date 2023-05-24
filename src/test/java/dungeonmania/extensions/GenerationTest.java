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

public class GenerationTest {
    @Test
    @Tag("25-1")
    @DisplayName("Test a dungeon where the player is next to the exit")
    public void playerNextToExit() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.generateDungeon(1, 1, 2, 1, "c_GenerationTest_playerNextToExit");

        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertEquals(new Position(1, 1), TestUtils.getEntities(res, "player").get(0).getPosition());
        assertEquals(new Position(2, 1), TestUtils.getEntities(res, "exit").get(0).getPosition());

        // move player to exit
        res = dmc.tick(Direction.RIGHT);

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("25-2")
    @DisplayName("Test a small dungeon where the player has to find the exit")
    public void smallMaze() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.generateDungeon(1, 1, 4, 4, "c_GenerationTest_smallMaze");

        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertEquals(new Position(1, 1), TestUtils.getEntities(res, "player").get(0).getPosition());
        assertEquals(new Position(4, 4), TestUtils.getEntities(res, "exit").get(0).getPosition());

        Direction currentDirection = Direction.RIGHT;
        Position playerPosition = TestUtils.getEntities(res, "player").get(0).getPosition();

        while (!playerPosition.equals(new Position(4, 4))) {
            assertTrue(TestUtils.getGoals(res).contains(":exit"));
            Position newPosition = Position.translateBy(playerPosition, currentDirection);
            if (TestUtils.getEntityAtPos(res, "wall", newPosition).isEmpty()) {
                res = dmc.tick(currentDirection);
                playerPosition = TestUtils.getEntities(res, "player").get(0).getPosition();
                currentDirection = turnLeftDirection(currentDirection);
            } else {
                currentDirection = turnLeftDirection(turnLeftDirection(turnLeftDirection(currentDirection)));
            }

            assertFalse(playerPosition.equals(new Position(1, 1)) && currentDirection == Direction.RIGHT);
        }

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("25-3")
    @DisplayName("Test a large dungeon where the player has to find the exit")
    public void largeMaze() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.generateDungeon(-2, -3, 9, 16, "c_GenerationTest_largeMaze");

        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertEquals(new Position(-2, -3), TestUtils.getEntities(res, "player").get(0).getPosition());
        assertEquals(new Position(9, 16), TestUtils.getEntities(res, "exit").get(0).getPosition());

        Direction currentDirection = Direction.RIGHT;
        Position playerPosition = TestUtils.getEntities(res, "player").get(0).getPosition();

        while (!playerPosition.equals(new Position(9, 16))) {
            assertTrue(TestUtils.getGoals(res).contains(":exit"));
            Position newPosition = Position.translateBy(playerPosition, currentDirection);
            if (TestUtils.getEntityAtPos(res, "wall", newPosition).isEmpty()) {
                res = dmc.tick(currentDirection);
                playerPosition = TestUtils.getEntities(res, "player").get(0).getPosition();
                currentDirection = turnLeftDirection(currentDirection);
            } else {
                currentDirection = turnLeftDirection(turnLeftDirection(turnLeftDirection(currentDirection)));
            }

            assertFalse(playerPosition.equals(new Position(-2, -3)) && currentDirection == Direction.RIGHT);
        }

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    private Direction turnLeftDirection(Direction direction) {
        switch (direction) {
            case UP:
                return Direction.LEFT;
            case LEFT:
                return Direction.DOWN;
            case DOWN:
                return Direction.RIGHT;
            case RIGHT:
                return Direction.UP;
            default:
                return Direction.NONE;
        }
    }
}
