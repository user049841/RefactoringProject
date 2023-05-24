package dungeonmania.entities.logic;

public class AndRule implements LogicRule {
    private static final int MINIMUM_ACTIVATED = 2;
    public boolean isSatisfied(int subCount, int activatedCount) {
        return activatedCount == subCount && activatedCount >= MINIMUM_ACTIVATED;
    }
}
