package learning.infrastructure;

import learning.domain.Money;
import learning.domain.Product;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JdbcProductRepositoryIT {

    // Singleton Testcontainer for fast startup
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    private static Connection connection;
    private JdbcProductRepository repository;

    @BeforeAll
    static void beforeAll() throws Exception {
        postgres.start();
        connection = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());

        // Initialize schema
        try (InputStream schemaStream = JdbcProductRepositoryIT.class.getResourceAsStream("/schema.sql")) {
            if (schemaStream == null) {
                throw new IllegalStateException("schema.sql not found");
            }
            String schemaSql = new String(schemaStream.readAllBytes());
            try (Statement statement = connection.createStatement()) {
                statement.execute(schemaSql);
            }
        }
    }

    @AfterAll
    static void afterAll() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        postgres.stop();
    }

    @BeforeEach
    void setUp() throws Exception {
        repository = new JdbcProductRepository(connection);
        // Truncate table to guarantee test isolation
        try (Statement statement = connection.createStatement()) {
            statement.execute("TRUNCATE TABLE products");
        }
    }

    @Test
    void shouldSaveAndFindProduct() {
        Product product = new Product("p1", "Laptop", new Money(BigDecimal.valueOf(999.99), "EUR"));
        repository.save(product);

        Optional<Product> found = repository.findById("p1");

        assertThat(found).isPresent();
        assertThat(found.get().name()).isEqualTo("Laptop");
        assertThat(found.get().price().amount()).isEqualByComparingTo(BigDecimal.valueOf(999.99));
        assertThat(found.get().price().currency()).isEqualTo("EUR");
    }

    @Test
    void shouldReturnEmptyOptionalWhenProductDoesNotExist() {
        Optional<Product> found = repository.findById("nonexistent");
        assertThat(found).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenSavingDuplicateId() {
        Product product1 = new Product("p1", "Laptop", new Money(BigDecimal.valueOf(999.99), "EUR"));
        repository.save(product1);

        Product product2 = new Product("p1", "Tablet", new Money(BigDecimal.valueOf(299.99), "EUR"));

        // Saving duplicate ID should throw a unique constraint SQLException (propagated or wrapped)
        assertThatThrownBy(() -> repository.save(product2));
    }

    @Test
    void shouldFindAllProducts() {
        Product p1 = new Product("p1", "Laptop", new Money(BigDecimal.valueOf(999.99), "EUR"));
        Product p2 = new Product("p2", "Tablet", new Money(BigDecimal.valueOf(299.99), "EUR"));
        repository.save(p1);
        repository.save(p2);

        List<Product> all = repository.findAll();

        assertThat(all).hasSize(2);
        assertThat(all).extracting(Product::id).containsExactlyInAnyOrder("p1", "p2");
    }
}
