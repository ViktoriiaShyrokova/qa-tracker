package org.qatracker.exception;

public class DuplicateTestException extends QaTrackerException {

    public DuplicateTestException(int id) {
        super("TestCase already exists: id=" + id);
    }
}