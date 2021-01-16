package dev.kscott.quantum.location.locator;

/**
 * A YLocator that returns the highest Y.
 */
public class HighestPossibleYLocator extends RangeYLocator {

    /**
     * Constructs HighestPossibleYLocator.
     */
    public HighestPossibleYLocator() {
        // 254 so the RangeYLocator can check 254 & 255
        // if it was 255 it would try checking 255, and 255+1 (256) and error out since that's above max height
        // same reason why we're checking down to 1 instead of 0
        super(254, 1);
    }
}
