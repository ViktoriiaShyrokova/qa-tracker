package org.qatracker.service;

import org.qatracker.exception.TestCaseNotFoundException;
import org.qatracker.model.TestCase;
import org.qatracker.repository.InMemoryTestCaseRepo;
import org.qatracker.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TestCaseService {

    private static final Logger logger = LoggerFactory.getLogger(TestCaseService.class);

    private final Repository<TestCase, Integer> repository;

    public TestCaseService() {
        this.repository = new InMemoryTestCaseRepo();
    }

    public TestCaseService(Repository<TestCase, Integer> repository) {
        this.repository = repository;
    }



    public void add(TestCase tc) {
        try {
            repository.save(tc);
        } catch (IOException e) {
            logger.error("Failed to save TestCase id={}", tc.getId(), e);
        }
    }

    public boolean remove(int id) {
        return repository.delete(id);
    }

    public Optional<TestCase> findById(int id) {
        return repository.findById(id);
    }

    public TestCase getByIdOrThrow(int id) {
        return findById(id)
                .orElseThrow(() -> new TestCaseNotFoundException(id));
    }

    public List<TestCase> findAll() {
        return Collections.unmodifiableList(repository.findAll());
    }



    // Группировка по статусу

    public Map<String, List<TestCase>> groupByStatus() {
        return findAll().stream()
                .collect(Collectors.groupingBy(
                        tc -> tc.getStatus().name(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    // Статистика по приоритетам
    public Map<String, Long> countByPriority() {
        return findAll().stream()
                .collect(Collectors.groupingBy(
                        tc -> tc.getPriority().name(),
                        LinkedHashMap::new,
                        Collectors.counting()
                ));
    }

    public int size() {
        return repository.count();
    }
}