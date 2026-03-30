package org.qatracker.model;

public enum BugStatus {
    OPEN,
    IN_PROGRESS,
    FIXED,
    CLOSED;

    public boolean isResolved() {
        return switch (this) {
            case IN_PROGRESS,OPEN -> false;
            case FIXED,CLOSED -> true;
        };
    }
    public BugStatus next() {
        return switch (this) {
            case OPEN -> IN_PROGRESS;
            case IN_PROGRESS -> FIXED;
            case FIXED -> CLOSED;
            case CLOSED -> CLOSED;
        };
    }

    public static BugStatus fromString(String value) {
        for (BugStatus status : values()) {
            if(status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }throw new IllegalArgumentException();
    }
}
