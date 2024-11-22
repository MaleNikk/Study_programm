package ru.skillbox;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.notification.Notification;
import ru.skillbox.notification_sender.NotificationSender;

import java.util.List;
@AllArgsConstructor
@Getter
@Setter
public class EmailNotificationSender implements NotificationSender<EmailNotification> {
    private final EmailNotification emailNotification;

    @Override
    public void send(EmailNotification EmailNotification) {
        System.out.println(EmailNotification.toString());
    }

    @Override
    public void send(List<EmailNotification> emailNotifications) {
        emailNotifications.add(emailNotification);
        for (EmailNotification emailNotification : emailNotifications) {
            System.out.println(emailNotification);
        }
    }
}
