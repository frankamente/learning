package learning.domain;

import learning.Money;
import learning.domain.discount.DiscountPolicy;
import learning.domain.discount.DiscountService;
import java.util.Optional;

public final class ProductService {
    private final InMemoryRepository<Product, String> repository;
    private final DiscountService discountService;

    public ProductService(InMemoryRepository<Product, String> repository, DiscountService discountService) {
        this.repository = repository;
        this.discountService = discountService;
    }

    public Optional<Money> findDiscountedPriceForProduct(String productId, DiscountPolicy policy) {
        // TODO: Find product in repository, map its price to the discounted price using discountService,
        // and return Optional<Money> utilizing a pure Optional pipeline (no manual 'if' statements or 'isPresent' checks).
        return Optional.empty();
    }
}
