package com.example.skillboxthirdtask.service;

import com.example.skillboxthirdtask.entity.Contact;
import com.example.skillboxthirdtask.storage.ContactStorage;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Data
@Service
@Slf4j
@RequiredArgsConstructor
public class ContactService implements ContactsService {

    private final ContactStorage contactStorage;


    @Override
    public List<Contact> searchAll() {
        log.debug("Enjoy service method search all");
        return contactStorage.searchAll();
    }

    @Override
    public Contact searchById(Long id) {
        log.debug("Enjoy service method search by id: {}", id);
        return contactStorage.searchById(id).orElse(null);
    }

    @Override
    public Contact saveContact(Contact contact) {
        log.debug("Enjoy method service save contact: {} ", contact);
        return contactStorage.saveContact(contact);
    }

    @Override
    public Contact editContact(Contact contact) {
        log.debug("Enjoy service method edit contact: {}", contact);
        return contactStorage.editContact(contact);
    }

    @Override
    public void removeContactById(Long id) {
        log.debug("Enjoy service method remove contact by id: {}", id);
        contactStorage.removeContactById(id);
    }

    @Override
    public void batchInsert(List<Contact> contacts) {
        log.debug("Enjoy service method batch insert.");
        contactStorage.batchInsert(contacts);
    }

    @Override
    public HashSet<Long> numbersId(){
        log.debug("Enjoy service method numbersId.");
        return contactStorage.numbersId();
    }
}
