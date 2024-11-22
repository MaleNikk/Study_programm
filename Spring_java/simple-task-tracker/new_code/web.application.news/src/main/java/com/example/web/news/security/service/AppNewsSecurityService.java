package com.example.web.news.security.service;

import com.example.web.news.security.injection.NewsAppSecurityService;
import com.example.web.news.security.injection.NewsAppSecurityStorage;
import com.example.web.news.security.model.AppSecurityModelEntity;
import com.example.web.news.security.model.NewsAppRoles;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Getter
public class AppNewsSecurityService implements NewsAppSecurityService {

    @Autowired
    private NewsAppSecurityStorage storage;

    @Override
    public AppSecurityModelEntity save(Long id, String role) {
        log.info("Init service method save at system time: {}", System.currentTimeMillis());
        return getStorage().save(id,role);
    }

    @Override
    public boolean refactorRole(Long id, String role) {
        log.info("Init service method refactorRole at system time: {}", System.currentTimeMillis());
        return getStorage().refactorRole(id, role);
    }

    @Override
    public boolean delete(Long user_id) {
        log.info("Init service method delete at system time: {}", System.currentTimeMillis());
        return getStorage().delete(user_id);
    }

    @Override
    public boolean checkRoleUser(Long id) {
        log.info("Init service method checkRoleUser at system time: {}", System.currentTimeMillis());
        return NewsAppRoles.USER.equals(getStorage().showRole(id));
    }

    @Override
    public boolean checkRoleModerator(Long id) {
        log.info("Init service method checkRoleModerator at system time: {}", System.currentTimeMillis());
        return NewsAppRoles.MODERATOR.equals(getStorage().showRole(id));
    }

    @Override
    public boolean checkRoleAdmin(Long id) {
        log.info("Init service method checkRoleAdmin at system time: {}", System.currentTimeMillis());
        return NewsAppRoles.ADMIN.equals(getStorage().showRole(id));
    }
}
