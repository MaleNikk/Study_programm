package com.fluxing.security.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webflux/test/app")
public class ApplicationTestController {

    private final Logger log = LoggerFactory.getLogger(ApplicationTestController.class);

    @GetMapping("/all")
    public String allAccess(){
        log.info("Init all access!");
        return "Public response data.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userAccess(){
        log.info("Init user access!");
        return "Admin response data.";
    }

    @GetMapping("/manager")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public String managerAccess(){
        log.info("Init manager access!");
        return "Admin, Manager response data.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public String adminAccess(){
        log.info("Init admin access!");
        return "Admin, Manager, User response data.";
    }

}
