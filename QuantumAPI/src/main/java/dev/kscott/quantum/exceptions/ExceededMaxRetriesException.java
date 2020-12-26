package dev.kscott.quantum.exceptions;

public class ExceededMaxRetriesException extends Exception {

    public ExceededMaxRetriesException() {
        super("This location search exceeded the configured max retry count. Try raising it?");
    }

}
