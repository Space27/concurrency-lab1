package org.labs;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class Waiters implements AutoCloseable {

    private final ExecutorService waiters;
    private final AtomicInteger leftoverFood;

    public Waiters(int waitersCount, int foodCount) {
        waiters = Executors.newFixedThreadPool(waitersCount);
        leftoverFood = new AtomicInteger(foodCount);
    }

    public Future<Boolean> askFood() {
        return waiters.submit(this::takeFood);
    }

    private boolean takeFood() {
        int previousFood = leftoverFood.getAndUpdate(f -> f > 0 ? f - 1 : 0);

        return previousFood > 0;
    }

    @Override
    public void close() {
        waiters.shutdown();
    }
}
