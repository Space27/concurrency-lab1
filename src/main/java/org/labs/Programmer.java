package org.labs;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantLock;

public class Programmer extends Thread {

    private final ReentrantLock firstSpoon;
    private final ReentrantLock secondSpoon;
    private final Waiters waiters;
    private int eaten;

    public Programmer(ReentrantLock firstSpoon, ReentrantLock secondSpoon, Waiters waiters) {
        this.firstSpoon = firstSpoon;
        this.secondSpoon = secondSpoon;
        this.waiters = waiters;
        this.eaten = 0;
    }

    @Override
    public void run() {
        try {
            while (waiters.askFood().get()) {
                takeSpoons();
                try {
                    ++eaten;
                } finally {
                    putSpoons();
                }

                Thread.yield();
            }
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getEaten() {
        return eaten;
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
