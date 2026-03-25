package org.qatracker.exception;
// Базовый класс для всех исключений QA Tracker.
// Позволяет ловить любую ошибку трекера одним catch(QaTrackerException e).
public class QaTrackerException extends RuntimeException {

    public QaTrackerException(String message) {
        super(message);
    }

    public QaTrackerException(String message, Throwable cause) {
        super(message, cause);
    }
}