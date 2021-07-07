package com.jpt21.socialmedia.controller;

import com.jpt21.socialmedia.model.Status;
import com.jpt21.socialmedia.service.HomeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api")
public class StatusController {

    private final HomeService homeService;

    @PostMapping("/postStatus")
    public String postStatus(@RequestBody Status status){
        if(!homeService.saveStatus(status)){
            return "Status upload failed";
        }else return "Status uploaded";
    }
}
