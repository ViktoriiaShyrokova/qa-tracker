package org.qatracker.lesson_17;

import org.qatracker.model.TestCase;
import org.qatracker.repository.Repository;

import java.io.IOException;
import java.util.*;

/**
 * Stub - возвращает фиксированные данные
 */
public class StubTestCaseRepository implements Repository<TestCase, Integer> {
    private final List<TestCase> stubbedData;

    public StubTestCaseRepository(List<TestCase> stubbedData) {
        this.stubbedData = new ArrayList<>(stubbedData);
    }

    @Override
    public void save(TestCase tc) throws IOException {
        stubbedData.add(tc);
    }

    @Override
    public Optional<TestCase> findById(Integer id) {
        return stubbedData.stream()
                .filter(tc -> tc.getId() == id)
                .findFirst();
    }

    @Override
    public List<TestCase> findAll() {
        return Collections.unmodifiableList(stubbedData);
    }

    @Override
    public boolean delete(Integer id) {
        return stubbedData.removeIf(tc -> tc.getId() == id);
    }

    @Override
    public int count() {
        return stubbedData.size();
    }
}
