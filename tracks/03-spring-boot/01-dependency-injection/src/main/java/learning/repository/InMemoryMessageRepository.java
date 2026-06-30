package learning.repository;

import learning.generator.RequestIdGenerator;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryMessageRepository implements MessageRepository {
    private final List<String> messages = new ArrayList<>();
    private final RequestIdGenerator idGenerator;

    public InMemoryMessageRepository(RequestIdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public void save(String message) {
        messages.add(message);
    }

    @Override
    public boolean exists(String message) {
        return messages.contains(message);
    }

    public RequestIdGenerator idGenerator() {
        return idGenerator;
    }
}
