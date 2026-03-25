package org.qatracker.service;

import org.qatracker.model.TestCase;

import java.util.*;
import java.util.stream.*;

public class TestRunService {

    private final TestCaseService testCaseService;

    public TestRunService(TestCaseService testCaseService) {
        this.testCaseService = testCaseService;
    }

    // FAILED тесты, отсортированные: CRITICAL первые
    public List<TestCase> getFailedByPriority() {
        List<String> order = List.of("CRITICAL", "HIGH", "MEDIUM", "LOW");
        return testCaseService.findAll().stream()
                .filter(tc -> "FAILED".equals(tc.getStatus()))
                .sorted(Comparator.comparingInt(tc -> order.indexOf(tc.getPriority())))
                .collect(Collectors.toList());
    }

    // Тесты конкретного исполнителя
    public List<TestCase> getByAssignee(String assignee) {
        return testCaseService.findAll().stream()
                .filter(tc -> assignee.equals(tc.getAssignedTo()))
                .collect(Collectors.toList());
    }

    // Процент прохождения
    public double getPassRate() {
        List<TestCase> all = testCaseService.findAll();
        if (all.isEmpty()) return 0.0;
        long passed = all.stream().filter(TestCase::isPassed).count();
        return (double) passed / all.size() * 100;
    }

    // Группировка по статусу — одна строка вместо цикла
    public Map<String, List<TestCase>> groupByStatus() {
        return testCaseService.findAll().stream()
                .collect(Collectors.groupingBy(TestCase::getStatus));
    }

    // Количество по каждому статусу
    public Map<String, Long> countByStatus() {
        return testCaseService.findAll().stream()
                .collect(Collectors.groupingBy(TestCase::getStatus, Collectors.counting()));
    }

    // Итоговый отчёт одной строкой
    public String generateSummary() {
        List<TestCase> all = testCaseService.findAll();
        long passed  = all.stream().filter(TestCase::isPassed).count();
        long failed  = all.stream().filter(TestCase::isFailed).count();
        long pending = all.stream().filter(TestCase::isPending).count();
        return String.format(
                "Total: %d | Passed: %d | Failed: %d | Pending: %d | Pass Rate: %.1f%%",
                all.size(), passed, failed, pending, getPassRate()
        );
    }
}