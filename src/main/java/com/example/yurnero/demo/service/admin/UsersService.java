package com.example.yurnero.demo.service.admin;

import com.example.yurnero.demo.dto.UserDto;

import java.util.List;

public interface UsersService {
    UserDto getUserById(Long id);
    List<UserDto> getAllUsers();
    UserDto createUser(UserDto userDto);
    UserDto updateUser(Long id, UserDto userDto);
    UserDto deleteUser(Long id);
    UserDto createTeacher(UserDto userDto);
    boolean hasUserWithEmail(String email);
    List<UserDto> getAllTeachers();
}
