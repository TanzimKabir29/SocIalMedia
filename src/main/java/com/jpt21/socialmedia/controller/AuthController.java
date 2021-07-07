package com.jpt21.socialmedia.controller;

import com.jpt21.socialmedia.model.CustomUserDetails;
import com.jpt21.socialmedia.model.auth.AuthRequest;
import com.jpt21.socialmedia.model.auth.AuthResponse;
import com.jpt21.socialmedia.service.CustomUserDetailsService;
import com.jpt21.socialmedia.service.JwtUtilities;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtilities jwtUtilities;

    @PostMapping(value = "/authenticate")
    public Callable<ResponseEntity<?>> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        return () -> {
            log.info("Received login request from {}", authRequest.getUsername());
            authenticate(authRequest.getUsername(), authRequest.getPassword());
            final UserDetails userDetails = customUserDetailsService.loadUserByUsername(authRequest.getUsername());
            final String jwt = jwtUtilities.generateToken(userDetails);
            return ResponseEntity.ok(new AuthResponse(jwt));
        };
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException de) {
            throw new Exception("USER_DISABLED");
        } catch (BadCredentialsException bce) {
//            CompletableFuture.runAsync(() -> authService.updateWrongPin(username));
            throw new Exception("INVALID_CREDENTIALS");
        }
    }
}
