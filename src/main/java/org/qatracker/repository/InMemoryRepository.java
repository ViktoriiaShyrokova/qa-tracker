package org.qatracker.repository;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

public abstract class InMemoryRepository<T, ID> implements Repository<T, ID> {

    private Map<ID, T> storage = new LinkedHashMap<>();



    protected abstract ID getId(T entity);

    @Override
    public void save(T entity) throws IOException{
        storage.put(getId(entity),entity);
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean delete(ID id) {
        return storage.remove(id) != null;
    }

    @Override
    public int count() {
        return storage.size();
    }
    public List<T> findWhere(Predicate<T> condition){
        return findAll().stream()
                .filter(condition)
                .toList();
    }

    public List<T> findAllSorted(Comparator<T> comparator){
        return findAll().stream()
                .sorted(comparator)
                .toList();
    }
}
