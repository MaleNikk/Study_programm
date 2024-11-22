package com.example.skillboxthirdtask.listener;

import com.example.skillboxthirdtask.entity.Contact;
import com.example.skillboxthirdtask.service.ContactsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataBaseListener {

    private final ContactsService contactsService;

    @EventListener(ApplicationStartedEvent.class)
    public void generateContacts() {
        log.info("Generate default contacts.");

        List<Contact> contacts = new ArrayList<>();
        Random random = new Random();
        if (contactsService.searchAll().isEmpty()) {
            contacts.add(new Contact(random.nextLong(1, 100), "Dasha", "Zueva",
                    "dasha.zueva@test.email", "+5677485645"));
            contacts.add(new Contact(random.nextLong(101, 200), "Roy", "Villy",
                    "roy.villy@test.email", "+486583975"));
            contacts.add(new Contact(random.nextLong(201, 300), "Vika", "Bokova",
                    "vika.bokova@test.email", "+549284657834"));
            contacts.add(new Contact(random.nextLong(301, 400), "Misha", "Zaicev",
                    "misha.zaicev@test.email", "+549389473"));
            contacts.add(new Contact(random.nextLong(401, 500), "Jonn", "Fury",
                    "jonn.fury@test.email", "+978587746"));
            contacts.add(new Contact(random.nextLong(501, 600), "Luchia", "Gremm",
                    "lichia.gremm@test.email", "+102394874"));
            contacts.add(new Contact(random.nextLong(601, 700), "Denis", "Trusov",
                    "denis.trusov@test.email", "+3958476382"));
            contactsService.batchInsert(contacts);
        }
    }
}
