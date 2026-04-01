package org.qatracker.lesson_17;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.qatracker.model.Priority;
import org.qatracker.model.Status;
import org.qatracker.model.TestCase;
import org.qatracker.service.TestCaseService;
import org.qatracker.service.TestRunService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCaseRepositoryManualMockTest {


    @Test
    @DisplayName("TestRunService works with stub repository data")
    void testRunService_usesStubData() {
        // Arrange: создаём stub с контролируемыми данными
        TestCase.resetIdCounter();
        TestCase tc1 = new TestCase("Login test",   Priority.CRITICAL);
        TestCase tc2 = new TestCase("Payment test", Priority.HIGH);
        tc1.setStatus(Status.PASSED);
        tc2.setStatus(Status.FAILED);

        StubTestCaseRepository stub = new StubTestCaseRepository(List.of(tc1, tc2));
        TestCaseService svc  = new TestCaseService(stub);
        TestRunService         run  = new TestRunService(svc);

        // Act
        double passRate = run.getPassRate();

        // Assert: результат зависит только от данных в stub, не от файла
        assertEquals(50.0, passRate, 0.001);
    }

    @Test
    @DisplayName("Spy records save() calls")
    void spy_recordsSaveCallCount() throws IOException {
        // Arrange
        TestCase.resetIdCounter();
        StubTestCaseRepository real = new StubTestCaseRepository(new ArrayList<>());
        SpyTestCaseRepository  spy  = new SpyTestCaseRepository(real);

        // Act: два вызова save()
        spy.save(new TestCase("Test 1", Priority.HIGH));
        spy.save(new TestCase("Test 2", Priority.LOW));

        // Assert: spy зафиксировал оба вызова
        assertEquals(2, spy.getSaveAllCount());
        assertEquals("Test 2", spy.getLastSavedTitle()); // последнее сохранённое
    }
}
