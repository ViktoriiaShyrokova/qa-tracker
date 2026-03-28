package org.qatracker.util;

import org.qatracker.model.Priority;
import org.qatracker.model.Status;
import org.qatracker.model.TestCase;

import java.io.BufferedWriter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import static org.qatracker.repository.TestCaseRepository.escapeCsv;

public class TestDataFileGenerator {

    private String filePath;
    private int count;


    private TestDataFileGenerator() {}

    public static void generate(String filePath, int count) {

        Path csv = Paths.get(filePath);

        try (BufferedWriter writer = Files.newBufferedWriter(csv, StandardCharsets.UTF_8)) {
            if (csv.getParent() != null) {
                Files.createDirectories(csv.getParent());
            }
//            String[] statuses = {"PENDING", "PASSED", "FAILED", "BLOCKED"};
//            String[] priorities = {"LOW", "MEDIUM", "HIGH", "CRITICAL"};
            Status[] statuses = Status.values();
            Priority[] priorities = Priority.values();
            IntStream.rangeClosed(1, count)
                    .mapToObj(i -> {
                        String title = "Auto Test #" + i;
                       Status status = statuses[i % statuses.length];
                        Priority priority = priorities[i % priorities.length];
                        return new TestCase(i, title, status, priority);
                    })
                    .map(tc -> String.join(",",
                            String.valueOf(tc.getId()),
                            escapeCsv(tc.getTitle()),
                            tc.getStatus().name(),
                            tc.getPriority().name()))
                    .forEach(line -> {
                                try {
                                    writer.write(line);
                                    writer.newLine();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    );


        } catch (IOException e) {
            System.out.println("Error! " + e.getMessage());
        }
    }

    public static void readStats(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Map<String, Integer> statusStats = new HashMap<>();
        // "1,A,PENDING"
        long lineNum = Files.lines(path)
                .peek(line -> {
                    String status = line.split(",")[2];
                    statusStats.merge(status, 1, Integer::sum);
                })
                .count();

        long sizeKb = Files.size(path) / 1024;

        System.out.println("Lines: " + lineNum);
        System.out.println("Status stats: " + statusStats);
        System.out.println("Size: " + sizeKb + " KB");
    }


}
