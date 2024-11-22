package com.example.skillboxthirdtask.controller;

import com.example.skillboxthirdtask.entity.Contact;
import com.example.skillboxthirdtask.service.ContactsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@Controller
@RequestMapping
@RequiredArgsConstructor

public class ContactController {

    private final ContactsService contactsService;

    @GetMapping("/")
    public String allContacts(Model model) {
        model.addAttribute("contacts", contactsService.searchAll());
        return "startpage.html";
    }

    @GetMapping("/contact/create")
    public String addContact(Model model) {
        model.addAttribute("contact", new Contact());
        return "create";
    }

    @PostMapping("/contact/create")
    public String addComplete(@ModelAttribute Contact contact) {
        Random random = new Random();
        long id;
        do {
            id = random.nextLong(1, 100_000);
        } while (contactsService.numbersId().contains(id));
        contact.setId(id);
        contactsService.saveContact(contact);
        return "redirect:/";
    }

    @GetMapping("/contact/edit/{id}")
    public String editContact(@PathVariable Long id, Model model) {
        Contact contact = contactsService.searchById(id);
        if (contact != null) {
            model.addAttribute("contact", contact);
            return "edit";
        }
        return "redirect:/";
    }

    @PostMapping("/contact/edit")
    public String editComplete(@ModelAttribute Contact contact) {
        contactsService.editContact(contact);
        return "redirect:/";
    }

    @GetMapping("contact/delete/{id}")
    public String deleteContact(@PathVariable Long id) {
        contactsService.removeContactById(id);
        return "redirect:/";
    }
}
