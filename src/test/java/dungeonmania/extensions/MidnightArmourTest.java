package dungeonmania.extensions;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class MidnightArmourTest {

    @Test
    @Tag("31-1")
    @DisplayName("Test midnight armour is not craftable when zombies are alive")
    public void notCraftableWithZombies() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_notCraftableWithZombies",
                "c_midnightArmourTest_notCraftableWithZombies");

        assertEquals(0, res.getBuildables().size());

        // pick up sun stone.
        res = dmc.tick(Direction.RIGHT);

        // pick up sword.
        res = dmc.tick(Direction.RIGHT);

        // check midnight armour can't be crafted
        assertEquals(0, res.getBuildables().size());
        assertThrows(InvalidActionException.class, () -> dmc.build("midnight_armour"));
    }

    @Test
    @Tag("31-2")
    @DisplayName("Test midnight armour is craftable without zombies present")
    public void craftableWithoutZombies() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_craftableWithoutZombies",
                "c_midnightArmourTest_craftableWithoutZombies");

        assertEquals(0, res.getBuildables().size());

        // pick up sun stone.
        res = dmc.tick(Direction.RIGHT);

        // pick up sword.
        res = dmc.tick(Direction.RIGHT);

        // check midnight armour can be built
        List<String> buildables = new ArrayList<>();
        buildables.add("midnight_armour");
        assertEquals(1, res.getBuildables().size());

        // Build midnight armour
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
    }

    @Test
    @Tag("31-3")
    @DisplayName("Test midnight armour is craftable if all zombies present die")
    public void craftableAfterZombiesDie() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_craftableAfterZombiesDie",
                "c_midnightArmourTest_craftableAfterZombiesDie");

        assertEquals(0, res.getBuildables().size());

        // pick up sun stone.
        res = dmc.tick(Direction.RIGHT);

        // pick up sword.
        res = dmc.tick(Direction.RIGHT);

        // check midnight armour can't be crafted
        assertEquals(0, res.getBuildables().size());
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast").size());

        // fight and kill zombie by going into portal
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, res.getBattles().size());
        assertEquals(0, TestUtils.getEntities(res, "zombie_toast").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());


        // check midnight armour can be built
        List<String> buildables = new ArrayList<>();
        buildables.add("midnight_armour");
        assertEquals(1, res.getBuildables().size());
        assertTrue(buildables.containsAll(res.getBuildables()));
        assertTrue(res.getBuildables().containsAll(buildables));

        // Build midnight armour
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
    }

    @Test
    @Tag("31-4")
    @DisplayName("Test midnight armour gives bonus attack")
    public void armourAttackBonus() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_armourAttackBonus",
                "c_midnightArmourTest_armourAttackBonus");

        // pick up sun stone.
        res = dmc.tick(Direction.RIGHT);

        // pick up sword.
        res = dmc.tick(Direction.RIGHT);

        // Build midnight armour
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());

        // fight and kill spider
        res = dmc.tick(Direction.UP);
        assertEquals(1, res.getBattles().size());
        assertEquals(0, TestUtils.getEntities(res, "spider").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());
    }

    @Test
    @Tag("31-5")
    @DisplayName("Test midnight armour gives bonus defence")
    public void armourDefenceBonus() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_armourDefenceBonus",
                "c_midnightArmourTest_armourDefenceBonus");

        // pick up sun stone.
        res = dmc.tick(Direction.RIGHT);

        // pick up sword.
        res = dmc.tick(Direction.RIGHT);

        // Build midnight armour
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));

        // fight and kill spider
        res = dmc.tick(Direction.UP);
        // check battle was won
        assertEquals(1, res.getBattles().size());
        assertEquals(0, TestUtils.getEntities(res, "spider").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());

    }

    @Test
    @Tag("31-6")
    @DisplayName("Test midnight armour lasts for at least 50 ticks and multiple battles")
    public void armourDuration() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_armourDuration",
                "c_midnightArmourTest_armourDuration");

        // pick up sun stone.
        res = dmc.tick(Direction.RIGHT);

        // pick up sword.
        res = dmc.tick(Direction.RIGHT);

        // Build midnight armour
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));

        // battle remaining enemies
        for (int i = 0; i < 10; i++) res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);

        for (int i = 0; i < 25; i++) {
            res = dmc.tick(Direction.LEFT);
            res = dmc.tick(Direction.RIGHT);
        }
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());
        assertEquals(0, TestUtils.getEntities(res, "mercenary").size());
    }
}
