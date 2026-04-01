package org.qatracker.model;

public class TestCaseBuilder {


    private String   title    = "Test Case";
    private Priority priority = Priority.MEDIUM;
    private Status   status   = Status.PENDING;
    private String   assignee = null;

    private TestCaseBuilder() {}  // приватный конструктор

    // ── Фабричные методы: описывают сценарий ────────────────────────────────
    public static TestCaseBuilder aTestCase()         { return new TestCaseBuilder(); }
    public static TestCaseBuilder aFailedTest()       { return new TestCaseBuilder().withStatus(Status.FAILED); }
    public static TestCaseBuilder aPassedTest()       { return new TestCaseBuilder().withStatus(Status.PASSED); }
    public static TestCaseBuilder aCriticalTest()     { return new TestCaseBuilder().withPriority(Priority.CRITICAL); }
    public static TestCaseBuilder aCriticalFailedTest(){
        return new TestCaseBuilder()
                .withPriority(Priority.CRITICAL)
                .withStatus(Status.FAILED);
    }

    // ── Настройщики: возвращают this для цепочки ─────────────────────────────
    public TestCaseBuilder withTitle(String title)      { this.title    = title;    return this; }
    public TestCaseBuilder withPriority(Priority p)     { this.priority = p;        return this; }
    public TestCaseBuilder withStatus(Status status)    { this.status   = status;   return this; }
    public TestCaseBuilder assignedTo(String assignee)  { this.assignee = assignee; return this; }

    // ── Финальный метод ───────────────────────────────────────────────────────
    public TestCase build() {
        TestCase tc = new TestCase(title, priority);
        tc.setStatus(status);
        if (assignee != null) tc.setAssignedTo(assignee);
        return tc;
    }
}
