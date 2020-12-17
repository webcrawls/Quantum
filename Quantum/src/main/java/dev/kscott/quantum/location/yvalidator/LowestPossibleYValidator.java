package dev.kscott.quantum.location.yvalidator;

/**
 * A YValidator that searches for the lowest possible location
 */
public class LowestPossibleYValidator extends RangeYValidator {

    public LowestPossibleYValidator() {
        super(0, 255);
    }
}
