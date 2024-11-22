package com.example.web.news.security.injection;

import com.example.web.news.security.model.AppSecurityModelEntity;

public interface NewsAppSecurityService {

    AppSecurityModelEntity save(Long id, String role);

    boolean refactorRole(Long id, String role);

    boolean delete(Long user_id);

    boolean checkRoleUser(Long id);

    boolean checkRoleModerator(Long id);

    boolean checkRoleAdmin(Long id);
}
