package org.qatracker.model;

// Интерфейс — контракт: любой класс, реализующий Reportable,
// обязуется предоставить метод getSummary().
public interface Reportable {

    // Абстрактный метод — каждая реализация пишет свой вариант
    String getSummary();

    // default метод — реализация по умолчанию.
    // Классы могут использовать её или переопределить.
    default void printReport() {
        System.out.println("=== Report ===");
        System.out.println(getSummary());
        System.out.println("==============");
    }
}
