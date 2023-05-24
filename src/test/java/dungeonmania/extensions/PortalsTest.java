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

public class PortalsTest {

    @Test
    @Tag("27-1")
    @DisplayName("Test player teleporting to a portal with cardinally adjacent wall")
    public void teleportWithWall() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_PortalsTest_teleportWithWall",
                "c_PortalsTest_teleportWithWall");

        res = dmc.tick(Direction.RIGHT);
        assertTrue(TestUtils.getPlayerPos(res).equals(new Position(5, 5))
                || TestUtils.getPlayerPos(res).equals(new Position(4, 4))
                || TestUtils.getPlayerPos(res).equals(new Position(6, 4)));
    }
}
