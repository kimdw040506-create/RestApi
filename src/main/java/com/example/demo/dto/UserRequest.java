package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UserRequest (
    @NotBlank(message = "Name must not be blank")
    @Size(max = 30, message = "Name must contain at most 30 characters")
    @Pattern(regexp = "^[\\p{L}]+$")
    String name,

    @NotBlank(message = "Surname must not be blank")
    @Size(max = 30, message = "Surname must contain at most 30 characters")
    @Pattern(regexp = "^[\\p{L}]+$")
    String surname,

    @NotBlank(message = "Email must not be blank")
    @Email
    @Size(max = 150, message = "Email must contain at most 150 characters")
    String email,

    @NotBlank(message = "Username must not be blank")
    String username,

    String password,

    Set <Long> roleIds
){
}