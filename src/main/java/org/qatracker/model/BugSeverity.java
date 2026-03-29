package org.qatracker.model;

public enum BugSeverity {
    LOW(4,"Low"),
    MEDIUM(3,"Medium"),
    HIGH(2,"High"),
    CRITICAL(1,"Critical");

    private int weight;
    private String label;

    BugSeverity(int weight, String label) {
        this.weight = weight;
        this.label = label;
    }

    public int getWeight() {
        return weight;
    }

    public boolean requiresImmediateAction() {
        return switch (this){
            case CRITICAL,HIGH -> true;
            case MEDIUM, LOW -> false;
        };
    }
    public String getSlaBreach() {
        return switch (this){
            case CRITICAL -> "4h";
            case HIGH -> "24h";
            case MEDIUM -> "72h";
            case LOW -> "1week";
        };
    }
    public static BugSeverity fromString(String value){
        for (BugSeverity severity : values()) {
            if(severity.name().equalsIgnoreCase(value) ||
            severity.label.equalsIgnoreCase(value)) {
                return severity;
            }
        }throw new IllegalArgumentException("Unknown severity" + value);
    }
}
