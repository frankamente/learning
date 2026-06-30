package learning.infrastructure;

import learning.domain.Money;
import learning.domain.Product;
import learning.domain.ProductRepository;

import java.math.BigDecimal;
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
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO products (id, name, price, currency) VALUES (?, ?, ?, ?)")) {

                preparedStatement.setString(1, product.id());
                preparedStatement.setString(2, product.name());
                preparedStatement.setBigDecimal(3, product.price().amount());
                preparedStatement.setString(4, product.price().currency());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Product> findById(String id) {
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, name, price, currency FROM products WHERE id = ?")) {
                preparedStatement.setString(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String newId = resultSet.getString("id");
                        String name = resultSet.getString("name");
                        BigDecimal price = resultSet.getBigDecimal("price");
                        String currency = resultSet.getString("currency");
                        return Optional.of(new Product(newId, name, new Money(price, currency)));
                    }
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> findAll() {
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, name, price, currency FROM products")) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    List<Product> result = new ArrayList<>();
                    while (resultSet.next()) {
                        String newId = resultSet.getString("id");
                        String name = resultSet.getString("name");
                        BigDecimal price = resultSet.getBigDecimal("price");
                        String currency = resultSet.getString("currency");
                        Product product = new Product(newId, name, new Money(price, currency));
                        result.add(product);
                    }
                    return result;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
