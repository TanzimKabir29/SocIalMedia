package com.jpt21.socialmedia.service;

import com.jpt21.socialmedia.model.CustomUserDetails;
import com.jpt21.socialmedia.model.User;
import com.jpt21.socialmedia.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserName(userName);
        if (user.isPresent()) {
            log.info("User found by username.");
            return new CustomUserDetails(user.get());
        } else {
            Optional<User> userEmail = userRepository.findByEmail(userName);
            if (userEmail.isPresent()) {
                log.info("User found by email.");
                return new CustomUserDetails(userEmail.get());
            } else {
                throw new UsernameNotFoundException("Username not found: " + userName);
            }
        }
    }
}
