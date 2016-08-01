package com.e16din.goeurotest;

public final class Utils {

    private Utils() {
    }

    public static String addZero(int value) {
        if (value < 10) {
            return "0" + Integer.toString(value);
        }

        return Integer.toString(value);
    }
}
