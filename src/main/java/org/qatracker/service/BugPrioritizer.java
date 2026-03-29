package org.qatracker.service;

import org.qatracker.model.BugReport;
import org.qatracker.model.BugSeverity;

import java.util.Comparator;
import java.util.List;

public class BugPrioritizer {

    public static List<BugReport> getUrgent(List<BugReport> bugs){
        return bugs.stream()
                .filter(bug -> bug.getSeverity().requiresImmediateAction())
                .sorted(Comparator.comparingInt(bug -> bug.getSeverity().getWeight()))
                .toList();
    }
}
