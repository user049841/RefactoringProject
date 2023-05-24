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

public class LogicSwitchesTest {

    @Test
    @Tag("32-1")
    @DisplayName("Test entities can stand on wires")
    public void standOnWire() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitchesTest_standOnWire", "c_logicSwitchesTest_standOnWire");
        res = dmc.tick(Direction.RIGHT);

        assertEquals(new Position(3, 1), TestUtils.getEntityPos(res, "mercenary"));
        assertEquals(new Position(2, 1), TestUtils.getPlayerPos(res));
    }

    @Test
    @Tag("32-2")
    @DisplayName("Test default state of a switch door is closed")
    public void switchDoorClosed() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitchesTest_switchDoorClosed",
                "c_logicSwitchesTest_switchDoorClosed");
        assertEquals(0, TestUtils.getEntities(res, "switch_door_open").size());
        // Check none of the switch doors can be walked through
        for (int i = 0; i < 4; i++) {
            Position pos = TestUtils.getPlayerPos(res);
            dmc.tick(Direction.DOWN);
            assertEquals(pos, TestUtils.getPlayerPos(res));
            dmc.tick(Direction.RIGHT);
        }
    }

    @Test
    @Tag("32-3")
    @DisplayName("Test the default state of a light bulb is off")
    public void lightBulbOff() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitchesTest_lightBulbOff", "c_logicSwitchesTest_lightBulbOff");
        assertEquals(4, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
    }

    @Test
    @Tag("32-4")
    @DisplayName("Test a switch cardinally adjacent to an OR light works")
    public void directLight() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitchesTest_directLight", "c_logicSwitchesTest_directLight");
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
    }

    @Test
    @Tag("32-5")
    @DisplayName("Test a switch cardinally adjacent to an OR door works")
    public void directDoor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitchesTest_directDoor", "c_logicSwitchesTest_directDoor");
        assertEquals(0, TestUtils.getEntities(res, "switch_door_open").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "switch_door_open").size());
        // Check the player can walk through the door
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        assertEquals(new Position(4, 1), TestUtils.getPlayerPos(res));
    }

    @Test
    @Tag("32-6")
    @DisplayName("Test a simple circuit works")
    public void simpleCircuit() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitchesTest_simpleCircuit", "c_logicSwitchesTest_simpleCircuit");
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_off").size());

    }

    @Test
    @Tag("32-7")
    @DisplayName("Test a switch door does not conduct")
    public void switchDoorDoesNotConduct() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitchesTest_switchDoorDoesNotConduct",
                "c_logicSwitchesTest_switchDoorDoesNotConduct");
        assertEquals(0, TestUtils.getEntities(res, "switch_door_open").size());
        res = dmc.tick(Direction.RIGHT);
        // Check the player cannot walk through the door
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        Position pos = TestUtils.getPlayerPos(res);
        res = dmc.tick(Direction.DOWN);
        assertEquals(pos, TestUtils.getPlayerPos(res));
    }

    @Test
    @Tag("32-8")
    @DisplayName("Test a light bulb does not conduct")
    public void lightBulbDoesNotConduct() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitchesTest_lightBulbDoesNotConduct",
                "c_logicSwitchesTest_lightBulbDoesNotConduct");
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(0, TestUtils.getEntities(res, "switch_door_open").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(0, TestUtils.getEntities(res, "switch_door_open").size());
        // Check the player cannot walk through the door
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        Position pos = TestUtils.getPlayerPos(res);
        res = dmc.tick(Direction.DOWN);
        assertEquals(pos, TestUtils.getPlayerPos(res));
    }

    @Test
    @Tag("32-9")
    @DisplayName("Test an AND entity does not turn on with one adjacent conductor")
    public void andCircuitOneAdjacent() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitchesTest_andCircuitOneAdjacent",
                "c_logicSwitchesTest_andCircuitOneAdjacent");
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
    }

    @Test
    @Tag("32-10")
    @DisplayName("Test an OR entity turns on with one adjacent conductor")
    public void orCircuitOneAdjacent() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitchesTest_orCircuitOneAdjacent",
                "c_logicSwitchesTest_orCircuitOneAdjacent");
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_off").size());
    }

    @Test
    @Tag("32-11")
    @DisplayName("Test an XOR entity turns on with one adjacent conductor")
    public void xorCircuitOneAdjacent() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitchesTest_orCircuitOneAdjacent",
                "c_logicSwitchesTest_orCircuitOneAdjacent");
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_off").size());
    }

    @Test
    @Tag("32-12")
    @DisplayName("Test a CO_AND entity does not turn on with one adjacent conductor")
    public void coAndCircuitOneAdjacent() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitchesTest_coAndCircuitOneAdjacent",
                "c_logicSwitchesTest_coAndCircuitOneAdjacent");
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
    }

    @Test
    @Tag("32-13")
    @DisplayName("Test multiple adjacent activated conductors do not activate XOR")
    public void xorCircuitTwoAdjacent() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitchesTest_xorCircuitTwoAdjacent",
                "c_logicSwitchesTest_xorCircuitTwoAdjacent");
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
    }

    @Test
    @Tag("32-14")
    @DisplayName("Test a CO_AND entity does not turn on when powered during different ticks")
    public void coAndDifferentTicks() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitchesTest_coAndDifferentTicks",
                "c_logicSwitchesTest_coAndDifferentTicks");
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
    }

    @Test
    @Tag("32-15")
    @DisplayName("Test an AND can be activated with 2 adjacent conductors")
    public void powerAnd() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitchesTest_powerAnd", "c_logicSwitchesTest_powerAnd");
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
    }

    @Test
    @Tag("32-16")
    @DisplayName("Test an AND cannot be activated with 2 out of 3 adjacent conductors")
    public void semipowerAnd() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitchesTest_semipowerAnd", "c_logicSwitchesTest_semipowerAnd");
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
    }

    @Test
    @Tag("32-17")
    @DisplayName("Test partially unpowering an AND deactivates it")
    public void unpowerAnd() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitchesTest_unpowerAnd", "c_logicSwitchesTest_unpowerAnd");
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
        res = dmc.tick(Direction.LEFT);
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
    }

    @Test
    @Tag("32-18")
    @DisplayName("Test a CO_AND activates when all adjacent conductors are simultaneously activated")
    public void powerCoAnd() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitchesTest_powerCoAnd", "c_logicSwitchesTest_powerCoAnd");
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
    }

    @Test
    @Tag("32-19")
    @DisplayName("Test partially unpowering a CO_AND deactivates it")
    public void unpowerCoAnd() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitchesTest_unpowerCoAnd", "c_logicSwitchesTest_unpowerCoAnd");
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
    }

    @Test
    @Tag("32-20")
    @DisplayName("Test updating the tick of a wire activating a CO_AND that was already powered retains power")
    public void tickUpdateCoAnd() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitchesTest_tickUpdateCoAnd",
                "c_logicSwitchesTest_tickUpdateCoAnd");
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());

        // activate switch that powers the coand
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());

        // activate switch that gives 'additional activations' to the wire
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());

        // activate switch that gives 'additional activations' to the wire
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
        // deactivate initial switch that powered the coand
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());

    }


    @Test
    @Tag("32-21")
    @DisplayName("Test partially unpowering an OR does not deactivate it")
    public void unpowerOr() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitchesTest_unpowerOr", "c_logicSwitchesTest_unpowerOr");
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
    }

    @Test
    @Tag("32-22")
    @DisplayName("Test partially unpowering an XOR can activate it")
    public void unpowerXor() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitchesTest_unpowerXor", "c_logicSwitchesTest_unpowerXor");
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
    }

    @Test
    @Tag("32-23")
    @DisplayName("Test a complex circuit works as intended")
    public void complexCircuit() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_logicSwitchesTest_complexCircuit", "c_logicSwitchesTest_complexCircuit");
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());
        Position coAndPos = new Position(2, 4);
        Position andPos = new Position(4, 4);
        Position orPos = new Position(5, 4);
        Position xorPos = new Position(6, 1);

        // activate lowermost switch
        res = dmc.tick(Direction.DOWN);
        assertTrue(TestUtils.entityAtPosition(res, "light_bulb_on", coAndPos));
        assertTrue(TestUtils.entityAtPosition(res, "light_bulb_on", andPos));
        assertTrue(TestUtils.entityAtPosition(res, "light_bulb_on", orPos));
        assertFalse(TestUtils.entityAtPosition(res, "light_bulb_on", xorPos));

        // deactivate switch
        res = dmc.tick(Direction.DOWN);
        assertEquals(0, TestUtils.getEntities(res, "light_bulb_on").size());

        // activate the centre switch
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        assertTrue(TestUtils.entityAtPosition(res, "light_bulb_on", coAndPos));
        assertTrue(TestUtils.entityAtPosition(res, "light_bulb_on", andPos));
        assertTrue(TestUtils.entityAtPosition(res, "light_bulb_on", orPos));
        assertTrue(TestUtils.entityAtPosition(res, "light_bulb_on", xorPos));

        // activate top most switch
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertTrue(TestUtils.entityAtPosition(res, "light_bulb_on", coAndPos));
        assertTrue(TestUtils.entityAtPosition(res, "light_bulb_on", andPos));
        assertTrue(TestUtils.entityAtPosition(res, "light_bulb_on", orPos));
        assertTrue(TestUtils.entityAtPosition(res, "light_bulb_on", xorPos));

        // deactivate centre switch
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
        assertTrue(TestUtils.entityAtPosition(res, "light_bulb_on", xorPos));
    }
}
