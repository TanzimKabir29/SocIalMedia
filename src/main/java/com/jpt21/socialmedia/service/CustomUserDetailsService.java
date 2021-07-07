package com.jpt21.socialmedia.service;

import com.jpt21.socialmedia.model.UserAccount;
import com.jpt21.socialmedia.repository.UserAccountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userRepository;

    //Modified to load user by username, and if it fails, by email. User can use any one to login
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<UserAccount> userByUserName = userRepository.findByUserName(userName);
        if (userByUserName.isPresent()) {
            log.trace("User found by username.");
            return User.builder()
                    .username(userByUserName.get().getUserName())
                    .password(userByUserName.get().getPassword())
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(false)
                    .authorities(new SimpleGrantedAuthority("USER"))
                    .build();
        } else {
            Optional<UserAccount> userByEmail = userRepository.findByEmail(userName);
            if (userByEmail.isPresent()) {
                log.trace("User found by email.");
                return User.builder()
                        .username(userByEmail.get().getUserName())
                        .password(userByEmail.get().getPassword())
                        .accountExpired(false)
                        .accountLocked(false)
                        .credentialsExpired(false)
                        .disabled(false)
                        .authorities(new SimpleGrantedAuthority("USER"))
                        .build();
            } else {
                throw new UsernameNotFoundException("Username not found: " + userName);
            }
        }
    }
}
