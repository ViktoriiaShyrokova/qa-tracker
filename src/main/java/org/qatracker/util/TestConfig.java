package org.qatracker.util;

// TestConfig — набор констант для всего приложения.
// static final: принадлежит классу (не объекту) и не меняется.
// Соглашение: UPPER_SNAKE_CASE для констант.
public class TestConfig {

    private TestConfig() {} // utility-класс


    // Настройки приложения
    public static final String DATA_FILE_PATH   = "data/testcases.csv";
    public static final int    MAX_TITLE_LENGTH = 200;
    public static final int    MIN_TITLE_LENGTH = 3;
}
