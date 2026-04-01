package org.qatracker.lesson_17;

import org.qatracker.model.Priority;
import org.qatracker.model.TestCase;
import org.qatracker.service.TestCaseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("Stub and Spy Test")
public class StubAndSpyTest {

    @Test
    void testRunService_usesRepository() {

        // Arrange: создаем stub с нужными данными
        TestCase tc1 = new TestCase("Login test", Priority.CRITICAL);
        TestCase tc2 = new TestCase("Payment test", Priority.HIGH);

        StubTestCaseRepository stub = new StubTestCaseRepository(List.of(tc1, tc2));

//        TestCaseService service = new TestCaseService(stub);
    }
}
