package eu.greev.jumpandrun.classes;

import java.util.Random;

public class Maths {
    // Generates a random integer between 'min' (inclusive) and 'max' (inclusive).
    public int randInt(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }
}
