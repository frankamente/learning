package learning.domain;

import learning.domain.exception.DuplicateKeyException;
import learning.domain.exception.EntityNotFoundException;
import learning.domain.result.Result;
import learning.domain.result.Success;
import learning.domain.result.Failure;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class InMemoryRepository<T extends Entity<ID>, ID> {

    private final LinkedHashMap<ID, T> inMemoryCollection;

    public InMemoryRepository() {
        this.inMemoryCollection = new LinkedHashMap<>();
    }

    public void save(T entity) {
        inMemoryCollection.put(entity.id(), entity);
    }

    public void saveStrict(T entity) throws DuplicateKeyException {
        // TODO: Throw DuplicateKeyException if key already exists, otherwise save normally
    }

    public Optional<T> findById(ID id) {
        return Optional.ofNullable(inMemoryCollection.get(id));
    }

    public T getOrThrow(ID id) throws EntityNotFoundException {
        // TODO: Retrieve entity by ID or throw EntityNotFoundException with descriptive message
        return null;
    }

    public Result<T> trySave(T entity) {
        // TODO: Try to save strictly, returning Success<T> or Failure<T>
        return null;
    }

    public List<T> findAll() {
        return List.copyOf(inMemoryCollection.values());
    }

    public void deleteById(ID id) {
        inMemoryCollection.remove(id);
    }

    public List<T> find(Predicate<T> filter) {
        return inMemoryCollection.values().stream().filter(filter).toList();
    }
}
