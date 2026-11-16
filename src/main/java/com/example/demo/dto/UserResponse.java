package com.example.demo.dto;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import java.util.Set;
import java.util.stream.Collectors;

public record UserResponse(Long id, String name, String surname, String email, String username, Set<String> roles){

     public static UserResponse from (User user){
         return new UserResponse(
                 user.getId(),
                 user.getName(),
                 user.getSurname(),
                 user.getEmail(),
                 user.getUsername(),
                 user.getRoles().stream()
                     .map(Role::getName)
                     .collect(Collectors.toSet())
         );
     }
 }