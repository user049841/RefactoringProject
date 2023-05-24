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

import java.util.List;

public class EnemiesOverlappingTest {

    @Test
    @Tag("35-1")
    @DisplayName("Test that multiple enemies can overlap")
    public void enemiesOverlap() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_enemiesOverlappingTest_enemiesOverlap",
                "c_enemiesOverlappingTest_enemiesOverlap");
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        List<Position> mercPos = TestUtils.getEntityPositions(res, "mercenary");
        assertEquals(mercPos.get(0), mercPos.get(1));
        assertEquals(new Position(2, 1), mercPos.get(0));
        assertEquals(new Position(2, 1), TestUtils.getEntityPos(res, "zombie_toast"));
    }
}
