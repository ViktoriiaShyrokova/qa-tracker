package org.qatracker.service;
import org.qatracker.exception.TestCaseNotFoundException;
import org.qatracker.model.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class TestCaseService {

    private static final Logger logger = LoggerFactory.getLogger(TestCaseService.class);

    // LinkedHashMap: O(1) поиск по ID + сохраняет порядок добавления
    private final Map<Integer, TestCase> storage = new LinkedHashMap<>();

    public void add(TestCase tc) {
        if (storage.containsKey(tc.getId())) {
            logger.warn("TestCase with id={} already exists, skipping", tc.getId());
            return;
        }
        storage.put(tc.getId(), tc);
        logger.info("TestCase added: id={}, title='{}'", tc.getId(), tc.getTitle());
    }

    public Optional<TestCase> findById(int id) {
        TestCase tc = storage.get(id);
        if (tc == null) logger.debug("TestCase not found: id={}", id);
        return Optional.ofNullable(tc);
    }
    // Найти по ID или бросить исключение — удобно когда null недопустим
    public TestCase getByIdOrThrow(int id) {
        return findById(id)
                .orElseThrow(() -> new TestCaseNotFoundException(id));
    }

    public List<TestCase> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(storage.values()));
    }

    public boolean remove(int id) {
        TestCase removed = storage.remove(id);
        if (removed != null) {
            logger.info("TestCase removed: id={}", id);
            return true;
        }
        logger.warn("Cannot remove: TestCase not found id={}", id);
        return false;
    }

    // Группировка по статусу
//    public Map<String, List<TestCase>> groupByStatus() {
//        Map<String, List<TestCase>> grouped = new LinkedHashMap<>();
//        for (TestCase tc : storage.values()) {
//            grouped.computeIfAbsent(tc.getStatus(), k -> new ArrayList<>()).add(tc);
//        }
//        return Collections.unmodifiableMap(grouped);
//    }
    public Map<String, List<TestCase>> groupByStatus() {
        return storage.values().stream()
                .collect(Collectors.groupingBy(tc -> tc.getStatus().name()));
    }

    // Статистика по приоритетам
    public Map<String, Long> countByPriority() {
        Map<String, Long> counts = new LinkedHashMap<>();
        for (TestCase tc : storage.values()) {
            counts.merge(tc.getPriority().name(), 1L, Long::sum);
        }
        return counts;
    }

    public int size() { return storage.size(); }
}