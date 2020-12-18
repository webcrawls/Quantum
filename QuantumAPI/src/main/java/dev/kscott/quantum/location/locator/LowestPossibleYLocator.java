package dev.kscott.quantum.location.locator;

/**
 * A YLocator that searches for the lowest possible location
 */
public class LowestPossibleYLocator extends RangeYLocator {

    public LowestPossibleYLocator() {
        super(0, 255);
    }
}
