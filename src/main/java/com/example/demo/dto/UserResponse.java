package com.example.demo.dto;

import com.example.demo.model.User;
import java.util.Set;

 public record UserResponse(Long id, String name, String surname, String email, String username, Set<String> roles){

     public static UserResponse from (User user){
         return new UserResponse(user.getId(), user.getName(), user.getSurname(), user.getEmail(), user.getUsername(),null);
     }
 }