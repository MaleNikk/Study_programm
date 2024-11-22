package ru.skillbox;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.notification.Notification;

@AllArgsConstructor
@Getter
@Setter
public class PushNotification implements Notification {

    private String message;
    private String title;
    private String receiver;
    private String emoji;

    public String formattedMessage() {
        return message = emoji + " " + message;
    }

    @Override
    public String toString() {
        return "Push" + '\n' +
                "title: " + title + '\n' +
                "receiver: " + receiver + '\n' +
                "message: " + formattedMessage();
    }
}
