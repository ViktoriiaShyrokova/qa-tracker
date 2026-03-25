package org.qatracker.model;

import java.time.LocalDateTime;
import java.util.Objects;

// BugReport — баг, привязанный к упавшему TestCase.
// Создаётся когда тест получил статус FAILED.
public class BugReport {

    private final int           id;
    private final int           testCaseId; // к какому тесту относится
    private String              title;
    private String              severity;   // LOW, MEDIUM, HIGH, CRITICAL
    private String              status;     // OPEN, IN_PROGRESS, FIXED, CLOSED
    private final LocalDateTime createdAt;

    private static int nextId = 1;

    public BugReport(int testCaseId, String title, String severity) {
        this.id         = nextId++;
        this.testCaseId = testCaseId;
        this.title      = title;
        this.severity   = severity;
        this.status     = "OPEN";
        this.createdAt  = LocalDateTime.now();
    }

    public int           getId()         { return id; }
    public int           getTestCaseId() { return testCaseId; }
    public String        getTitle()      { return title; }
    public String        getSeverity()   { return severity; }
    public String        getStatus()     { return status; }
    public LocalDateTime getCreatedAt()  { return createdAt; }

    public void setStatus(String status) { this.status = status; }

    public boolean isOpen() {
        return "OPEN".equals(status) || "IN_PROGRESS".equals(status);
    }

    public static void resetIdCounter() { nextId = 1; }

    @Override
    public String toString() {
        return String.format("BugReport{id=%d, tcId=%d, title='%s', severity=%s, status=%s}",
                id, testCaseId, title, severity, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BugReport b = (BugReport) o;
        return id == b.id && Objects.equals(title, b.title);
    }

    @Override
    public int hashCode() { return Objects.hash(id, title); }
}