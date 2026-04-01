package org.qatracker.lesson_17;

import org.qatracker.model.TestCase;
import org.qatracker.repository.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class SpyTestCaseRepository implements Repository<TestCase, Integer> {

    private final Repository<TestCase, Integer> delegate;   // реальный объект
    private int saveAllCount = 0;
    private int findAllCallCount;
    private String lastSavedTitle;

    public SpyTestCaseRepository(Repository<TestCase, Integer> delegate) {
        this.delegate = delegate;
    }


    @Override
    public void save(TestCase tc) throws IOException {
        saveAllCount++;
        lastSavedTitle = tc.getTitle();
        delegate.save(tc);
    }

    @Override
    public Optional<TestCase> findById(Integer id) {
        return delegate.findById(id);
    }

    @Override
    public List<TestCase> findAll() {
        findAllCallCount++;
        return delegate.findAll();
    }

    @Override
    public boolean delete(Integer id) {
        return delegate.delete(id);
    }

    @Override
    public int count() {
        return delegate.count();
    }

    public int getSaveAllCount() {
        return saveAllCount;
    }

    public int getFindAllCallCount() {
        return findAllCallCount;
    }

    public String getLastSavedTitle() {
        return lastSavedTitle;
    }
}
