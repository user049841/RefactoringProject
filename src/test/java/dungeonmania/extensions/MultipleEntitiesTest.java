package dungeonmania.extensions;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MultipleEntitiesTest {

    @Test
    @Tag("24-1")
    @DisplayName("Test that the player moves first when there are multiple moving entities")
    public void simple() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_multipleEntitiesTest_simple", "c_multipleEntitiesTest_simple");
        res = dmc.tick(Direction.RIGHT);
        assertEquals(3, TestUtils.getEntities(res, "mercenary").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());
    }
}
