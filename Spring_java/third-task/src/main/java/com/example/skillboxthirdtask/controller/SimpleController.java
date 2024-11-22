package com.example.skillboxthirdtask.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class SimpleController {

    @GetMapping("/example")
    public String startPage(Model model){
        model.addAttribute("name","Ivan Ivanov");

        List<String> departments = Arrays.asList("South","West","Center");

        model.addAttribute("departments", departments);

        User user = new User("Ivan", "ivanov@email.test");
        model.addAttribute("user", user);

        return "example/startpage.html";
    }

    @PostMapping("/example/save")
    public String saveData(@ModelAttribute("user") User user) {
        System.out.println("Saving user: ".concat(String.valueOf(user)));
        return "redirect:/example";
    }
    @GetMapping("example/footer")
    public String footer() {
        return "example/fragments/footer :: footer";
    }

    @GetMapping("/example/header")
    public String header() {
        return "example/fragments/header :: header";
    }
    @Data
    @AllArgsConstructor
    class User {
        private String name, email;
        @Override
        public String toString(){
            return MessageFormat.format("User saved : \n\tName: {0} \n\t Email: {1}", getName(), getEmail());
        }
    }
}
