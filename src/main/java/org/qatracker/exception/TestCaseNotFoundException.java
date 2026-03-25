package org.qatracker.exception;
public class TestCaseNotFoundException extends QaTrackerException {

    private final int testCaseId;

    public TestCaseNotFoundException(int id) {
        super("TestCase not found: id=" + id);
        this.testCaseId = id;
    }

    public int getTestCaseId() { return testCaseId; }
}
