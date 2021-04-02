package eu.greev.jumpandrun.utils;

import java.util.SplittableRandom;

public class Maths {
    private Maths() {
        throw new IllegalStateException("Utility class");
    }

    // Generates a random integer between 'min' (inclusive) and 'max' (inclusive).
    public static int randInt(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        return new SplittableRandom().nextInt((max - min) + 1) + min;
    }
}
