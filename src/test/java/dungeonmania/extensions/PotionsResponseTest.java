package dungeonmania.extensions;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PotionsResponseTest {

    @Test
    @Tag("34-1")
    @DisplayName("Test potions appear in the battle response")
    public void potionsInBattleResponse() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_potionsResponseTest_potionsInBattleResponse",
                "c_potionsResponseTest_potionsInBattleResponse");

        // pick up potion
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "invincibility_potion").size());

        // use potion
        String potionId = TestUtils.getFirstItemId(res, "invincibility_potion");
        res = assertDoesNotThrow(() -> dmc.tick(potionId));

        // fight mercenary
        res = dmc.tick(Direction.RIGHT);

        // check battle was won
        assertEquals(1, res.getBattles().size());
        assertEquals(0, TestUtils.getEntities(res, "mercenary").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());

        // check potion is in battle response
        assertEquals("invincibility_potion", res.getBattles().get(0).getBattleItems().get(0).getType());
    }
}
