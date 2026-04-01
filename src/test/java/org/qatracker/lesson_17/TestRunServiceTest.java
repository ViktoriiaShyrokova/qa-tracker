package org.qatracker.lesson_17;

import org.qatracker.model.Priority;
import org.qatracker.model.TestCase;
import org.qatracker.model.TestCaseBuilder;
import org.qatracker.service.TestCaseService;
import org.qatracker.service.TestRunService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TestRunServiceTest {
    private TestCaseService service;
    private TestRunService runService;

    @BeforeEach
    void setUp() {
        TestCase.resetIdCounter();
        service = new TestCaseService();
        runService = new TestRunService(service);
    }


    @Test
    @DisplayName("Summary contains correct total and pass rate")
    void summary_correctCountsAndPassRate() {
        service.add(TestCaseBuilder.aPassedTest().withTitle("TC1").build());
        service.add(TestCaseBuilder.aPassedTest().withTitle("TC2").build());
        service.add(TestCaseBuilder.aFailedTest().withTitle("TC3").build());
        service.add(TestCaseBuilder.aTestCase().withTitle("TC4").build());

        String summary = runService.generateSummary();
        System.out.println(summary);
        assertAll(
                () -> assertTrue(summary.contains("Total: 4")),
                () -> assertTrue(summary.contains("Passed: 2")),
                () -> assertTrue(summary.contains("Failed: 1")),
                () -> assertTrue(summary.contains("Pending: 1")),
                () -> assertTrue(summary.contains("50,0%"))
        );
    }

    @Test
    @DisplayName("Failed tests sorted CRITICAL → HIGH → LOW")
    void failedByPriority_sortedCorrectly() {
        service.add(TestCaseBuilder.aFailedTest()
                .withTitle("Low").withPriority(Priority.LOW).build());
        service.add(TestCaseBuilder.aCriticalFailedTest()
                .withTitle("Critical").build());
        service.add(TestCaseBuilder.aFailedTest()
                .withTitle("High").withPriority(Priority.HIGH).build());

        List<TestCase> failed = runService.getFailedByPriority();

        assertEquals(Priority.CRITICAL, failed.get(0).getPriority());
        assertEquals(Priority.HIGH, failed.get(1).getPriority());
        assertEquals(Priority.LOW, failed.get(2).getPriority());
    }

    @ParameterizedTest(name = "{2}: ожидаемый pass rate = {1}%")
    @MethodSource("passRateScenarios")
    void passRate_variousScenarios(List<TestCase> tests, double expected, String desc) {
        tests.forEach(service::add);
        assertEquals(expected, runService.getPassRate(), 0.1);
    }


    @Test
    @DisplayName("Pass rate should be 66.7% when 2 of 3 tests pass")
    void passRate_twoOrThree() {
        // Builder
        service.add(TestCaseBuilder.aPassedTest().withTitle("Login").build());
        service.add(TestCaseBuilder.aPassedTest().withTitle("Profile").build());
        service.add(TestCaseBuilder.aFailedTest().withTitle("Payment").build());

        assertEquals(66.7, runService.getPassRate(), 0.1);
    }

    @Test
    @DisplayName("Failed tests sorted by priority — CRITICAL first")
    void failedByPriority_criticalFirst() {
        service.add(TestCaseBuilder.aFailedTest()
                .withTitle("Low priority fail")
                .withPriority(Priority.LOW)
                .build());
        service.add(TestCaseBuilder.aCriticalFailedTest()
                .withTitle("Critical fail")
                .build());
        service.add(TestCaseBuilder.aFailedTest()
                .withTitle("High priority fail")
                .withPriority(Priority.HIGH)
                .build());

        List<TestCase> failed = runService.getFailedByPriority();

        assertEquals(3, failed.size());
        assertEquals(Priority.CRITICAL, failed.get(0).getPriority()); // первый — CRITICAL
        assertEquals(Priority.HIGH, failed.get(1).getPriority());
        assertEquals(Priority.LOW, failed.get(2).getPriority());
    }

    @ParameterizedTest(name = "{2}: pass rate = {1}%")
    @MethodSource("passRateScenarios")
    void passRateShouldBeCalculatedCorrectly(List<TestCase> tests, double expected, String desc) {
        tests.forEach(service::add);
        assertEquals(expected, runService.getPassRate(), 0.1);
    }

    static Stream<Arguments> passRateScenarios() {
        return Stream.of(
                Arguments.of(List.of(TestCaseBuilder.aPassedTest().build()), 100.0, "all passed"),
                Arguments.of(List.of(TestCaseBuilder.aFailedTest().build()), 0.0, "all failed"),
                Arguments.of(List.of(
                                TestCaseBuilder.aPassedTest().build(),
                                TestCaseBuilder.aFailedTest().build()),
                                50.0, "half passed"),
                Arguments.of(List.of(), // пустой список
                        0.0, "empty — pass rate is 0")
        );
    }

}
