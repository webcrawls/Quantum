package dev.kscott.quantum.location;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

// TODO: make this a general stats class which hold values per-ruleset search and stuff
public class QuantumTimer {

    private final @NonNull List<Long> pastTimes;

    public QuantumTimer() {
        this.pastTimes = new ArrayList<>();
    }

    public void addTime(final long time) {
        pastTimes.add(time);
    }

    public long getAverageTime() {
        long time = pastTimes.stream().mapToLong(timeAsLong -> timeAsLong).sum();

        try {
            return time / pastTimes.size();
        } catch (ArithmeticException e) {
            return -1;
        }
    }

    public int getTotalSearches() {
        return this.pastTimes.size();
    }

}
