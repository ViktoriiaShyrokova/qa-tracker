package org.qatracker.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestSuite implements Iterable<TestCase> {
    private final String name;
    private final List<TestCase> testCases;

    public TestSuite(String name) {
        this.name = name;
        this.testCases = new ArrayList<>();
    }

    public void add(TestCase testCase) {
        testCases.add(testCase);
    }

    public void printReport() {
        System.out.println("= TestSuite Report =");
        System.out.println("= Report =");
        System.out.printf("TestSuite '%s': total=%d, passed=%d, failed=%d, pending=%d, blocked=%d%n",
                name, getTotalCount(), getPassedCount(), getFailedCount(), getPendingCount(), getBlockedCount());
    }

    public int getTotalCount() {
        return testCases.size();
    }

    public int getPassedCount() {
        return (int) testCases.stream().filter(TestCase::isPassed).count();
    }

    public int getFailedCount() {
        return (int) testCases.stream().filter(TestCase::isFailed).count();
    }

    public int getPendingCount() {
        return (int) testCases.stream().filter(TestCase::isPending).count();
    }

    public int getBlockedCount() {
        return (int) testCases.stream().filter(tc -> tc.getStatus().equals(Status.BLOCKED)).count();
    }

    public String getName() {
        return name;
    }

    @Override
    public Iterator<TestCase> iterator() {
        return testCases.iterator();
    }

    // Filtering iterator
    public Iterator<TestCase> iterator(String statusFilter) {
        return new FilteredIterator(testCases.iterator(), statusFilter);
    }

    private static class FilteredIterator implements Iterator<TestCase> {
        private final Iterator<TestCase> originalIterator;
        private final String statusFilter;
        private TestCase nextTestCase;
        private boolean hasNextCalled = false;

        public FilteredIterator(Iterator<TestCase> originalIterator, String statusFilter) {
            this.originalIterator = originalIterator;
            this.statusFilter = statusFilter;
        }

        @Override
        public boolean hasNext() {
            if (!hasNextCalled) {
                nextTestCase = findNextMatching();
                hasNextCalled = true;
            }
            return nextTestCase != null;
        }

        @Override
        public TestCase next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            TestCase result = nextTestCase;
            hasNextCalled = false;
            nextTestCase = null;
            return result;
        }

        private TestCase findNextMatching() {
            while (originalIterator.hasNext()) {
                TestCase tc = originalIterator.next();
                if (statusFilter == null || statusFilter.equalsIgnoreCase(tc.getStatus().name())) {
                    return tc;
                }
            }
            return null;
        }
    }
}
