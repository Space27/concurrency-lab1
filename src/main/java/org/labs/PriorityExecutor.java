package org.labs;

import java.util.concurrent.*;

public class PriorityExecutor extends ThreadPoolExecutor {

    public PriorityExecutor(int threadsCount, int queueSize) {
        super(threadsCount, threadsCount, 0L, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<>(queueSize));
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new ComparableFutureTask<>(callable);
    }

    private static class ComparableFutureTask<V> extends FutureTask<V> implements Comparable<ComparableFutureTask<V>> {

        private final Callable<V> callable;

        public ComparableFutureTask(Callable<V> callable) {
            super(callable);
            this.callable = callable;
        }

        @Override
        @SuppressWarnings("unchecked")
        public int compareTo(ComparableFutureTask<V> o) {
            if (o == null) throw new NullPointerException();
            if (this == o) return 0;

            if (this.callable instanceof Comparable && o.callable instanceof Comparable) {
                return ((Comparable<Object>) this.callable).compareTo(o.callable);
            }
            return 0;
        }
    }
}