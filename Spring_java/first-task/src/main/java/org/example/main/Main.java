package org.example.main;

import org.example.config.AppConfig;
import org.example.component.ManagementStorage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        context.getBean(ManagementStorage.class).start();
    }
}