package com.example.yurnero.demo.controller.admin;

import com.example.yurnero.demo.dto.UserDto;
import com.example.yurnero.demo.service.admin.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/users")
public class UserController {
    private final UsersService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id) {
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<UserDto>> getAllTeachers() {
        List<UserDto> users = userService.getAllTeachers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/addUser")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        if (userService.hasUserWithEmail(userDto.getEmail()))
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User already exist by this email!");

        UserDto savedUserDto = userService.createUser(userDto);
        return new ResponseEntity<>(savedUserDto, HttpStatus.CREATED);
    }

    @PostMapping("/addTeacher")
    public ResponseEntity<UserDto> createTeacher(@RequestBody UserDto userDto) {
        UserDto savedUserDto = userService.createTeacher(userDto);
        return new ResponseEntity<>(savedUserDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id, @RequestBody UserDto userDto) {
        UserDto updateUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updateUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> deleteById(@PathVariable("id") Long id) {
        UserDto deletedUserDto = userService.deleteUser(id);
        return ResponseEntity.ok(deletedUserDto);
    }
}
