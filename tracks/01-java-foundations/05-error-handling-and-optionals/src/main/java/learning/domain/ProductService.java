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
        Optional<Product> optionalProduct = repository.findById(productId);
        return optionalProduct.map(product -> discountService.applyDiscount(product.price(), policy));
    }
}
