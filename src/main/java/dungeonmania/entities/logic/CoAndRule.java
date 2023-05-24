package dungeonmania.entities.logic;

public class CoAndRule implements LogicRule {
    private static final int MINIMUM_ACTIVATED = 2;
    // Helper method for testing if a Co_And entity is activated.
    public boolean isSatisfied(int subCount, int activatedCount) {
        return activatedCount == subCount && activatedCount >= MINIMUM_ACTIVATED;
    }

    public boolean isActivated(int subCount, int activatedCount, int activatedThisTickCount,
            boolean activeOnPriorTick) {
        return isSatisfied(subCount, activatedCount) && (activeOnPriorTick || activatedThisTickCount == subCount);
    }
}
