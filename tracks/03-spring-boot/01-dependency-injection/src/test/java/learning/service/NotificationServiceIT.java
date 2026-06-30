package learning.service;

import learning.Application;
import learning.generator.RequestIdGenerator;
import learning.repository.InMemoryMessageRepository;
import learning.sender.EmailNotificationSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("email")
class NotificationServiceIT {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private InMemoryMessageRepository repository;

    @Test
    void contextLoadsAndBeansAreResolved() {
        assertThat(context).isNotNull();
        assertThat(notificationService).isNotNull();
    }

    @Test
    void shouldResolveEmailSenderWhenEmailProfileIsActive() {
        assertThat(notificationService.sender())
                .isInstanceOf(EmailNotificationSender.class);
    }

    @Test
    void shouldVerifyPrototypeScopeOfRequestIdGenerator() {
        RequestIdGenerator serviceGenerator = notificationService.idGenerator();
        RequestIdGenerator repoGenerator = repository.idGenerator();

        assertThat(serviceGenerator).isNotNull();
        assertThat(repoGenerator).isNotNull();

        // Since RequestIdGenerator should be Prototype scoped, they must be distinct instances
        assertThat(serviceGenerator).isNotSameAs(repoGenerator);
        assertThat(serviceGenerator.id()).isNotEqualTo(repoGenerator.id());
    }
}
