package com.xyzbank.utils;

import java.util.concurrent.ThreadLocalRandom;

/** Generates unique, valid test data so tests never depend on each other. */
public final class TestDataGenerator {

    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";

    private TestDataGenerator() {
    }

    /** Alphabetic-only name (acceptance criterion), e.g. "Qatuvm". */
    public static String name() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toUpperCase(LETTERS.charAt(random.nextInt(LETTERS.length()))));
        for (int i = 0; i < 6; i++) {
            sb.append(LETTERS.charAt(random.nextInt(LETTERS.length())));
        }
        return sb.toString();
    }

    /** Numeric-only post code (acceptance criterion). */
    public static String postCode() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(10000, 99999));
    }
}
