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

public class AllyStatsTest {

    @Test
    @Tag("23-1")
    @DisplayName("Test allies give the player bonus attack")
    public void allyAttackBonus() {
        // Round damage calculation here is similar to the test for bow damage, since the config stats are equal
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_allyStatsTest_allyAttackBonus", "c_allyStatsTest_allyAttackBonus");
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // bribe mercenary
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        // fight spider
        res = dmc.tick(Direction.DOWN);

        // check battle was won
        assertEquals(1, res.getBattles().size());
        assertEquals(0, TestUtils.getEntities(res, "spider").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());
    }

    @Test
    @Tag("23-2")
    @DisplayName("Test allies give the player bonus defence")
    public void allyDefenceBonus() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_allyStatsTest_allyDefenceBonus", "c_allyStatsTest_allyDefenceBonus");
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // bribe mercenary
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        // fight spider
        res = dmc.tick(Direction.DOWN);

        // check battle was won
        assertEquals(1, res.getBattles().size());
        assertEquals(0, TestUtils.getEntities(res, "spider").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());
    }

    @Test
    @Tag("23-3")
    @DisplayName("Test multiple allies give the player the sum of their bonuses")
    public void multipleAllyBonuses() {
        /* Battle calculations:
        player health = 11
        player base attack damage = 4
        total ally attack bonus = 6
        total ally defence bonus = 3
        enemy health = 10
        enemy attack damage = 23

        Battle occurs:
        - Round 1   enemy health    = 10 - ((4 + 6) / 5)  = 8
                    player health   = 11 - ((23 - 3) / 10)    = 9
        - Round 2   enemy health    = 8 - ((4 + 6) / 5)   = 6
                    player health   = 9 - (23 - 3) / 10)     = 7
        - Round 3   enemy health    = 6 - ((4 + 6) / 5)   = 4
                    player health   = 7 - (23 - 3) / 10)     = 5
        - Round 4   enemy health    = 4 - ((4 + 6) / 5)   = 2
                    player health   = 5 - (23 - 3) / 10)     = 3
        - Round 5   enemy health    = 2 - ((4 + 6) / 5)   = 0
                    player health   = 3 - (23 - 3) / 10)     = 1
         */

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_allyStatsTest_multipleAllyBonuses",
                "c_allyStatsTest_multipleAllyBonuses");
        List<EntityResponse> values = TestUtils.getEntitiesStream(res, "mercenary").collect(Collectors.toList());
        String mercId1 = getMercId(values, new Position(0, 2));
        String mercId2 = getMercId(values, new Position(8, 2));
        String assassinId = TestUtils.getEntitiesStream(res, "assassin").findFirst().get().getId();

        // bribe merc to left of player
        res = dmc.tick(Direction.RIGHT);
        res = assertDoesNotThrow(() -> dmc.interact(mercId1));

        // bribe merc to right of player
        res = dmc.tick(Direction.RIGHT);
        res = assertDoesNotThrow(() -> dmc.interact(mercId2));

        // bribe assassin
        res = dmc.tick(Direction.RIGHT);
        res = assertDoesNotThrow(() -> dmc.interact(assassinId));

        // fight spider
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        // check battle was won
        assertEquals(1, res.getBattles().size());
        assertEquals(0, TestUtils.getEntities(res, "spider").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());
    }

    @Test
    @Tag("23-4")
    @DisplayName("Test ally stat bonuses do not override invincibility potion effects")
    public void allyBonusAndInvincibilityPotion() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_allyStatsTest_allyBonusAndInvincibilityPotion",
                "c_allyStatsTest_allyBonusAndInvincibilityPotion");

        // pick up invincibility potion
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "invincibility_potion").size());
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // bribe mercenary
        res = assertDoesNotThrow(() -> dmc.interact(mercId));

        // use potion
        String potionId = TestUtils.getFirstItemId(res, "invincibility_potion");
        res = assertDoesNotThrow(() -> dmc.tick(potionId));

        // fight spider
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        // check battle was won
        assertEquals(1, res.getBattles().size());
        assertEquals(0, TestUtils.getEntities(res, "spider").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());
    }

    @Test
    @Tag("23-5")
    @DisplayName("Test ally stat bonuses do not override invisibility potion effects")
    public void allyBonusAndInvisibilityPotion() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_allyStatsTest_allyBonusAndInvisibilityPotion",
                "c_allyStatsTest_allyBonusAndInvisibilityPotion");

        // pick up invincibility potion
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "invisibility_potion").size());
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // bribe mercenary
        res = assertDoesNotThrow(() -> dmc.interact(mercId));

        // use potion
        String potionId = TestUtils.getFirstItemId(res, "invisibility_potion");
        res = assertDoesNotThrow(() -> dmc.tick(potionId));

        // walk into spider
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.DOWN);

        // check no battle occurred
        assertEquals(0, res.getBattles().size());
        assertEquals(1, TestUtils.getEntities(res, "spider").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());
    }

    private String getMercId(List<EntityResponse> mercenaries, Position pos) {
        return mercenaries.stream().filter(merc -> merc.getPosition().equals(pos)).findFirst().get().getId();
    }
}
