package org.qatracker.util;

// TestConfig — набор констант для всего приложения.
// static final: принадлежит классу (не объекту) и не меняется.
// Соглашение: UPPER_SNAKE_CASE для констант.
public class TestConfig {

    private TestConfig() {} // utility-класс

    // Допустимые статусы тест-кейса
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_PASSED  = "PASSED";
    public static final String STATUS_FAILED  = "FAILED";
    public static final String STATUS_BLOCKED = "BLOCKED";

    // Допустимые приоритеты
    public static final String PRIORITY_LOW      = "LOW";
    public static final String PRIORITY_MEDIUM   = "MEDIUM";
    public static final String PRIORITY_HIGH     = "HIGH";
    public static final String PRIORITY_CRITICAL = "CRITICAL";

    // Настройки приложения
    public static final String DATA_FILE_PATH   = "data/testcases.csv";
    public static final int    MAX_TITLE_LENGTH = 200;
}
