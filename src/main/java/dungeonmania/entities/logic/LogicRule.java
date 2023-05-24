package dungeonmania.entities.logic;

import java.io.Serializable;

public interface LogicRule extends Serializable {
    public boolean isSatisfied(int subCount, int activatedCount);
}
