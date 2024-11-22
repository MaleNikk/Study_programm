package com.example.web.news.controller;

import com.example.web.news.entity.ReaderEntity;
import com.example.web.news.injections.service.ServiceReaders;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@Getter
@Controller
@RequestMapping
@RequiredArgsConstructor
public class ReadersController {
    private final ServiceReaders<ReaderEntity> serviceReaders;
    @GetMapping("/readers/readership")
    public String readership(Model model){
        model.addAttribute("readers",getServiceReaders().searchAll());
        return "readers/showReaders";
    }

    @GetMapping("/readers/registration")
    public String registration(Model model){
        ReaderEntity readerEntity = new ReaderEntity();
        model.addAttribute("readerEntity",readerEntity);
        return "readers/registration";
    }
    @PostMapping("/readers/registration")
    public String completeRegistration(@ModelAttribute ReaderEntity readerEntity, Model model){
        AtomicLong atomicLong = new AtomicLong(getServiceReaders().searchAll().size());
        long id;
        do { id = atomicLong.getAndIncrement(); } while (getServiceReaders().numbersId().contains(id));
        getServiceReaders().numbersId().add(id);
        readerEntity.setId(id);
        getServiceReaders().save(readerEntity);
        model.addAttribute("readers",getServiceReaders().searchAll());
        return "readers/showReaders";
    }

    @GetMapping("/readers/editReader/{id}")
    public String editReader(@PathVariable Long id, Model model){
        ReaderEntity readerEntity = getServiceReaders().searchById(id);
        if (readerEntity == null){
            return "redirect:/";
        } else {
            model.addAttribute("readerEntity",readerEntity);
            return "/readers/editReader";
        }
    }
    @PostMapping("/readers/editReader")
    public String completeEditReader(@ModelAttribute ReaderEntity readerEntity,Model model){
        getServiceReaders().edit(readerEntity);
        model.addAttribute("readers",getServiceReaders().searchAll());
        return "readers/showReaders";
    }

    @GetMapping("/readers/deleteReader/{id}")
    public String removeReader(@PathVariable Long id, Model model){
        getServiceReaders().removeById(id);
        model.addAttribute("readers",getServiceReaders().searchAll());
        return "readers/showReaders";
    }
    @GetMapping("/readers/comeback")
    public String comeBack(){
        return "redirect:/";
    }
    private boolean checkPin(){
        return false;
    }
}
