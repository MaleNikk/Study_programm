package com.example.jwt_security.security;

import com.example.jwt_security.entity.AppUser;
import com.example.jwt_security.entity.User;
import com.example.jwt_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = (AppUser) userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User not found. Username is: " + username));
        return new AppUserDetails(user);
    }
}
