package org.qatracker.lesson_17;

import org.qatracker.model.TestCase;
import org.qatracker.model.TestCaseBuilder;
import org.qatracker.repository.Repository;
import org.qatracker.service.TestCaseService;
import org.qatracker.service.TestRunService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Финальный тест-класс — студенты пишут сами, преподаватель помогает
@ExtendWith(MockitoExtension.class)
class TestRunServiceIntegrationTest {

    private TestCaseService service;
    private TestRunService runService;

    @BeforeEach
    void setUp() {
        TestCase.resetIdCounter();
        service    = new TestCaseService();
        runService = new TestRunService(service);
    }

    // ── Тесты с Builder ──────────────────────────────────────────────────────

    @Test
    void generateSummary_shouldContainCorrectCounts() {
        service.add(TestCaseBuilder.aPassedTest().withTitle("TC1").build());
        service.add(TestCaseBuilder.aPassedTest().withTitle("TC2").build());
        service.add(TestCaseBuilder.aFailedTest().withTitle("TC3").build());
        service.add(TestCaseBuilder.aTestCase().withTitle("TC4").build()); // PENDING

        String summary = runService.generateSummary();

        assertAll("summary contains correct counts",
                () -> assertTrue(summary.contains("Total: 4")),
                () -> assertTrue(summary.contains("Passed: 2")),
                () -> assertTrue(summary.contains("Failed: 1")),
                () -> assertTrue(summary.contains("Pending: 1")),
                () -> assertTrue(summary.contains("50,0%"))
        );
    }

    @Test
    void getByAssignee_shouldReturnOnlyAssignedTests() {
        service.add(TestCaseBuilder.aTestCase()
                .withTitle("Alice test 1").assignedTo("alice").build());
        service.add(TestCaseBuilder.aTestCase()
                .withTitle("Alice test 2").assignedTo("alice").build());
        service.add(TestCaseBuilder.aTestCase()
                .withTitle("Bob test").assignedTo("bob").build());

        List<TestCase> aliceTests = runService.getByAssignee("alice");

        assertEquals(2, aliceTests.size());
        assertTrue(aliceTests.stream().allMatch(tc -> "alice".equals(tc.getAssignedTo())));
    }

    // ── Тесты с Mockito ──────────────────────────────────────────────────────

    @Test
    void mockito_example_verifyInteraction(@Mock Repository<TestCase, Integer> repo)
            throws IOException {
        // Настраиваем поведение
        when(repo.findAll()).thenReturn(List.of(
                TestCaseBuilder.aPassedTest().withTitle("Mocked test").build()
        ));

        // Вызываем
        List<TestCase> result = repo.findAll();

        // Проверяем результат
        assertEquals(1, result.size());

        // Проверяем что findAll() был вызван ровно 1 раз
        verify(repo, times(1)).findAll();
    }
}