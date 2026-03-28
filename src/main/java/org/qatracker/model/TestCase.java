package org.qatracker.model;

import org.qatracker.util.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

// TestCase — основная сущность нашего трекера.
// Представляет один тест-кейс: что тестируем, каков статус, кто отвечает.
public class TestCase {
    private static final Logger log = LoggerFactory.getLogger(TestCase.class);
    private final int      id;
    private String         title;
    private Status         status;    // было: String status
    private Priority       priority;  // было: String priority
    private String         assignedTo;

    // Новый конструктор — принимает enum
    public TestCase(String title, Priority priority) {
        this.id       = IdGenerator.nextTestCaseId();
        setTitle(title);
        this.priority = priority;
        this.status   = Status.PENDING;
    }

    // Конструктор для загрузки CSV — парсим строки в enum
    public TestCase(int id, String title, String status, String priority, String assignedTo) {
        this.id         = id;
        setTitle(title);
        this.status     = Status.fromString(status);     // "PASSED" → Status.PASSED
        this.priority   = Priority.fromString(priority); // "HIGH" → Priority.HIGH
        this.assignedTo = assignedTo;
    }

    public TestCase(int id, String title, Status status, Priority priority) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.priority = priority;
    }


    // Геттеры возвращают enum

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getAssignedTo() {
        return assignedTo;
    }
    public Status   getStatus()   { return status; }
    public Priority getPriority() { return priority; }

    // Сеттеры принимают enum — опечатка "CRITIKAL" не пройдёт компиляцию
    public void setStatus(Status status)      { this.status   = status; }
    public void setPriority(Priority priority){ this.priority = priority; }

    // Предикаты — сравниваем через == (для enum это правильно!)
    public boolean isPassed()  { return status == Status.PASSED; }
    public boolean isFailed()  { return status == Status.FAILED; }
    public boolean isPending() { return status == Status.PENDING; }

    @Override
    public String toString() {
        return String.format("TestCase{id=%d, title='%s', status=%s[%s], priority=%s[%s]}",
                id, title,
                status.getLabel(), status.getIcon(),
                priority.getLabel(), priority.getWeight());
    }
//    // ── Конструкторы (занятие 1) ─────────────────────────────────────────────
//
//    public TestCase(int id, String title, String status, String priority) {
//        this.id = id;
//        this.title = title;
//        this.status = status;
//        this.priority = priority;
//    }
//
//    // Основной конструктор: id и title обязательны, остальное — по умолчанию
//    public TestCase(int id, String title) {
//        this.id       = id;
//        setTitle(title);             // через сеттер — валидация работает с самого начала
//        this.status   = "PENDING";   // новый тест всегда начинает как PENDING
//        this.priority = "MEDIUM";    // приоритет по умолчанию
//    }
//
//    public TestCase(String title, String status) {
//        this.id     = IdGenerator.nextTestCaseId();
//        this.title  = title;
//        this.status = status;
//    }
//
//    // Расширенный конструктор: задаём приоритет сразу
//    // this(id, title) — вызов первого конструктора (избегаем дублирования кода)
//    public TestCase(int id, String title, String priority) {
//        this(id, title);             // делегируем основному конструктору
//        this.priority = priority;
//    }
//
//    // Полный конструктор: все поля
//    public TestCase(int id, String title, String status, String priority, String assignedTo) {
//        this.id         = id;
//        setTitle(title);
//        setStatus(status);
//        setPriority(priority);
//        this.assignedTo = assignedTo;
//    }
//
//    // Конструктор копирования: создать копию существующего TestCase
//    public TestCase(TestCase other) {
//        this(other.id, other.title, other.status, other.priority, other.assignedTo);
//    }
//
//    // ── Геттеры: только чтение, без изменений ────────────────────────────────────
//    public int    getId()         { return id; }
//    public String getTitle()      { return title; }
//    public String getStatus()     { return status; }
//    public String getPriority()   { return priority; }
//    public String getAssignedTo() { return assignedTo; }

// ── Сеттеры с валидацией ─────────────────────────────────────────────────────

    public void setTitle(String title) {
        // Валидация: title не может быть пустым
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be blank");
        }
        this.title = title.trim(); // убираем лишние пробелы
    }

//    public void setStatus(String status) {
//        // Валидация: только допустимые значения статуса
//        if (!isValidStatus(status)) {
//            throw new IllegalArgumentException(
//                    "Invalid status: '" + status + "'. Allowed: PENDING, PASSED, FAILED, BLOCKED"
//            );
//        }
//        this.status = status;
//    }
//
//    public void setPriority(String priority) {
//        if (!isValidPriority(priority)) {
//            throw new IllegalArgumentException(
//                    "Invalid priority: '" + priority + "'. Allowed: LOW, MEDIUM, HIGH, CRITICAL"
//            );
//        }
//        this.priority = priority;
//    }

    public void setAssignedTo(String assignedTo) {
        // assignedTo может быть null (тест не назначен никому)
        this.assignedTo = assignedTo;
    }

    // toString() — читаемое строковое представление объекта.
    // Вызывается автоматически при System.out.println(tc) и в логах.
    // Без этого метода вы увидите бесполезное "TestCase@7852e922"
//    @Override
//    public String toString() {
//        return String.format(
//                "TestCase{id=%d, title='%s', status=%s, priority=%s, assignedTo=%s}",
//                id, title, status, priority,
//                assignedTo != null ? assignedTo : "unassigned"
//        );
//    }


    // equals() — сравнение СОДЕРЖИМОГО двух объектов.
    // Без этого метода tc1.equals(tc2) сравнивает АДРЕСА в памяти (как ==),
    // а не фактическое содержимое. Это ломает Set и Map.
    @Override
    public boolean equals(Object obj) {
        // Шаг 1: тот же объект — сразу true (оптимизация)
        if (this == obj) return true;

        // Шаг 2: null или другой тип — false
        if (obj == null || getClass() != obj.getClass()) return false;

        // Шаг 3: приводим тип и сравниваем поля
        TestCase other = (TestCase) obj;

        // Два TestCase равны если у них одинаковый id и title.
        // status и priority не включаем — они могут меняться, не нарушая "идентичность" теста.
        return this.id == other.id
                && java.util.Objects.equals(this.title, other.title);
    }

    // hashCode() — числовой код для использования в HashMap и HashSet.
    // ПРАВИЛО: если equals() возвращает true, hashCode() ОБЯЗАН вернуть одинаковое число.
    // Если нарушить — объекты "теряются" в HashSet и HashMap.
    // Objects.hash() — удобный способ вычислить хеш из нескольких полей.
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, title);
        // Те же поля что и в equals() — это обязательное правило!
    }

    // ── Вспомогательные методы ───────────────────────────────────────────────────

    // Удобные методы-предикаты — проверка статуса без сравнения строк
//    public boolean isPassed()  { return "PASSED".equals(status); }
//    public boolean isFailed()  { return "FAILED".equals(status); }
//    public boolean isPending() { return "PENDING".equals(status); }
//    public boolean isBlocked() { return "BLOCKED".equals(status); }

    // Приватные методы валидации — используются только внутри класса
    private boolean isValidStatus(String s) {
        return s != null && (
                s.equals("PENDING") || s.equals("PASSED") ||
                        s.equals("FAILED")  || s.equals("BLOCKED")
        );
    }

    private boolean isValidPriority(String p) {
        return p != null && (
                p.equals("LOW")    || p.equals("MEDIUM") ||
                        p.equals("HIGH")   || p.equals("CRITICAL")
        );
    }
}
