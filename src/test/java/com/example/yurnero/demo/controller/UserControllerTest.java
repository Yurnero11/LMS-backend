package com.example.yurnero.demo.controller;

import com.example.yurnero.demo.controller.admin.UserController;
import com.example.yurnero.demo.dto.UserDto;
import com.example.yurnero.demo.service.admin.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserControllerTest  {
    @Mock
    private UsersService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserById_Success() {
        UserDto userDto = new UserDto();
        when(userService.getUserById(anyLong())).thenReturn(userDto);

        ResponseEntity<UserDto> responseEntity = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userDto, responseEntity.getBody());
    }

    @Test
    void getAllUsers_Success() {
        List<UserDto> users = Collections.singletonList(new UserDto());
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<UserDto>> responseEntity = userController.getAllUsers();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(users, responseEntity.getBody());
    }

    @Test
    void getAllTeachers_Success() {
        List<UserDto> users = Collections.singletonList(new UserDto());
        when(userService.getAllTeachers()).thenReturn(users);

        ResponseEntity<List<UserDto>> responseEntity = userController.getAllTeachers();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(users, responseEntity.getBody());
    }



    @Test
    void createTeacher_Success() {
        UserDto userDto = new UserDto();
        when(userService.createTeacher(any(UserDto.class))).thenReturn(userDto);

        ResponseEntity<UserDto> responseEntity = userController.createTeacher(userDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(userDto, responseEntity.getBody());
    }

    @Test
    void updateUser_Success() {
        UserDto userDto = new UserDto();
        when(userService.updateUser(anyLong(), any(UserDto.class))).thenReturn(userDto);

        ResponseEntity<UserDto> responseEntity = userController.updateUser(1L, userDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userDto, responseEntity.getBody());
    }

    @Test
    void deleteById_Success() {
        UserDto userDto = new UserDto();
        when(userService.deleteUser(anyLong())).thenReturn(userDto);

        ResponseEntity<UserDto> responseEntity = userController.deleteById(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userDto, responseEntity.getBody());
    }

    @Test
    void createUser_Success() {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        when(userService.hasUserWithEmail(anyString())).thenReturn(false);
        when(userService.createUser(any(UserDto.class))).thenReturn(userDto);

        ResponseEntity<?> responseEntity = userController.createUser(userDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(userDto, responseEntity.getBody());
    }

    @Test
    void createUser_UserAlreadyExists() {
        UserDto userDto = new UserDto();
        userDto.setEmail("existing@example.com");
        when(userService.hasUserWithEmail(anyString())).thenReturn(true);

        ResponseEntity<?> responseEntity = userController.createUser(userDto);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof String);
        assertEquals("User already exist by this email!", responseEntity.getBody());
    }
}
