package org.labs;

public class Main {

    private static final int DEFAULT_PROGRAMMERS_COUNT = 7;
    private static final int DEFAULT_FOOD_COUNT = 1_000_000;
    private static final int DEFAULT_WAITERS_COUNT = 2;

    public static void main(String[] args) {
        DiningProgrammers task = new DiningProgrammers(DEFAULT_PROGRAMMERS_COUNT, DEFAULT_FOOD_COUNT, DEFAULT_WAITERS_COUNT);
        int[] eatenFoods = task.runTask();

        for (int i = 0; i < DEFAULT_PROGRAMMERS_COUNT; ++i) {
            System.out.printf("Programmer %d ate %d food units\n", i + 1, eatenFoods[i]);
        }
    }
}