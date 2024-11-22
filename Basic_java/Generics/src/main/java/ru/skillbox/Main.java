package ru.skillbox;

import java.util.ArrayList;

public class Main {
    protected static EmailNotification emailNotification;
    protected static SmsNotification smsNotification;
    protected static PushNotification pushNotification;
    protected static EmailNotificationSender emailNotificationSender;
    protected static SmsNotificationSender smsNotificationSender;
    protected static PushNotificationSender pushNotificationSender;


    public static void main(String[] args) {
        emailNotification = new EmailNotification(
                "Спасибо за регистрацию на сервисе!",
                "oleg@java.skillbox.ru, masha@java.skillbox.ru, yan@java.skillbox.ru",
                "Успешная регистрация!");
        emailNotificationSender = new EmailNotificationSender(emailNotification);
        emailNotificationSender.send(new ArrayList<>());

        smsNotification = new SmsNotification(
                "Спасибо за регистрацию на сервисе!",
                "+70001234567");
        smsNotificationSender = new SmsNotificationSender(smsNotification);
        smsNotificationSender.send(new ArrayList<>());

        pushNotification = new PushNotification(
                "Спасибо за регистрацию на сервисе!",
                "Успешная регистрация!",
                "o.yanovich",
                "\uD83D\uDC4B");
        pushNotificationSender = new PushNotificationSender(pushNotification);
        pushNotificationSender.send(new ArrayList<>());

    }
}
