package learning.domain;

public interface InventoryService {
    void reserve(Order order) throws OutOfStockException;
    void release(Order order);
}
