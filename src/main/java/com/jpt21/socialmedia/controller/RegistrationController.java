package com.jpt21.socialmedia.controller;

import com.jpt21.socialmedia.model.UserAccount;
import com.jpt21.socialmedia.repository.UserAccountRepository;
import com.jpt21.socialmedia.service.HomeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class RegistrationController {

    private final HomeService homeService;

    @PostMapping(value = "/registration")
    public ResponseEntity<?> registerUser(@RequestBody UserAccount userAccount){
        return ResponseEntity.ok(homeService.registerUser(userAccount));
    }
}
