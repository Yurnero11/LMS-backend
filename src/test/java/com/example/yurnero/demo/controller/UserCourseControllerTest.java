package com.example.yurnero.demo.controller;

import com.example.yurnero.demo.controller.user.UserCourseController;
import com.example.yurnero.demo.dto.BookCourseDto;
import com.example.yurnero.demo.dto.CourseDto;
import com.example.yurnero.demo.dto.UserDto;
import com.example.yurnero.demo.service.admin.CourseService;
import com.example.yurnero.demo.service.admin.UsersService;
import com.example.yurnero.demo.service.user.UserCourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserCourseControllerTest {
    @Mock
    private UserCourseService userCourseService;

    @Mock
    private UsersService userService;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private UserCourseController userCourseController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllCourses() {
        // Mock data
        List<CourseDto> courses = Arrays.asList(new CourseDto(), new CourseDto());
        when(userCourseService.getAllCourse()).thenReturn(courses);

        // Call controller method
        ResponseEntity<List<CourseDto>> response = userCourseController.gerAllCourses();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(courses, response.getBody());
    }

    @Test
    public void testGetRegisteredCourses() {
        Long userId = 1L;
        List<Long> registeredCourseIds = Arrays.asList(101L, 102L, 103L);
        when(courseService.getRegisteredCourseIds(userId)).thenReturn(registeredCourseIds);

        ResponseEntity<List<Long>> response = userCourseController.getRegisteredCourses(userId);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(registeredCourseIds, response.getBody());
    }

    @Test
    public void testGetAllTeachers() {
        // Mock data
        List<UserDto> teachers = Arrays.asList(new UserDto(), new UserDto());
        when(userService.getAllTeachers()).thenReturn(teachers);

        // Call controller method
        ResponseEntity<List<UserDto>> response = userCourseController.getAllTeachers();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(teachers, response.getBody());
    }

    @Test
    public void testBookCourseSuccess() {
        // Mock data
        BookCourseDto bookCourseDto = new BookCourseDto();
        bookCourseDto.setUserId(1L);
        bookCourseDto.setCourseId(101L);
        when(userCourseService.bookCourse(bookCourseDto)).thenReturn(true);

        // Call controller method
        ResponseEntity<Void> response = userCourseController.bookCourse(bookCourseDto);

        // Verify
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testBookCourseFailure() {
        // Mock data
        BookCourseDto bookCourseDto = new BookCourseDto();
        bookCourseDto.setUserId(1L);
        bookCourseDto.setCourseId(101L);
        when(userCourseService.bookCourse(bookCourseDto)).thenReturn(false);

        // Call controller method
        ResponseEntity<Void> response = userCourseController.bookCourse(bookCourseDto);

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testGetCourseByIdFound() {
        // Mock data
        Long courseId = 1L;
        CourseDto courseDto = new CourseDto();
        courseDto.setId(courseId);
        when(userCourseService.getCourseById(courseId)).thenReturn(courseDto);

        // Call controller method
        ResponseEntity<CourseDto> response = userCourseController.getCourseById(courseId);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(courseId, response.getBody().getId());
    }

    @Test
    public void testGetCourseByIdNotFound() {
        // Mock data
        Long courseId = 1L;
        when(userCourseService.getCourseById(courseId)).thenReturn(null);

        // Call controller method
        ResponseEntity<CourseDto> response = userCourseController.getCourseById(courseId);

        // Verify
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
