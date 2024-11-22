package com.fluxing.security.configuration.security;

import com.fluxing.security.dto.UserJwt;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class AppUserDetailsJwt implements UserDetails {

    private final UserJwt userJwt;

    public AppUserDetailsJwt(UserJwt userJwt) {
        this.userJwt = userJwt;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userJwt.getRoles()
                .stream()
                .map(entity -> new SimpleGrantedAuthority(entity.name()))
                .toList();
    }

    @Override
    public String getPassword() {
        return userJwt.getPassword();
    }

    @Override
    public String getUsername() {
        return userJwt.getUsername();
    }

    public String getEmail(){
        return userJwt.getEmail();
    }

    public Long getId(){
        return userJwt.getId();
    }

}
