package org.qatracker;

import org.qatracker.exception.TestCaseNotFoundException;
import org.qatracker.model.BugReport;
import org.qatracker.model.TestCase;

import org.qatracker.model.TestSuite;
import org.qatracker.repository.TestCaseRepository;
import org.qatracker.service.BugService;
import org.qatracker.service.TestCaseService;
import org.qatracker.service.TestRunService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        logger.info("=== QA Tracker v0.2 started ===");

        // ── TestCaseService: центральный реестр ──────────────────────────────
        TestCaseService tcService = new TestCaseService();

        TestCase tc1 = new TestCase("Login test", "CRITICAL");
        TestCase tc2 = new TestCase("Payment test", "CRITICAL");
        TestCase tc3 = new TestCase("Profile test", "MEDIUM");
        TestCase tc4 = new TestCase("Search test", "LOW");
        TestCase tc5 = new TestCase("Logout test", "HIGH");

        tcService.add(tc1);
        tcService.add(tc2);
        tcService.add(tc3);
        tcService.add(tc4);
        tcService.add(tc5);

        // Имитируем результаты
        tc1.setStatus("PASSED");
        tc2.setStatus("FAILED");
        tc3.setStatus("PASSED");
        // tc4, tc5 остаются PENDING

        // Поиск по ID
        System.out.println("\n=== Find by ID ===");
        tcService.findById(2).ifPresent(tc -> System.out.println("Found: " + tc));
        tcService.findById(99).ifPresentOrElse(
                tc -> System.out.println("Found: " + tc),
                () -> System.out.println("Not found: id=99")
        );

        // Группировка по статусу
        System.out.println("\n=== By Status ===");
        tcService.groupByStatus().forEach((status, tests) ->
                System.out.println(status + ": " + tests.size() + " tests")
        );

        // Статистика по приоритетам
        System.out.println("\n=== By Priority ===");
        tcService.countByPriority().forEach((priority, count) ->
                System.out.println(priority + " → " + count)
        );

        // ── TestSuite: набор для прогона (из занятия 8) ──────────────────────
        System.out.println("\n=== TestSuite Report ===");
        TestSuite suite = new TestSuite("Sprint 42");
        tcService.findAll().forEach(suite::add);
        suite.printReport();

        // ── TestRunService: аналитика через Stream API ───────────────────────────
        System.out.println("\n=== Run Analytics ===");
        TestRunService runService = new TestRunService(tcService);

        System.out.println(runService.generateSummary());

        System.out.println("\nFAILED по приоритету:");
        runService.getFailedByPriority().forEach(tc ->
                System.out.println("  [" + tc.getPriority() + "] " + tc.getTitle())
        );

        System.out.println("\nСтатистика по статусам:");
        runService.countByStatus().forEach((status, count) ->
                System.out.println("  " + status + ": " + count)
        );

        // ── BugService: работа с багами ──────────────────────────────────────────
        System.out.println("\n=== Bug Tracking ===");
        BugService bugService = new BugService(tcService);

        // Создаём баг для упавшего теста (tc2 = Payment test, FAILED)
        BugReport bug1 = bugService.createBug(tc2.getId(), "Payment timeout on staging", "CRITICAL");
        System.out.println("Created: " + bug1);

        // Попытка создать баг для несуществующего теста → исключение
        try {
            bugService.createBug(999, "Ghost bug", "LOW");
        } catch (TestCaseNotFoundException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        // Открытые баги
        System.out.println("Open bugs: " + bugService.getOpenBugs().size());

        // Закрываем баг
        bugService.closeBug(bug1.getId());
        System.out.println("After close, open bugs: " + bugService.getOpenBugs().size());

        logger.info("=== QA Tracker finished ===");


    }
}

