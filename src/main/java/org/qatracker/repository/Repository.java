package org.qatracker.repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

// Generic интерфейс - один контракт для любого хранилища.
// T -тип сущности, ID - тип идентификатора.
public interface Repository<T, ID> {
    void        save(T entity) throws IOException;
    Optional<T> findById(ID id);
    List<T>     findAll();
    boolean     delete(ID id);
    int         count();
}
