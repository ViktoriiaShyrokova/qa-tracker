package org.qatracker.service;

import org.qatracker.exception.TestCaseNotFoundException;
import org.qatracker.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class BugService {

    private static final Logger logger = LoggerFactory.getLogger(BugService.class);

    private final Map<Integer, BugReport> bugs = new LinkedHashMap<>();
    private final TestCaseService testCaseService;

    public BugService(TestCaseService testCaseService) {
        this.testCaseService = testCaseService;
    }

    // Создать баг для упавшего тест-кейса
    public BugReport createBug(int testCaseId, String title, BugSeverity severity) {
        // orElseThrow: если тест-кейс не найден — бросит TestCaseNotFoundException
        TestCase tc = testCaseService.findById(testCaseId)
                .orElseThrow(() -> new TestCaseNotFoundException(testCaseId));

        if (!Status.FAILED.equals(tc.getStatus())) {
            logger.warn("Creating bug for non-failed TestCase id={}, status={}",
                    testCaseId, tc.getStatus());
        }

        BugReport bug = new BugReport(testCaseId, title, severity);
        bugs.put(bug.getId(), bug);
        logger.info("Bug created: id={}, tcId={}, severity={}", bug.getId(), testCaseId, severity);
        return bug;
    }

    public Optional<BugReport> findById(int id) {
        return Optional.ofNullable(bugs.get(id));
    }

    public List<BugReport> getOpenBugs() {
        List<BugReport> result = new ArrayList<>();
        for (BugReport b : bugs.values()) {
            if (b.isOpen()) result.add(b);
        }
        return Collections.unmodifiableList(result);
    }

    public void closeBug(int id) {
        BugReport bug = bugs.get(id);
        if (bug == null) {
            logger.error("Cannot close: BugReport not found id={}", id);
            throw new NoSuchElementException("BugReport not found: id=" + id);
        }
        bug.setStatus(BugStatus.CLOSED);
        logger.info("Bug closed: id={}", id);
    }

    public int size() {
        return bugs.size();
    }

    public void advanceStatus(int id) {
        BugReport bug = bugs.get(id);
        if (bug == null) {
            logger.error("Cannot close: BugReport not found id={}", id);
            throw new NoSuchElementException("BugReport not found: id=" + id);
        }
        bug.setStatus(bug.getStatus().next());
    }
}