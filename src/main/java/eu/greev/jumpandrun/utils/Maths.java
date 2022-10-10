package eu.greev.jumpandrun.utils;

import java.util.Random;

public class Maths {
    private Maths() {
        throw new IllegalStateException("Utility class");
    }

    private static final Random RANDOM = new Random();

    /**
     * @param min Min value (inclusive)
     * @param max Max value (inclusive)
     * @return int Random generated number
     */
    public static int randInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be smaller than max!");
        }

        return RANDOM.nextInt((max - min) + 1) + min;
    }
}
