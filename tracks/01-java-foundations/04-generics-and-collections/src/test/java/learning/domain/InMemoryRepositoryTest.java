package learning.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

class InMemoryRepositoryTest {

    record TestProduct(String id, String name, double price) implements Entity<String> {}

    private InMemoryRepository<TestProduct, String> repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryRepository<>();
    }

    @Test
    void shouldSaveAndFindById() {
        var product = new TestProduct("p1", "Keyboard", 49.99);
        
        repository.save(product);
        Optional<TestProduct> found = repository.findById("p1");

        assertThat(found).isPresent().hasValue(product);
    }

    @Test
    void shouldReturnEmptyOptionalWhenEntityDoesNotExist() {
        Optional<TestProduct> found = repository.findById("non-existent");
        
        assertThat(found).isEmpty();
    }

    @Test
    void shouldFindAllInInsertionOrder() {
        var p1 = new TestProduct("p1", "Keyboard", 49.99);
        var p2 = new TestProduct("p2", "Mouse", 29.99);
        var p3 = new TestProduct("p3", "Monitor", 199.99);

        repository.save(p1);
        repository.save(p2);
        repository.save(p3);

        List<TestProduct> all = repository.findAll();

        assertThat(all).containsExactly(p1, p2, p3);
    }

    @Test
    void shouldUpdateEntityInPlaceWithoutChangingInsertionOrder() {
        var p1 = new TestProduct("p1", "Keyboard", 49.99);
        var p2 = new TestProduct("p2", "Mouse", 29.99);
        var p1Updated = new TestProduct("p1", "Mechanical Keyboard", 89.99);

        repository.save(p1);
        repository.save(p2);
        
        // Update p1
        repository.save(p1Updated);

        List<TestProduct> all = repository.findAll();

        // The order should still be p1 then p2 (no rearrangement of p1 to the end)
        assertThat(all).containsExactly(p1Updated, p2);
    }

    @Test
    void shouldDeleteById() {
        var p1 = new TestProduct("p1", "Keyboard", 49.99);
        var p2 = new TestProduct("p2", "Mouse", 29.99);

        repository.save(p1);
        repository.save(p2);

        repository.deleteById("p1");

        assertThat(repository.findById("p1")).isEmpty();
        assertThat(repository.findAll()).containsExactly(p2);
    }

    @Test
    void shouldFilterEntitiesUsingPredicate() {
        var p1 = new TestProduct("p1", "Cheap Mouse", 9.99);
        var p2 = new TestProduct("p2", "Mechanical Keyboard", 89.99);
        var p3 = new TestProduct("p3", "Gaming Monitor", 299.99);

        repository.save(p1);
        repository.save(p2);
        repository.save(p3);

        // Filter for products that cost more than $50
        List<TestProduct> expensiveProducts = repository.find(product -> product.price() > 50.0);

        assertThat(expensiveProducts).containsExactly(p2, p3);
    }
}
