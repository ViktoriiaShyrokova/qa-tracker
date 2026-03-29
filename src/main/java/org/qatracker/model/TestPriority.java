package org.qatracker.model;

public enum TestPriority {

    SMOKE(1),
    SANITY(2),
    REGRESSION(3),
    PERFORMANCE(4);

    int level;

    TestPriority(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public boolean isHigherThan(TestPriority other) {
        return this.level > other.level;
   }
}
