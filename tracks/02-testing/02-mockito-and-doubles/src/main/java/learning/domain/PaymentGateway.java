package learning.domain;

public interface PaymentGateway {
    PaymentResult charge(Order order);
}
