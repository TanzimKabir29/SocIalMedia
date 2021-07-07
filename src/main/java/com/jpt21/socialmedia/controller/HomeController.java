package com.jpt21.socialmedia.controller;

import com.jpt21.socialmedia.service.HomeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Arrays;

@Controller
@AllArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }

    @GetMapping("/register")
    public String getRegistrationPage(){
        return "registration";
    }

    @GetMapping("/home")
    public String getHomePage(Model model) {
        model.addAttribute("statusList", homeService.getStatusList());
        return "index";
    }
}
