package com.example.yurnero.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthenticationRequest {
    @Email(message = "Email should be valid")
    private String email;
    @NotNull(message = "Password cannot be null")
    private String password;
}
