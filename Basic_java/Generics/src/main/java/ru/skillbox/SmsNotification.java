package ru.skillbox;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.notification.Notification;

@AllArgsConstructor
@Getter
@Setter
public class SmsNotification implements Notification {
    private String message;
    private String receivers;

    @Override
    public String formattedMessage() {        
        return message;
    }

    @Override
    public String toString() {
        return "Sms" + '\n' +
                "receivers: " + receivers + '\n' +
                "message: " + formattedMessage();
    }
}
