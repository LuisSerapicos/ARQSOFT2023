package com.isep.acme.utils;

import java.util.Random;

public class RandomLongGenerator {
    private static final Random random = new Random();

    public static Long generateRandomLong() {
        return Math.abs(random.nextLong());
    }
}
