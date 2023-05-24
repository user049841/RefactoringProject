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

public class MercenaryTest {

    @Test
    @Tag("18-1")
    @DisplayName("Test mercenary moves towards player when starting 210 distance away")
    public void longDistanceMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_longDistanceMovement",
                "c_mercenaryTest_longDistanceMovement");

        // move towards mercenary
        for (int i = 211; i > 106; --i) {
            assertEquals(new Position(i, 1), getMercPos(res));
            res = dmc.tick(Direction.RIGHT);
        }

        // battle mercenary
        assertEquals(1, res.getBattles().size());
        assertEquals(0, TestUtils.getEntities(res, "mercenary").size());
    }

    private Position getMercPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "mercenary").get(0).getPosition();
    }
}
