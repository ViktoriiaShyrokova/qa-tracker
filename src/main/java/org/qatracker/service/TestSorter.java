package org.qatracker.service;

import org.qatracker.model.Status;
import org.qatracker.model.TestCase;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public interface TestSorter extends Comparator {

    /*
    `static Comparator<TestCase> byPriority()` — CRITICAL первые (по weight)
2. `static Comparator<TestCase> byTitle()` — алфавитно по title
3. `static Comparator<TestCase> byStatus()` — FAILED первые, PENDING последние (через Map с порядком)
4. `static Comparator<TestCase> byPriorityThenTitle()` — цепочка из п.1 и п.2
5. `static <T extends Comparable<T>> List<T> topN(List<T> items, int n)` — первые N элементов в естественном порядке
     */
    static Comparator<TestCase> byPriority() {
        return Comparator.comparingInt(tc -> tc.getPriority().getWeight());
    }

    static Comparator<TestCase> byTitle() {
        return Comparator.comparing(TestCase::getTitle);
    }

    static Comparator<TestCase> byStatus() {
        Map<Status, Integer> order = Map.of(Status.FAILED, 1,
                Status.BLOCKED, 2,
                Status.PENDING, 3,
                Status.PASSED, 4);
        return Comparator.comparingInt(tc -> order.get(tc.getStatus()));
    }

    static Comparator<TestCase> byPriorityThenTitle() {
        return byPriority().thenComparing(byTitle());
    }

    static <T extends Comparable<T>> List<T> topN(List<T> items, int n) {
        return items.stream()
                .sorted()
                .limit(n)
                .toList();
    }
}
