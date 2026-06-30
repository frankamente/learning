package learning.domain;

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

    public Optional<T> findById(ID id) {
        return Optional.ofNullable(inMemoryCollection.get(id));
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
