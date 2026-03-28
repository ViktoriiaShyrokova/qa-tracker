package org.qatracker.model;

public enum Priority {
    LOW     (4, "Low"),
    MEDIUM  (3, "Medium"),
    HIGH    (2, "High"),
    CRITICAL(1, "Critical"); // 1 = наивысший (CRITICAL первый при сортировке по возрастанию)

    private final int    weight; // для сортировки — меньше = важнее
    private final String label;  // читаемое название

    // Конструктор enum — всегда private (явно или неявно)
    Priority(int weight, String label) {
        this.weight = weight;
        this.label  = label;
    }

    public int    getWeight() { return weight; }
    public String getLabel()  { return label; }

    // Метод в enum — как в обычном классе
    public boolean isMoreUrgentThan(Priority other) {
        return this.weight < other.weight;
    }

    // Безопасный парсинг: "critical" → Priority.CRITICAL (нечувствительно к регистру)
    // Нужен потому что Status.valueOf("critical") бросит IllegalArgumentException
    public static Priority fromString(String value) {
        for (Priority p : values()) {
            if (p.name().equalsIgnoreCase(value) || p.label.equalsIgnoreCase(value))
                return p;
        }
        throw new IllegalArgumentException("Unknown priority: " + value);
    }
}