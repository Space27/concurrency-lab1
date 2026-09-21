package org.labs;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class Programmer implements Supplier<Integer> {

    private final static int EATING_DURATION_MICROSECONDS = 7;
    private final static int DISCUSSING_DURATION_MICROSECONDS = 5;

    private final ReentrantLock firstSpoon;
    private final ReentrantLock secondSpoon;
    private final Waiters waiters;

    @Override
    public Integer get() {
        int eaten = 0;

        while (waiters.askFood(eaten)) {
            takeSpoons();
            try {
                eat();
                ++eaten;
            } finally {
                putSpoons();
            }

            discuss();
        }

        return eaten;
    }

    private void eat() {
        TimerUtils.waitMicroseconds(EATING_DURATION_MICROSECONDS);
    }

    private void discuss() {
        TimerUtils.waitMicroseconds(DISCUSSING_DURATION_MICROSECONDS);
    }

    private void takeSpoons() {
        firstSpoon.lock();
        secondSpoon.lock();
    }

    private void putSpoons() {
        secondSpoon.unlock();
        firstSpoon.unlock();
    }
}
