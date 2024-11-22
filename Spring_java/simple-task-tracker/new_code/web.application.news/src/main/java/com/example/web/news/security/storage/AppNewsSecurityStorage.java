package com.example.web.news.security.storage;

import com.example.web.news.security.injection.NewsAppSecurityRepository;
import com.example.web.news.security.injection.NewsAppSecurityStorage;
import com.example.web.news.security.model.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;


@Repository
@Qualifier
@Data
@Slf4j
public class AppNewsSecurityStorage implements NewsAppSecurityStorage {

    @Autowired
    private NewsAppSecurityRepository securityRepository;

    @Override
    public NewsAppRoles showRole(Long id) {
        log.info("Work method show role from storage management class.");
        return findObject(id).getRole();
    }

    @Override
    public AppSecurityModelEntity save(Long user_id, String role) {
        log.info("Work method save from storage management class.");
        if (!checkPresent(user_id)) {
            return (role.strip().equalsIgnoreCase(NewsAppRoles.ADMIN.toString())) ?
                    getSecurityRepository().save(getEntity(user_id, NewsAppRoles.ADMIN)) :
                    role.strip().equalsIgnoreCase(NewsAppRoles.MODERATOR.toString()) ?
                            getSecurityRepository().save(getEntity(user_id, NewsAppRoles.MODERATOR)) :
                            getSecurityRepository().save(getEntity(user_id, NewsAppRoles.USER));
        }
        return getEntity(0L,NewsAppRoles.USER);
    }

    @Override
    public boolean refactorRole(Long id, String role) {
        log.info("Work method refactorRole from storage management class.");
        if (checkPresent(id)) {
            getSecurityRepository().delete(findObject(id));
            save(id, role);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Long user_id) {
        log.info("Work method delete from storage management class.");
        if (checkPresent(user_id)){
            getSecurityRepository().delete(findObject(user_id));
            return true;
        }
        return false;
    }

    private AppSecurityModelEntity getEntity(Long user_id, NewsAppRoles role) {
        log.info("Work method getEntity from storage management class.");
        return new AppSecurityModelEntity(UUID.randomUUID().toString(),user_id, Date.from(Instant.now()), role);
    }

    private AppSecurityModelEntity findObject(Long user_id) {
        log.info("Work method findObject from storage management class.");
        return checkPresent(user_id) ? getSecurityRepository().findAll()
                .stream()
                .filter(user -> user.getUser_id().equals(user_id))
                .findFirst()
                .get() :
                getEntity(0L, NewsAppRoles.USER);
    }

    private boolean checkPresent(Long user_id) {
        log.info("Work method checkPresent from storage management class.");
        return getSecurityRepository()
                .findAll()
                .stream()
                .anyMatch(user -> user.getUser_id().equals(user_id));
    }
}
