package org.labs;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class Waiters implements AutoCloseable {

    private final static int FOOD_TAKING_DURATION_MICROSECONDS = 5;

    private final ExecutorService waiters;
    private final AtomicInteger leftoverFood;

    public Waiters(int waitersCount, int foodCount, int programmersCount) {
        waiters = new PriorityExecutor(waitersCount, programmersCount);
        leftoverFood = new AtomicInteger(foodCount);
    }

    public boolean askFood(int eaten) {
        try {
            return waiters.submit(new PriorityTask(eaten)).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } catch (ExecutionException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private boolean takeFood() {
        TimerUtils.waitMicroseconds(FOOD_TAKING_DURATION_MICROSECONDS);
        int previousFood = leftoverFood.getAndUpdate(f -> Math.max(0, f - 1));

        return previousFood > 0;
    }

    @Override
    public void close() {
        waiters.shutdown();
    }

    @RequiredArgsConstructor
    private class PriorityTask implements Comparable<PriorityTask>, Callable<Boolean> {
        private final int priority;

        @Override
        public int compareTo(PriorityTask other) {
            return Integer.compare(this.priority, other.priority);
        }

        @Override
        public Boolean call() {
            return takeFood();
        }
    }
}
