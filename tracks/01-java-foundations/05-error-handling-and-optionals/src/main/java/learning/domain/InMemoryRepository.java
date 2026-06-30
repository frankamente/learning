package learning.domain;

import learning.domain.exception.DuplicateKeyException;
import learning.domain.exception.EntityNotFoundException;
import learning.domain.result.Failure;
import learning.domain.result.Result;
import learning.domain.result.Success;

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
        if (findById(entity.id()).isPresent()) {
            throw new DuplicateKeyException("Error saving entity: " + entity);
        }
        this.save(entity);
    }

    public Optional<T> findById(ID id) {
        return Optional.ofNullable(inMemoryCollection.get(id));
    }

    public T getOrThrow(ID id) throws EntityNotFoundException {
        return findById(id).orElseThrow(() -> new EntityNotFoundException("unknown entity with id: " + id));
    }

    public Result<T> trySave(T entity) {
        try {
            saveStrict(entity);
            return new Success<>(entity);
        } catch (DuplicateKeyException e) {
            return new Failure<>("error saving entity: " + entity, e);
        }
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
