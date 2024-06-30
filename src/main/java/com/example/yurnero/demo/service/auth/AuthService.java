package com.example.yurnero.demo.service.auth;

import com.example.yurnero.demo.dto.SignupRequest;
import com.example.yurnero.demo.dto.UserDto;

public interface AuthService {
    UserDto signupUser(SignupRequest signupRequest);
    boolean hasUserWithEmail(String email);
}
