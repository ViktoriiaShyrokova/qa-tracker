package org.qatracker.repository;

import org.qatracker.model.BugReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class BugReportRepository {

    private final Logger logger = LoggerFactory.getLogger(BugReportRepository.class);
    private static final String HEADER = "id,testCaseId,title,severity,status,createdAt";
    private final Path filePath;

    public BugReportRepository(Path filePath) {
        this.filePath = filePath;
    }

//    public void saveAll(List<BugReport> bugs) throws IOException {
//        if(filePath.getParent() != null) {
//            Files.createDirectories(filePath.getParent());
//        }
//
//        logger.info("Saving {} bugs to: {}", bugs.size(), filePath);
//
//        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)){
//            writer.write(HEADER);
//            writer.newLine();
//            for (BugReport bug : bugs) {
//                writer.write(toCsv(bug));
//                writer.newLine();
//            }
//        }
//        logger.info("Saved successfully: {}", filePath);
//    }

//    private String toCsv(BugReport bug) {
////        return String.format("%d,%d,%s,%s,%s,%s",
////                bug.getId(),bug.getTestCaseId(),bug.getTitle(),bug.getSeverity(),bug.getStatus(),bug.getCreatedAt());
//        return String.join(",",
//                String.valueOf(bug.getId()),
//                String.valueOf(bug.getTestCaseId()),
//                escapeCsv(bug.getTitle()),
//                bug.getSeverity(),
//                bug.getStatus(),
//                bug.getCreatedAt().toString()
//        );
//
//    }

//    private String escapeCsv(String title) {
//        if (title == null) return "";
//        if (title.contains(",") || title.contains("\""))
//        return null;
//    }
}
