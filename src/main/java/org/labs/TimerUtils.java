package org.labs;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TimerUtils {

    public static void waitMicroseconds(int micros) {
        long deadline = System.nanoTime() + micros * 1000L;
        while (System.nanoTime() < deadline) {
            Thread.onSpinWait();
        }
    }
}
