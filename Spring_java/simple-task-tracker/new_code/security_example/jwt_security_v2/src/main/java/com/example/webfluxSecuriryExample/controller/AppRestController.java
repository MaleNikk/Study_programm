package com.example.webfluxSecuriryExample.controller;

import com.example.webfluxSecuriryExample.mapper.MappingEntity;
import com.example.webfluxSecuriryExample.model.AppRequestModel;
import com.example.webfluxSecuriryExample.model.AppResponseModel;
import com.example.webfluxSecuriryExample.model.UserModel;
import com.example.webfluxSecuriryExample.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import com.example.webfluxSecuriryExample.security.AppDataPrincipal;
import com.example.webfluxSecuriryExample.security.AppSecurityService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/webflux/test/security")
public class AppRestController {

    private final AppSecurityService securityService;

    private final UserService userService;

    private final MappingEntity userMapper;

    @PostMapping("/register")
    public Mono<UserModel> register(@RequestBody UserModel model) {
        log.info("init method register at system time: {}", System.currentTimeMillis());
        return userService.registerUser(model);
    }

    @PostMapping("/login")
    public Mono<AppResponseModel> login(@RequestBody AppRequestModel requestModel) {
        return securityService.authenticate(requestModel.getUserName(), requestModel.getPassword())
                .flatMap(appTokenDetails -> Mono.just(
                  AppResponseModel.builder()
                          .userId(appTokenDetails.getUserId())
                          .token(appTokenDetails.getToken())
                          .issueAt(appTokenDetails.getIssueAt())
                          .expireAt(appTokenDetails.getExpireAt()).build()
                ));
    }

    @GetMapping("/info")
    public Mono<UserModel> getUserInfo(Authentication authentication) {
        AppDataPrincipal principal = (AppDataPrincipal) authentication.getPrincipal();
        return userService.getUserById(String.valueOf(principal.getId()));

    }
}
