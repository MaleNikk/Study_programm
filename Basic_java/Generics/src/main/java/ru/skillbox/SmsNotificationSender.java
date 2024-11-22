package ru.skillbox;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.notification_sender.NotificationSender;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class SmsNotificationSender implements NotificationSender<SmsNotification> {

    private final SmsNotification smsNotification;

    @Override
    public void send(SmsNotification SmsNotification) {
        System.out.println(SmsNotification.toString());
    }

    @Override
    public void send(List<SmsNotification> smsNotifications) {
        smsNotifications.add(smsNotification);
        for (SmsNotification smsNotification : smsNotifications) {
            System.out.println(smsNotification);
        }
    }

}
