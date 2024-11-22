package ru.skillbox;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.notification_sender.NotificationSender;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class PushNotificationSender implements NotificationSender<PushNotification> {

    private final PushNotification pushNotification;

    @Override
    public void send(PushNotification pushNotification) {
        System.out.println(pushNotification.toString());
    }

    @Override
    public void send(List<PushNotification> pushNotifications) {
        pushNotifications.add(pushNotification);
        for (PushNotification pushNotification : pushNotifications) {
            System.out.println(pushNotification);
        }
    }
}
