package com.example.yurnero.demo.dto;

import com.example.yurnero.demo.enums.UserRole;
import lombok.Data;

@Data
public class AuthenticationResponse {
    private String jwt;
    private Long userId;
    private UserRole userRole;

}
