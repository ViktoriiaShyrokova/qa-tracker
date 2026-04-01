package org.qatracker.lesson_17;

import org.qatracker.model.TestCase;
import org.qatracker.repository.TestCaseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Custom")
public class CustomTestCaseTest {

    @Test
    void loadAll_shouldReturnTestCase() {
        TestCaseRepository repo = new TestCaseRepository(Paths.get("./data/testcase.csv"));
        List<TestCase> cases = repo.findAll();  // нужен реальный файл
        assertEquals(0, cases.size());  // а если файл изменился?
    }
}
