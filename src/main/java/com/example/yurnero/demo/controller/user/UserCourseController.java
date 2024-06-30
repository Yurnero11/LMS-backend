package com.example.yurnero.demo.controller.user;

import com.example.yurnero.demo.dto.BookCourseDto;
import com.example.yurnero.demo.dto.CourseDto;
import com.example.yurnero.demo.dto.UserDto;
import com.example.yurnero.demo.service.admin.CourseService;
import com.example.yurnero.demo.service.admin.UsersService;
import com.example.yurnero.demo.service.user.UserCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/user/courses")
@RequiredArgsConstructor
public class UserCourseController {
    private final UserCourseService userCourseService;
    private final UsersService userService;
    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<List<CourseDto>> gerAllCourses() {
        List<CourseDto> courseDtoList = userCourseService.getAllCourse();
        return ResponseEntity.ok(courseDtoList);
    }

    @GetMapping("/registered")
    public ResponseEntity<List<Long>> getRegisteredCourses(@RequestParam Long userId) {
        List<Long> registeredCourseIds = courseService.getRegisteredCourseIds(userId);
        return ResponseEntity.ok(registeredCourseIds);
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<UserDto>> getAllTeachers() {
        List<UserDto> users = userService.getAllTeachers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/book")
    public ResponseEntity<Void> bookCourse(@RequestBody BookCourseDto bookCourseDto) {
        boolean success = userCourseService.bookCourse(bookCourseDto);
        if (success) return ResponseEntity.status(HttpStatus.CREATED).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable("id") Long id) {
        CourseDto courseDto = userCourseService.getCourseById(id);
        if (courseDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(courseDto);
    }


}
