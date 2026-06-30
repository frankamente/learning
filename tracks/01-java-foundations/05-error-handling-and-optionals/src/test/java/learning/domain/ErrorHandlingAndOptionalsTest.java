package learning.domain;

import learning.Money;
import learning.domain.discount.DiscountFactory;
import learning.domain.discount.DiscountPolicy;
import learning.domain.discount.DiscountService;
import learning.domain.exception.DuplicateKeyException;
import learning.domain.exception.EntityNotFoundException;
import learning.domain.result.Result;
import learning.domain.result.Success;
import learning.domain.result.Failure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ErrorHandlingAndOptionalsTest {

    private InMemoryRepository<Product, String> repository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        repository = new InMemoryRepository<>();
        var discountService = new DiscountService();
        productService = new ProductService(repository, discountService);
    }

    @Test
    void shouldThrowDuplicateKeyExceptionOnStrictSaveDuplicate() {
        var product = new Product("p1", "Keyboard", new Money(BigDecimal.valueOf(100), "EUR"));
        repository.save(product);

        assertThatThrownBy(() -> repository.saveStrict(product))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessageContaining("p1");
    }

    @Test
    void shouldGetOrThrowWhenProductExists() {
        var product = new Product("p1", "Keyboard", new Money(BigDecimal.valueOf(100), "EUR"));
        repository.save(product);

        Product result = repository.getOrThrow("p1");
        assertThat(result).isEqualTo(product);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenProductDoesNotExist() {
        assertThatThrownBy(() -> repository.getOrThrow("unknown"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("unknown");
    }

    @Test
    void shouldReturnSuccessOnTrySaveStrictNew() {
        var product = new Product("p1", "Keyboard", new Money(BigDecimal.valueOf(100), "EUR"));

        Result<Product> result = repository.trySave(product);

        assertThat(result).isInstanceOf(Success.class);
        assertThat(((Success<Product>) result).value()).isEqualTo(product);
        assertThat(repository.findById("p1")).isPresent().hasValue(product);
    }

    @Test
    void shouldReturnFailureOnTrySaveStrictDuplicate() {
        var product = new Product("p1", "Keyboard", new Money(BigDecimal.valueOf(100), "EUR"));
        repository.save(product);

        Result<Product> result = repository.trySave(product);

        assertThat(result).isInstanceOf(Failure.class);
        var failure = (Failure<Product>) result;
        assertThat(failure.errorMessage()).contains("p1");
        assertThat(failure.cause()).isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void shouldFindDiscountedPriceForProductUsingOptionalPipeline() {
        var product = new Product("p1", "Keyboard", new Money(BigDecimal.valueOf(100), "EUR"));
        repository.save(product);

        DiscountPolicy policy = DiscountFactory.percentage(BigDecimal.TEN);
        Optional<Money> priceOpt = productService.findDiscountedPriceForProduct("p1", policy);

        assertThat(priceOpt).isPresent();
        assertThat(priceOpt.get().amount()).isEqualByComparingTo(BigDecimal.valueOf(90));
    }

    @Test
    void shouldReturnEmptyOptionalWhenProductNotFoundInServicePipeline() {
        DiscountPolicy policy = DiscountFactory.percentage(BigDecimal.TEN);
        Optional<Money> priceOpt = productService.findDiscountedPriceForProduct("unknown", policy);

        assertThat(priceOpt).isEmpty();
    }
}
