package org.qatracker.model;
public enum Status {
    PENDING ("Pending"),
    PASSED  ("Passed"),
    FAILED  ("Failed"),
    BLOCKED ("Blocked");

    private final String label;

    Status(String label) { this.label = label; }

    public String getLabel() { return label; }

    // Switch expression прямо в методе enum
    public boolean isFinal() {
        return switch (this) {
            case PASSED, FAILED   -> true;
            case PENDING, BLOCKED -> false;
            // default не нужен — все значения enum покрыты (exhaustiveness)!
        };
    }

    public String getIcon() {
        return switch (this) {
            case PASSED  -> "✓";
            case FAILED  -> "✗";
            case PENDING -> "⏳";
            case BLOCKED -> "⛔";
        };
    }

    // ANSI-цвет для консольного вывода
    public String getConsoleColor() {
        return switch (this) {
            case PASSED  -> "\u001B[32m"; // зелёный
            case FAILED  -> "\u001B[31m"; // красный
            case BLOCKED -> "\u001B[33m"; // жёлтый
            case PENDING -> "\u001B[0m";  // обычный
        };
    }

    public static Status fromString(String value) {
        for (Status s : values()) {
            if (s.name().equalsIgnoreCase(value)) return s;
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }
}