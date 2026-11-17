package com.example.demo.controller;

import com.example.demo.dto.UserResponse;
import com.example.demo.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrentUserRestControlle {

    @GetMapping("/api/user")
    public UserResponse getCurrentUser(@AuthenticationPrincipal User user){
        return UserResponse.from(user);
    }
}
