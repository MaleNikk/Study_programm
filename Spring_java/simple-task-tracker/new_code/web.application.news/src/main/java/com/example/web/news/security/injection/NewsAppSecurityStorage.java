package com.example.web.news.security.injection;

import com.example.web.news.security.model.AppSecurityModelEntity;
import com.example.web.news.security.model.NewsAppRoles;

public interface NewsAppSecurityStorage {

    NewsAppRoles showRole(Long user_id);

    AppSecurityModelEntity save(Long user_id, String role);

    boolean refactorRole(Long user_id, String role);

    boolean delete(Long user_id);
}
