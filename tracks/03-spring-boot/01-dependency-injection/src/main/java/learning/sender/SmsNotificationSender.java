package learning.sender;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("sms")
@Component
public class SmsNotificationSender implements NotificationSender {
    @Override
    public void send(String recipient, String message) {
        System.out.println("Sending SMS to " + recipient + ": " + message);
    }
}
