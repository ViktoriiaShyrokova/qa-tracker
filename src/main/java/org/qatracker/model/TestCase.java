package org.qatracker.model;

import org.qatracker.util.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

// TestCase — основная сущность нашего трекера.
// Представляет один тест-кейс: что тестируем, каков статус, кто отвечает.
public class TestCase implements Comparable<TestCase>{
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
        setTitle(title);
        this.status = status;
        this.priority = priority;
    }

    public static void resetIdCounter() {
        IdGenerator.resetAll();
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

    // Сеттеры принимают enum — опечатка "CRITICAL" не пройдёт компиляцию
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


// ── Сеттеры с валидацией ─────────────────────────────────────────────────────

    public void setTitle(String title) {
        // Валидация: title не может быть пустым
        if (title == null || title.isBlank() || title.trim().length() < 3)
            throw new IllegalArgumentException("Title cannot be blank");
        this.title = title.trim(); // убираем лишние пробелы
    }

    public void setAssignedTo(String assignedTo) {
        // assignedTo может быть null (тест не назначен никому)
        this.assignedTo = assignedTo;
    }


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
    @Override
    public int compareTo(TestCase other) {
        int byPriority = Integer.compare(
                this.priority.getWeight(),
                other.priority.getWeight()
        );
        return byPriority != 0
                ? byPriority
                : Integer.compare(this.id, other.id);
    }
}
