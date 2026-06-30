package learning.service;

import learning.generator.RequestIdGenerator;
import learning.repository.MessageRepository;
import learning.sender.NotificationSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final MessageRepository repository;
    private final NotificationSender sender;
    private final RequestIdGenerator idGenerator;

    public NotificationService(
            MessageRepository repository,
            NotificationSender sender,
            RequestIdGenerator idGenerator
    ) {
        this.repository = repository;
        this.sender = sender;
        this.idGenerator = idGenerator;
    }

    public void sendNotification(String recipient, String message) {
        repository.save(message);
        sender.send(recipient, message);
    }

    public MessageRepository repository() {
        return repository;
    }

    public NotificationSender sender() {
        return sender;
    }

    public RequestIdGenerator idGenerator() {
        return idGenerator;
    }
}
