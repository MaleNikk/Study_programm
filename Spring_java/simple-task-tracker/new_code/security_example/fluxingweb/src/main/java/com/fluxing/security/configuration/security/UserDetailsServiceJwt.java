package com.fluxing.security.configuration.security;

import com.fluxing.security.dto.UserJwt;
import com.fluxing.security.repository.UserRepositoryJwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceJwt implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceJwt.class);

    @Autowired
    private UserRepositoryJwt userRepositoryJwt;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Init check search user by username at system time: {}!", System.currentTimeMillis());

        UserJwt userJwt = userRepositoryJwt.findByUsername(username).orElse(null);

        if (userJwt != null) {
            return new AppUserDetailsJwt(userJwt);
        }

        throw new UsernameNotFoundException("User not found! Try again or register!");
    }
}
