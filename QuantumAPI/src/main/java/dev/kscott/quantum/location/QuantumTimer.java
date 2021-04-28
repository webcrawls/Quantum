package dev.kscott.quantum.location;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

// TODO: make this a general stats class which hold values per-ruleset search and stuff
public class QuantumTimer {

    /**
     * A List which stores all the previous search times.
     */
    private final @NonNull List<Long> pastTimes;

    /**
     * Constructs QuantumTimer.
     */
    public QuantumTimer() {
        this.pastTimes = new ArrayList<>();
    }

    /**
     * Adds a time to the search time array.
     *
     * @param time amount of time spent searching for a location, in ms.
     */
    public void addTime(final long time) {
        pastTimes.add(time);
    }

    /**
     * Returns the average search time (sum of all search times / search time array size).
     *
     * @return averaged long, in ms.
     */
    public long getAverageTime() {
        try {
            return getTotalTime() / pastTimes.size();
        } catch (ArithmeticException e) {
            return -1;
        }
    }

    /**
     * Returns the total amount of time spent
     *
     * @return total time in ms.
     */
    public long getTotalTime() {
        long totalTime = 0;

        for (long time : pastTimes) {
            totalTime += time;
        }

        return totalTime;
    }

    /**
     * Returns the amount of times in the search time array.
     *
     * @return length of search time array.
     */
    public int getTotalSearches() {
        return this.pastTimes.size();
    }

}
