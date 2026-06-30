package learning.repository;

public interface MessageRepository {
    void save(String message);
    boolean exists(String message);
}
