package com.example.demo.dto;

import jakarta.validation.constraints.*;

import java.util.Set;

public record UserRequest (
    @NotBlank(message = "Name must not be blank")
    @Size(max = 30, message = "Name must contain at most 30 characters")
    @Pattern(regexp = "^[\\p{L}]+$",
            message = "Name must contain only letters")
    String name,

    @NotBlank(message = "Surname must not be blank")
    @Size(max = 30, message = "Surname must contain at most 30 characters")
    @Pattern(regexp = "^[\\p{L}]+$",
            message = "Surname must contain only letters")
    String surname,

    @NotBlank(message = "Email must not be blank")
    @Email
    @Size(max = 250, message = "Email must contain at most 250 characters")
    String email,

    @NotBlank(message = "Username must not be blank")
    String username,

    String password,

    @NotEmpty
    Set <Long> roleIds
){
}