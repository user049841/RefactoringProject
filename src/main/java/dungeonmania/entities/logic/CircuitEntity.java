package dungeonmania.entities.logic;

import java.io.Serializable;

public interface CircuitEntity extends Serializable {
    public void notify(boolean adding, int activatedCount);
}
