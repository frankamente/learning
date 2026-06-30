package learning.generator;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.UUID;

// TODO: Annotate with @Scope("prototype")
@Component
public class RequestIdGenerator {
    private final String id = UUID.randomUUID().toString();

    public String id() {
        return id;
    }
}
