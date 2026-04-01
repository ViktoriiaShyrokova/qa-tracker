package org.qatracker.util;

public class MathUtils {
    public static int clamp(int value, int min, int max) {
        if (min > max) throw new IllegalArgumentException("min > max");
        return Math.max(min, Math.min(max, value));
    }

    public static double average(int... values) {
        if (values == null || values.length == 0)
            throw new IllegalArgumentException("No values provided");
        int sum = 0;
        for (int v : values) sum += v;
        return (double) sum / values.length;
    }

    public static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
}