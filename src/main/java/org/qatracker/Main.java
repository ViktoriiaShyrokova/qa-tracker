package org.qatracker;

import org.qatracker.exception.InvalidTestCaseException;
import org.qatracker.exception.TestCaseNotFoundException;
import org.qatracker.model.*;

import org.qatracker.repository.TestCaseRepository;
import org.qatracker.service.*;
import org.qatracker.util.TestDataFileGenerator;
import org.qatracker.validation.TestCaseValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.qatracker.model.Priority.*;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        logger.info("=== QA Tracker v0.2 started ===");

        // ── TestCaseService: центральный реестр ──────────────────────────────
        TestCaseService tcService = new TestCaseService();

        TestCase tc1 = new TestCase("Login test", CRITICAL); // не строка!
        TestCase tc2 = new TestCase("Payment test", HIGH);

        tc1.setStatus(Status.PASSED);
        tc2.setStatus(Status.FAILED);

        System.out.println(tc1.getStatus().getIcon() + " " + tc1.getTitle());
// ✓ Login test

// Switch в Main для команд
        String command = "report";
        switch (command) {
            case "list" -> System.out.println("Listing tests...");
            case "report" -> System.out.println("Generating report...");
            case "save" -> System.out.println("Saving data...");
            default -> System.out.println("Unknown command: " + command);
        }

        try {
            TestCaseValidator.validateOrThrow(tc1);
        } catch (InvalidTestCaseException e) {
            System.out.println(e.getErrors());
        }

        tcService.add(tc1);
        tcService.add(tc2);

        // Имитируем результаты
        tc1.setStatus(Status.FAILED);
        tc2.setStatus(Status.BLOCKED);

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
        BugReport bug1 = bugService.createBug(tc2.getId(), "Payment timeout on staging", BugSeverity.CRITICAL);
        System.out.println("Created: " + bug1);

        // Попытка создать баг для несуществующего теста → исключение
        try {
            bugService.createBug(999, "Ghost bug", BugSeverity.LOW);
        } catch (TestCaseNotFoundException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        // Открытые баги
        System.out.println("Open bugs: " + bugService.getOpenBugs().size());

        // Закрываем баг
        bugService.closeBug(bug1.getId());
        System.out.println("After close, open bugs: " + bugService.getOpenBugs().size());

        logger.info("=== QA Tracker finished ===");


        TestDataFileGenerator.generate("data//tcgenerator.csv", 100);
        try {

            TestDataFileGenerator.readStats("data//tcgenerator.csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("=".repeat(50));
        System.out.println("TestPriority");
        System.out.println(PriorityRouter.route(TestPriority.SMOKE));
        System.out.println(PriorityRouter.route(TestPriority.SANITY));
        System.out.println(PriorityRouter.route(TestPriority.REGRESSION));
        System.out.println(PriorityRouter.route(TestPriority.PERFORMANCE));
        System.out.println("=".repeat(50));
        System.out.println(PriorityRouter.estimateMinutes(TestPriority.SMOKE));
        System.out.println(PriorityRouter.estimateMinutes(TestPriority.SANITY));
        System.out.println(PriorityRouter.estimateMinutes(TestPriority.REGRESSION));
        System.out.println(PriorityRouter.estimateMinutes(TestPriority.PERFORMANCE));


        System.out.println("=========BugSeverity===========");

        List<BugReport> listBugs = List.of(new BugReport(11, "Test1", BugSeverity.CRITICAL),
                new BugReport(12, "Test2", BugSeverity.MEDIUM),
                new BugReport(13, "Test3", BugSeverity.HIGH),
                new BugReport(14, "Test4", BugSeverity.LOW),
                new BugReport(15, "Test5", BugSeverity.CRITICAL));
//        BugReport br1 = new BugReport(11,"Test1",BugSeverity.CRITICAL);
//        BugReport br2 = new BugReport(12,"Test2",BugSeverity.MEDIUM);
//        BugReport br3 = new BugReport(13,"Test3",BugSeverity.HIGH);
//        BugReport br4 = new BugReport(14,"Test4",BugSeverity.LOW);
//        BugReport br5 = new BugReport(15,"Test5",BugSeverity.CRITICAL);

        List<BugReport> urgentBugs = BugPrioritizer.getUrgent(listBugs);
        urgentBugs.forEach(bugReport -> {
            System.out.println(bugReport);
            System.out.println("SLA: " + bugReport.getSeverity().getSlaBreach());
        });

        System.out.println("=".repeat(100));
        TestCase tc3 =new TestCase("Login test", CRITICAL);
        tcService.add(tc3);
        BugReport br1 = bugService.createBug(tc3.getId(),"Test1", BugSeverity.CRITICAL);
        System.out.println("br1 id=" + br1.getId());
        System.out.println(br1.getStatus());
        bugService.advanceStatus(br1.getId());
        System.out.println(br1.getStatus());
        bugService.advanceStatus(br1.getId());
        System.out.println(br1.getStatus());
        bugService.advanceStatus(br1.getId());
        System.out.println(br1.getStatus());
        bugService.advanceStatus(br1.getId());
        System.out.println(br1.getStatus());


        System.out.println("=====-====Comparing Bugs==========");
        BugReport bugReport1 = new BugReport(200,200,"Bug1",BugSeverity.LOW,BugStatus.OPEN,LocalDateTime.now());
        BugReport bugReport2 = new BugReport(202,200,"Bug2",BugSeverity.HIGH,BugStatus.IN_PROGRESS,LocalDateTime.now().minusDays(25));
        BugReport bugReport3 = new BugReport(203,200,"Bug3",BugSeverity.CRITICAL,BugStatus.OPEN,LocalDateTime.now().minusDays(5));
        BugReport bugReport4 = new BugReport(204,200,"Bug4",BugSeverity.CRITICAL,BugStatus.OPEN,LocalDateTime.now().minusDays(10));
        BugReport bugReport5 = new BugReport(205,200,"Bug5",BugSeverity.MEDIUM,BugStatus.OPEN,LocalDateTime.now().minusDays(30));
        List<BugReport> bugReportList = List.of(bugReport1,bugReport2,bugReport3,bugReport4,bugReport5);
        bugService.createBug(tc3.getId(),"Bug1",BugSeverity.LOW );
        bugService.createBug(tc3.getId(),"Bug2",BugSeverity.HIGH);
        bugService.createBug(tc3.getId(),"Bug3",BugSeverity.CRITICAL );
        bugService.createBug(tc3.getId(),"Bug4",BugSeverity.CRITICAL );
        bugService.createBug(tc3.getId(),"Bug5",BugSeverity.MEDIUM);

        for (BugReport bug : BugReport.getSortedOpenBugs(bugService)) {
            System.out.println(bug);
        }
        Optional<BugReport> mostUrgent = BugReport.getMostUrgent(bugReportList);
        System.out.println(mostUrgent);
    }
}

