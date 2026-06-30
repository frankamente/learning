package learning.infrastructure;

import learning.domain.Money;
import learning.domain.Product;
import learning.domain.ProductRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcProductRepository implements ProductRepository {
    private final Connection connection;

    public JdbcProductRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Product product) {
        // TODO: Insert product using raw JDBC:
        // INSERT INTO products (id, name, price, currency) VALUES (?, ?, ?, ?)
    }

    @Override
    public Optional<Product> findById(String id) {
        // TODO: Query product by id using raw JDBC:
        // SELECT id, name, price, currency FROM products WHERE id = ?
        return Optional.empty();
    }

    @Override
    public List<Product> findAll() {
        // TODO: Query all products using raw JDBC:
        // SELECT id, name, price, currency FROM products
        return List.of();
    }
}
