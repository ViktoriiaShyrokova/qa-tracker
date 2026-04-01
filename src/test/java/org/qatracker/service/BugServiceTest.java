package org.qatracker.service;

import org.qatracker.exception.TestCaseNotFoundException;
import org.qatracker.model.*;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BugService Tests")
class BugServiceTest {

    private TestCaseService tcService;
    private BugService      bugService;
    private TestCase failedTc;

    @BeforeEach
    void setUp() {
        TestCase.resetIdCounter();
        BugReport.resetIdCounter();

        tcService  = new TestCaseService();
        bugService = new BugService(tcService);

        // Готовим упавший тест-кейс — большинство тестов BugService его используют
        failedTc = new TestCase("Payment test", Priority.CRITICAL);
        tcService.add(failedTc);
        failedTc.setStatus(Status.FAILED);
    }

    @Test
    @DisplayName("createBug() should create BugReport linked to TestCase")
    void createBug_shouldCreateLinkedBugReport() {
        BugReport bug = bugService.createBug(failedTc.getId(), "Payment timeout", BugSeverity.CRITICAL);

        assertNotNull(bug);
        assertEquals(failedTc.getId(), bug.getTestCaseId());
        assertEquals("Payment timeout", bug.getTitle());
        assertEquals("OPEN", bug.getStatus().name());
        assertTrue(bug.isOpen());
    }

    @Test
    @DisplayName("createBug() should throw when TestCase not found")
    void createBug_shouldThrow_whenTestCaseNotFound() {
        TestCaseNotFoundException ex = assertThrows(
                TestCaseNotFoundException.class,
                () -> bugService.createBug(999, "Ghost bug", BugSeverity.LOW)
        );
        assertEquals(999, ex.getTestCaseId());
    }

    @Test
    @DisplayName("getOpenBugs() should return only open bugs")
    void getOpenBugs_shouldReturnOnlyOpen() {
        BugReport bug1 = bugService.createBug(failedTc.getId(), "Bug 1", BugSeverity.HIGH);
        BugReport bug2 = bugService.createBug(failedTc.getId(), "Bug 2", BugSeverity.LOW);

        bugService.closeBug(bug1.getId());

        List<BugReport> open = bugService.getOpenBugs();
        assertEquals(1, open.size());
        assertEquals(bug2.getId(), open.get(0).getId());
    }

    @Test
    @DisplayName("closeBug() should change status to CLOSED")
    void closeBug_shouldChangeStatusToClosed() {
        BugReport bug = bugService.createBug(failedTc.getId(), "Bug to close", BugSeverity.MEDIUM);
        assertTrue(bug.isOpen());

        bugService.closeBug(bug.getId());

        assertFalse(bug.isOpen());
        assertEquals("CLOSED", bug.getStatus().name());
    }

    @Test
    @DisplayName("closeBug() should throw when bug not found")
    void closeBug_shouldThrow_whenNotFound() {
        assertThrows(NoSuchElementException.class,
                () -> bugService.closeBug(999)
        );
    }
}