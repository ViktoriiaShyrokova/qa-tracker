package org.qatracker.repository;

import org.qatracker.model.TestCase;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class InMemoryTestCaseRepo extends InMemoryRepository<TestCase,Integer>{




    @Override
    protected Integer getId(TestCase entity) {
        return entity.getId();
    }

}
