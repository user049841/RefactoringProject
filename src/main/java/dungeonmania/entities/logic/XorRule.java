package dungeonmania.entities.logic;

public class XorRule implements LogicRule {
    private static final int XOR_ACTIVATION = 1;

    public boolean isSatisfied(int subCount, int activatedCount) {
        return activatedCount == XOR_ACTIVATION;
    }
}
