package dungeonmania.entities.logic;

public class OrRule implements LogicRule {
    private static final int MINIMUM_ACTIVATED = 1;

    public boolean isSatisfied(int subCount, int activatedCount) {
        return activatedCount >= MINIMUM_ACTIVATED;
    }
}
