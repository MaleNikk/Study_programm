package ru.skillbox;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.notification.Notification;

@AllArgsConstructor
@Setter
@Getter
public class EmailNotification implements Notification {
    private String message;
    private String receivers;
    private String subject;

    @Override
    public String formattedMessage() {       
        return message = "<p>" + message + "</p>";
    }
   
    @Override
    public String toString() {
        return "Email" + '\n' +
                "subject: " + subject + '\n' +
                "receivers: " + receivers + '\n' +
                "message: " + formattedMessage();
    }     
}
