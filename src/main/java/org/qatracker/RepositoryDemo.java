package org.qatracker;

import org.qatracker.model.TestCase;
import org.qatracker.repository.TestCaseRepository;
import org.qatracker.service.TestCaseService;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class RepositoryDemo {
    public static void main(String[] args) {
        // Demo with repo
        Path path = Paths.get("./data","testcases.csv");
        TestCaseService service = new TestCaseService();
        TestCaseRepository repo = new TestCaseRepository(path);

        // Добавляем новые
        // текущий статус: PENDING/PASSED/FAILED/BLOCKED
        // приоритет: LOW/MEDIUM/HIGH/CRITICAL
        TestCase tc6 = new TestCase(1, "Login test", "PENDING", "CRITICAL", "Admin");
        TestCase tc7 = new TestCase(2, "Payment test", "PASSED", "CRITICAL", "Buh");
        TestCase tc8 = new TestCase(3, "Profile test", "BLOCKED", "MEDIUM", "Manager");
        TestCase tc9 = new TestCase(4, "Search test", "FAILED", "LOW", "Manager");
        TestCase tc10 = new TestCase(5, "Logout test", "FAILED", "HIGH", "Admin");

        List<TestCase> testCases = Arrays.asList(tc6, tc7, tc8, tc9, tc10);

        // Сохраняем в хранилище
        try {
            repo.saveAll(testCases);
            System.out.println("Saved successfully");
        } catch (IOException e) {
            System.err.println("Cannot save data: " + e.getMessage());
        }

        // Считываем данные из файла
        try {
            List<TestCase> loaded = repo.loadAll();
            System.out.println("Loaded: " + loaded.size() + " test cases");
        } catch (IOException e) {
            System.err.println("Cannot load data: " + e.getMessage());
        }
    }
}
