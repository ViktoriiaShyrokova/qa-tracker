package org.qatracker.service;

import org.qatracker.exception.TestCaseNotFoundException;
import org.qatracker.model.Priority;
import org.qatracker.model.Status;
import org.qatracker.model.TestCase;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import java.util.*;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TestCaseService Tests")
class TestCaseServiceTest {

    private TestCaseService service;

    @BeforeEach
    void setUp() {
        // Сбрасываем static-счётчик — каждый тест начинает с id=1
        TestCase.resetIdCounter();
        service = new TestCaseService();
    }

    // ── add и findById ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("add() should store TestCase and findById should return it")
    void add_shouldStoreTestCase() {
        TestCase tc = new TestCase("Login test", Priority.HIGH);
        service.add(tc);

        Optional<TestCase> found = service.findById(tc.getId());
        assertTrue(found.isPresent());
        assertEquals("Login test", found.get().getTitle());
    }

    @Test
    @DisplayName("findById() should return empty Optional when not found")
    void findById_shouldReturnEmpty_whenNotFound() {
        Optional<TestCase> result = service.findById(999);
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("getByIdOrThrow() should throw TestCaseNotFoundException when not found")
    void getByIdOrThrow_shouldThrow_whenNotFound() {
        TestCaseNotFoundException ex = assertThrows(
                TestCaseNotFoundException.class,
                () -> service.getByIdOrThrow(42)
        );
        assertEquals(42, ex.getTestCaseId());
        assertTrue(ex.getMessage().contains("42"));
    }

    @Test
    @DisplayName("add() should not store duplicate — same id")
    void add_shouldIgnoreDuplicate() {
        TestCase tc = new TestCase("Login test", Priority.HIGH);
        service.add(tc);
        service.add(tc); // повторное добавление — игнорируется

        assertEquals(1, service.size());
    }

    // ── findAll и groupByStatus ────────────────────────────────────────────────

    @Test
    @DisplayName("findAll() should return all added TestCases")
    void findAll_shouldReturnAllTestCases() {
        service.add(new TestCase("Test 1", Priority.HIGH));
        service.add(new TestCase("Test 2", Priority.LOW));
        service.add(new TestCase("Test 3", Priority.CRITICAL));

        assertEquals(3, service.findAll().size());
    }

    @Test
    @DisplayName("groupByStatus() should group correctly after status changes")
    void groupByStatus_shouldReturnCorrectGroups() {
        TestCase tc1 = new TestCase("Test 1", Priority.HIGH);
        TestCase tc2 = new TestCase("Test 2", Priority.LOW);
        TestCase tc3 = new TestCase("Test 3", Priority.CRITICAL);

        service.add(tc1);
        service.add(tc2);
        service.add(tc3);

        tc1.setStatus(Status.PASSED);
        tc2.setStatus(Status.FAILED);
        // tc3 остаётся PENDING

        Map<String, List<TestCase>> grouped = service.groupByStatus();

        assertEquals(1, grouped.get("PASSED").size());
        assertEquals(1, grouped.get("FAILED").size());
        assertEquals(1, grouped.get("PENDING").size());
    }

    // ── Параметризованные: валидация ──────────────────────────────────────────

    @ParameterizedTest(name = "invalid title: ''{0}''")
    @ValueSource(strings = {"", "  ", "AB"})
    @DisplayName("TestCase with invalid title should throw")
    void invalidTitle_shouldThrow(String title) {
        assertThrows(IllegalArgumentException.class,
                () -> new TestCase(title, Priority.HIGH)
        );
    }

    static Stream<Arguments> validTestCases() {
        return Stream.of(
                Arguments.of("Login test",    Priority.CRITICAL, "critical test"),
                Arguments.of("Payment test",  Priority.HIGH,     "high priority"),
                Arguments.of("Profile test",  Priority.MEDIUM,   "medium priority"),
                Arguments.of("Search test",   Priority.LOW,      "low priority")
        );
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("validTestCases")
    @DisplayName("Valid TestCases should be stored correctly")
    void validTestCases_shouldBeStored(String title, Priority priority, String desc) {
        TestCase tc = new TestCase(title, priority);
        service.add(tc);

        assertAll("stored correctly",
                () -> assertTrue(service.findById(tc.getId()).isPresent()),
                () -> assertEquals(title,    service.findById(tc.getId()).get().getTitle()),
                () -> assertEquals(priority, service.findById(tc.getId()).get().getPriority())
        );
    }
}