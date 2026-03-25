package org.qatracker.repository;

import org.qatracker.exception.InvalidTestDataException;
import org.qatracker.model.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// TestCaseRepository - персистентность тест-кейсов через CSV
public class TestCaseRepository {
    private static final Logger logger = LoggerFactory.getLogger(TestCaseRepository.class);

    private static final String HEADER = "id,title,status,priority,assignedTo";

    private final Path filePath;

    public TestCaseRepository(Path filePath) {
        this.filePath = filePath;
    }

    // Сохранение
    public void saveAll(List<TestCase> testCases) throws IOException {
        // Создаем родительские директории, если их нет
        if (filePath.getParent() != null) {
            Files.createDirectories(filePath.getParent());
        }

        logger.info("Saving {} test cases to: {}", testCases.size(), filePath);

        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            writer.write(HEADER);
            writer.newLine();
            for (TestCase tc : testCases) {
                writer.write(toCsv(tc));
                writer.newLine();
            }
        }

        logger.info("Saved successfully: {}", filePath);
    }

    // Загрузка
    public List<TestCase> loadAll() throws IOException {
        if (!Files.exists(filePath)) {
            logger.info("Data file not found, starting empty: {}", filePath);
            return new ArrayList<>();
        }

        List<TestCase>  result  = new ArrayList<>();
        List<String>    errors  = new ArrayList<>();
        int             lineNum = 0;

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                if (lineNum == 1) continue;     // пропуск заголовка

                if (line.isBlank()) continue;   // пропуск пустых строк

                try {
                    result.add(fromCsv(line));
                } catch (InvalidTestDataException e) {
                    logger.warn("Skipped invalid line {}: {}", lineNum, e.getMessage());
                    errors.add("Line " + line + ": " + e.getMessage());
                }
            }
        }

        if (!errors.isEmpty()) {
            logger.warn("Loaded with {} parse errors. First: {}", errors.size(), errors.get(0));
        }
        logger.info("Loaded {} test cases from {}", result.size(), filePath);

        return result;
    }

    // Конвертация
    private TestCase fromCsv(String line) throws InvalidTestDataException{
        // split с лимитом 5 - не разбивать assignedTo, если он содержит запятые
        String[] parts = line.split(",", 5);
        if (parts.length < 4)
            throw new InvalidTestDataException(line, "expected at least 4 columns, got " + parts.length);

        try {
            int     id          = Integer.parseInt(parts[0].trim());
            String  title       = unescapeCsv(parts[1].trim());
            String  status      = parts[2].trim();
            String  priority    = parts[3].trim();
            String  assignedTo  = parts.length > 4 && !parts[4].trim().isEmpty() ?
                    unescapeCsv(parts[4].trim()) : "";

            return new TestCase(id, title, status, priority, assignedTo);
        } catch (NumberFormatException e) {
            throw new InvalidTestDataException(line, "invalid id: '" + parts[0] + "'", e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private String toCsv(TestCase tc) {
        return String.join(",",
                String.valueOf(tc.getId()),
                escapeCsv(tc.getTitle()),
                tc.getStatus(),
                tc.getPriority(),
                tc.getAssignedTo() != null ? escapeCsv(tc.getAssignedTo()) : ""

        );
//        return String.format("%d,%s,%s,%s,%s",
//                tc.getId(),tc.getTitle(),tc.getStatus(),tc.getPriority(),tc.getAssignedTo() != null ? tc.getAssignedTo() : "");
    }

    // Экранирование: если содержит запятую или кавычку - обернуть в кавычки
    private String escapeCsv(String s) {
        if (s == null) return "";
        if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }

    private String unescapeCsv(String s) {
        if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2) {
            return s.substring(1, s.length() - 1).replace("\"\"", "\"");
        }
        return s;
    }
}











