package com.example.skillboxthirdtask.storage;

import com.example.skillboxthirdtask.entity.Contact;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public interface ContactStorage {

    List<Contact> searchAll();

    Optional<Contact> searchById(Long id);

    Contact saveContact(Contact contact);

    Contact editContact(Contact contact);

    void removeContactById(Long id);

    void batchInsert(List<Contact> contacts);

    HashSet<Long> numbersId();

}
