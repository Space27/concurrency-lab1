package org.labs;

import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DiningProgrammers {

    private final int programmersCount;
    private final int waitersCount;
    private final int foodCount;

    public DiningProgrammers(int programmersCount, int foodCount, int waitersCount) {
        if (programmersCount < 2) throw new IllegalArgumentException("Should be at least 2 programmers");
        if (foodCount < 0) throw new IllegalArgumentException("Available food count should be non-negative");
        if (waitersCount < 1) throw new IllegalArgumentException("Should be at least 1 waiter");

        this.programmersCount = programmersCount;
        this.foodCount = foodCount;
        this.waitersCount = waitersCount;
    }

    public int[] runTask() {
        ReentrantLock[] spoons = Stream.generate(ReentrantLock::new)
                .limit(programmersCount)
                .toArray(ReentrantLock[]::new);
        try (Waiters waiters = new Waiters(waitersCount, foodCount)) {
            Programmer[] programmers = IntStream.range(0, programmersCount)
                    .mapToObj(i -> {
                        int firstSpoon = Math.min(i, (i + 1) % programmersCount);
                        int secondSpoon = Math.max(i, (i + 1) % programmersCount);
                        return new Programmer(spoons[firstSpoon], spoons[secondSpoon], waiters);
                    }).toArray(Programmer[]::new);

            Arrays.stream(programmers).forEach(Programmer::start);
            for (Programmer programmer : programmers) {
                try {
                    programmer.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            return Arrays.stream(programmers).mapToInt(Programmer::getEaten).toArray();
        }
    }
}
