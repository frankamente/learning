package learning.service;

import learning.Application;
import learning.sender.SmsNotificationSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("sms")
class NotificationServiceSmsIT {

    @Autowired
    private NotificationService notificationService;

    @Test
    void shouldResolveSmsSenderWhenSmsProfileIsActive() {
        assertThat(notificationService.sender())
                .isInstanceOf(SmsNotificationSender.class);
    }
}
