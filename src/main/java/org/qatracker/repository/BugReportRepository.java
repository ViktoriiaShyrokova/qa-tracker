package org.qatracker.repository;

import org.qatracker.exception.InvalidTestDataException;
import org.qatracker.model.BugReport;
import org.qatracker.model.BugSeverity;
import org.qatracker.model.BugStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class BugReportRepository implements Repository<BugReport, Integer> {

    private final Logger logger = LoggerFactory.getLogger(BugReportRepository.class);
    private static final String HEADER = "id,testCaseId,title,severity,status,createdAt";
    private final Path filePath;

    public BugReportRepository(Path filePath) {
        this.filePath = filePath;
    }

    public void saveAll(List<BugReport> bugs) throws IOException {
        if (filePath.getParent() != null) {
            Files.createDirectories(filePath.getParent());
        }

        logger.info("Saving {} bugs to: {}", bugs.size(), filePath);

        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            writer.write(HEADER);
            writer.newLine();
            for (BugReport bug : bugs) {
                writer.write(toCsv(bug));
                writer.newLine();
            }
        }
        logger.info("Saved {} successfully: {}", bugs.size(), filePath);
    }

    public List<BugReport> loadAll() throws IOException {
        if (!Files.exists(filePath)) {
            logger.info("Data file not found, starting empty: {}", filePath);
            return new ArrayList<>();
        }

        List<BugReport> bugs = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        int lineNum = 0;

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                if (lineNum == 1) continue;
                if (line.isBlank()) continue;
                try {
                    bugs.add(fromCsv(line));
                } catch (InvalidTestDataException e) {
                    logger.warn("Skipped invalid line {}: {}", lineNum, e.getMessage());
                    errors.add("Line " + lineNum + ": " + e.getMessage());
                }
            }
        }
        if (!errors.isEmpty()) {
            logger.warn("Loaded with {} parse errors. First: {}", errors.size(), errors.get(0));
        }
        logger.info("Loaded {} test cases from {}", bugs.size(), filePath);
        return bugs;
    }

    private BugReport fromCsv(String line) throws InvalidTestDataException {
        String[] parts = line.split(",", 6);

        if (parts.length < 6) {
            throw new InvalidTestDataException(line,
                    "expected 6 columns, got " + parts.length);
        }

        try {
            int id = Integer.parseInt(parts[0]);
            int testCaseId = Integer.parseInt(parts[1]);
            String title = unescapeCsv(parts[2]);
            BugSeverity severity = BugSeverity.fromString(parts[3].trim());
            BugStatus status = BugStatus.fromString(parts[4].trim());
            LocalDateTime createdAt = LocalDateTime.parse(parts[5].trim());

            return new BugReport(id, testCaseId, title, severity, status, createdAt);

        } catch (Exception e) {
            throw new InvalidTestDataException(line,
                    "invalid data: " + e.getMessage(), e);
        }
    }

    private String unescapeCsv(String title) {
        if (title.startsWith("\"") && title.endsWith("\"") && title.length() >= 2) {
            return title.substring(1, title.length() - 1).replace("\"\"", "\"");
        }
        return title;
    }

    private String toCsv(BugReport bug) {
        return String.join(",",
                String.valueOf(bug.getId()),
                String.valueOf(bug.getTestCaseId()),
                escapeCsv(bug.getTitle()),
                bug.getSeverity().name(),
                bug.getStatus().name(),
                bug.getCreatedAt().toString()
        );

    }

    private String escapeCsv(String title) {
        if (title == null) return "";
        if (title.contains(",") || title.contains("\"") || title.contains("\n"))
            return "\"" + title.replace("\"", "\"\"") + "\"";
        return title;
    }

    @Override
    public void save(BugReport entity) throws IOException {
        saveAll(List.of(entity));
    }

    @Override
    public Optional<BugReport> findById(Integer id) {
        try {
            return loadAll().stream()
                    .filter(bug -> bug.getId() == id)
                    .findFirst();

        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<BugReport> findAll() {
        try {
            return loadAll();
        } catch (IOException e) {
            return List.of();
        }
    }

    @Override
    public boolean delete(Integer id) {
        try {
            List<BugReport> all = loadAll();
            boolean removed = all.removeIf(bug -> bug.getId() == id);
            if (removed) saveAll(all);
            return removed;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public int count() {
        return findAll().size();
    }
}
