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

public class KeyTest {

    @Test
    @Tag("21-1")
    @DisplayName("Test player walking over two keys only picks up the first one.")
    public void onlyCarryOneKey() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_keyTest_onlyCarryOneKey", "c_keyTest_onlyCarryOneKey");

        // pick up first key
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "key").size());

        // walk over second key without picking it up
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "key").size());
    }

    @Test
    @Tag("21-2")
    @DisplayName("Test player can use a key then get another key")
    public void useKeyThenGetKey() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_keyTest_useKeyThenGetKey", "c_keyTest_useKeyThenGetKey");

        // pick up first key
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "key").size());
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();

        // walk through door and check key is gone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getInventory(res, "key").size());
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());

        // check next key is picked up
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "key").size());
    }
}
