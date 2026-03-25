package org.qatracker.util;

// IdGenerator — автоматическая генерация уникальных ID.
// Использует static-счётчик: один счётчик на весь класс, а не на каждый объект.
public class IdGenerator {

    // static поле — одно значение для всей программы
    // (а не отдельное для каждого объекта)
    private static int nextTestCaseId  = 1;
    private static int nextBugReportId = 1;

    // Приватный конструктор — запрещаем создавать объекты этого класса
    // IdGenerator — это utility-класс, все методы static
    private IdGenerator() {}

    // Каждый вызов возвращает следующий уникальный ID
    public static int nextTestCaseId()  { return nextTestCaseId++; }
    public static int nextBugReportId() { return nextBugReportId++; }

    // Текущие значения (без увеличения) — для информации
    public static int currentTestCaseId()  { return nextTestCaseId; }
    public static int currentBugReportId() { return nextBugReportId; }

    // Сброс счётчиков — используется в тестах (занятие 16)
    // Без сброса тесты зависят от порядка запуска
    public static void resetAll() {
        nextTestCaseId  = 1;
        nextBugReportId = 1;
    }
}
