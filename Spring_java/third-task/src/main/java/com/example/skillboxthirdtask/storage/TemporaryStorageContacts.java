package com.example.skillboxthirdtask.storage;

import com.example.skillboxthirdtask.entity.Contact;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@Data
@NoArgsConstructor
public class TemporaryStorageContacts implements ContactStorage {

    private final List<Contact> contacts = new ArrayList<>();
    private final HashSet<Long> numbersId = new HashSet<>();

    @Override
    public List<Contact> searchAll() {
        log.debug("Enjoy TemporaryStorageContacts method search all.");
        return getContacts();
    }

    @Override
    public Optional<Contact> searchById(Long id) {
        log.debug("Enjoy TemporaryStorageContacts method search by Id: {}", id);
        return getContacts().stream().filter(contact -> id.equals(contact.getId())).findFirst();
    }

    @Override
    public Contact saveContact(Contact contact) {
        log.debug("Enjoy TemporaryStorageContacts method save contact: {}", contact);
        contact.setId(System.currentTimeMillis());
        getContacts().add(contact);
        return contact;
    }

    @Override
    public Contact editContact(Contact contact) {
        log.debug("Enjoy TemporaryStorageContacts method edit contact: {}", contact);
        Contact editedContact = searchById(contact.getId()).orElse(null);
        if (editedContact != null) {
            editedContact.setId(contact.getId());
            editedContact.setName(contact.getName());
            editedContact.setSurname(contact.getSurname());
            editedContact.setEmail(contact.getEmail());
            editedContact.setPhone(contact.getPhone());
        }
        return editedContact;
    }

    @Override
    public void removeContactById(Long id) {
        log.debug("Enjoy TemporaryStorageContacts method remove contact by Id: {}", id);
        searchById(id).ifPresent(getContacts()::remove);

    }

    @Override
    public void batchInsert(List<Contact> contacts) {
        log.debug("Enjoy TemporaryStorageContacts method batchInsert.");
        getContacts().addAll(contacts);
    }

    @Override
    public HashSet<Long> numbersId() {
        for (Contact contact:getContacts()){
            getNumbersId().add(contact.getId());
        }
        return getNumbersId();
    }
}
