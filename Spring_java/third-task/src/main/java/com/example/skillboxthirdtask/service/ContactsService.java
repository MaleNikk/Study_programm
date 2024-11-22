package com.example.skillboxthirdtask.service;

import com.example.skillboxthirdtask.entity.Contact;

import java.util.HashSet;
import java.util.List;

public interface ContactsService {
    List<Contact> searchAll();

    Contact searchById(Long id);

    Contact saveContact(Contact contact);

    Contact editContact(Contact contact);

    void removeContactById(Long Id);

    void batchInsert(List<Contact> contacts);

    HashSet<Long> numbersId();
}
