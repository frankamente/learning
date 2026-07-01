package learning.sender;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("email")
@Component
public class EmailNotificationSender implements NotificationSender {
    @Override
    public void send(String recipient, String message) {
        System.out.println("Sending email to " + recipient + ": " + message);
    }
}
