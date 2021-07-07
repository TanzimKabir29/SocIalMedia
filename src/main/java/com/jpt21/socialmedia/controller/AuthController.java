package com.jpt21.socialmedia.controller;

import com.jpt21.socialmedia.model.CustomUserDetails;
import com.jpt21.socialmedia.model.auth.AuthRequest;
import com.jpt21.socialmedia.model.auth.AuthResponse;
import com.jpt21.socialmedia.service.CustomUserDetailsService;
import com.jpt21.socialmedia.service.JwtUtilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtilities jwtUtilities;

    public AuthController(AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService, JwtUtilities jwtUtilities){
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtilities = jwtUtilities;
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        log.info("Received login request from {}", authRequest.getUsername());
        authenticate(authRequest.getUsername(),authRequest.getPassword());
        final CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtUtilities.generateToken(customUserDetails);
        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
//            CompletableFuture.runAsync(() -> authService.updateWrongPin(username));
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
