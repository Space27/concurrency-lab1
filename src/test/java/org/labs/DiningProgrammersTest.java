package org.labs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Timeout(value = 90, unit = TimeUnit.SECONDS)
@DisplayName("Тесты симуляции обеда программистов")
class DiningProgrammersTest {

    private static final int DEFAULT_PROGRAMMERS_COUNT = 7;
    private static final int DEFAULT_FOOD_COUNT = 1_000_000;
    private static final int DEFAULT_WAITERS_COUNT = 2;

    private static final double ALLOWED_DEVIATION = 0.02;

    private double getMinMaxDeviation(int programmersCount, int foodCount, int[] eatenFood) {
        if (foodCount == 0) return 0;
        int minMaxDifference = Arrays.stream(eatenFood).max().orElseThrow() - Arrays.stream(eatenFood).min().orElseThrow();
        double mean = (double) foodCount / programmersCount;
        return minMaxDifference / mean;
    }

    @Test
    @DisplayName("Запуск с стандартными значениями")
    void runTask_shouldCompleteWithDefaultParametersWithSmallDeviation() {
        DiningProgrammers task = new DiningProgrammers(DEFAULT_PROGRAMMERS_COUNT, DEFAULT_FOOD_COUNT, DEFAULT_WAITERS_COUNT);

        int[] eatenFood = task.runTask();

        assertThat(Arrays.stream(eatenFood).sum())
                .isEqualTo(DEFAULT_FOOD_COUNT);
        assertThat(getMinMaxDeviation(DEFAULT_PROGRAMMERS_COUNT, DEFAULT_FOOD_COUNT, eatenFood))
                .isLessThan(ALLOWED_DEVIATION);
    }

    @ParameterizedTest(name = "Число программистов {0}, число еды {1} и число официантов {2}")
    @CsvSource({
            "16,1_000_000,2",
            "3,1_000_000,2",
            "7,2_000_000,2",
            "7,500_000,2",
            "7,1_000_000,7",
            "7,1_000_000,1",
    })
    @DisplayName("Задача с различными аргументами")
    void runTask_shouldCompleteWithSmallDeviation(int programmersCount, int foodCount, int waitersCount) {
        DiningProgrammers task = new DiningProgrammers(programmersCount, foodCount, waitersCount);

        int[] eatenFood = task.runTask();

        assertThat(Arrays.stream(eatenFood).sum())
                .isEqualTo(foodCount);
        assertThat(getMinMaxDeviation(programmersCount, foodCount, eatenFood))
                .isLessThan(ALLOWED_DEVIATION);
    }

    @ParameterizedTest(name = "Число программистов {0}, число еды {1} и число официантов {2}")
    @CsvSource({
            "2,1_000_000,2",
            "7,0,2",
            "7,1_000_000,1",
    })
    @DisplayName("Задача с граничными аргументами")
    void runTask_shouldCompleteWithSmallDeviationWithBoundaryParameters(int programmersCount, int foodCount, int waitersCount) {
        DiningProgrammers task = new DiningProgrammers(programmersCount, foodCount, waitersCount);

        int[] eatenFood = task.runTask();

        assertThat(Arrays.stream(eatenFood).sum())
                .isEqualTo(foodCount);
        assertThat(getMinMaxDeviation(programmersCount, foodCount, eatenFood))
                .isLessThan(ALLOWED_DEVIATION);
    }

    @ParameterizedTest(name = "Число программистов {0}, число еды {1} и число официантов {2}")
    @CsvSource({
            "1,1_000_000,2",
            "0,1_000_000,2",
            "7,-1,1",
            "7,1_000_000,0"
    })
    @DisplayName("Задача с невалидными аргументами")
    void diningProgrammers_shouldThrowExceptionWithInvalidParameters(int programmersCount, int foodCount, int waitersCount) {
        assertThatThrownBy(() -> new DiningProgrammers(programmersCount, foodCount, waitersCount))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
