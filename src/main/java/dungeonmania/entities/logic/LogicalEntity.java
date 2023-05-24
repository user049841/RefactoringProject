package dungeonmania.entities.logic;

import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public abstract class LogicalEntity extends Entity implements CircuitEntity {
    private LogicRule logicRule;
    private boolean activeOnPriorTick = false;
    private int activatedCount;
    private int activatedThisTickCount;
    private int subCount = 0;
    private static final int UNACTIVATED_COUNT = 0;
    private static final int NEWLY_ACTIVATED_COUNT = 1;

    public LogicalEntity(Position position, LogicRule logicRule) {
        super(position);
        this.logicRule = logicRule;
    }

    public boolean isActivated() {
        if (logicRule instanceof CoAndRule) {
            return ((CoAndRule) logicRule).isActivated(subCount, activatedCount, activatedThisTickCount,
                    activeOnPriorTick);
        }
        return logicRule.isSatisfied(subCount, activatedCount);
    }

    @Override
    public void notify(boolean adding, int activatedCount) {
        if (adding && activatedCount == NEWLY_ACTIVATED_COUNT) {
            this.activatedCount++;
            activatedThisTickCount++;
        } else if (activatedCount == UNACTIVATED_COUNT) {
            this.activatedCount--;
        }
    }

    public void setActiveOnPriorTick(boolean activeOnPriorTick) {
        this.activeOnPriorTick = activeOnPriorTick;
    }

    public void addSubCount(int count) {
        subCount += count;
    }

    public void resetActivatedThisTickCount() {
        activatedThisTickCount = 0;
    }
}
