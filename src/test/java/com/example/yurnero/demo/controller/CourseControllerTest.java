package com.example.yurnero.demo.controller;

import com.example.yurnero.demo.controller.admin.CourseController;
import com.example.yurnero.demo.dto.CourseDto;
import com.example.yurnero.demo.service.admin.CourseService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class CourseControllerTest {
    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCourses() {
        List<CourseDto> courses = Collections.singletonList(new CourseDto());
        when(courseService.getAllCourses()).thenReturn(courses);

        ResponseEntity<?> responseEntity = courseController.getAllCourses();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(courses, responseEntity.getBody());
    }

    @Test
    void createCourse() {
        CourseDto courseDto = new CourseDto();
        when(courseService.createCourse(any(CourseDto.class))).thenReturn(courseDto);

        ResponseEntity<CourseDto> responseEntity = courseController.createCourse(courseDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(courseDto, responseEntity.getBody());
    }

    @Test
    void createCourseBadRequest() {
        when(courseService.createCourse(any(CourseDto.class))).thenReturn(null);

        ResponseEntity<CourseDto> responseEntity = courseController.createCourse(new CourseDto());

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void getCourseById() {
        CourseDto courseDto = new CourseDto();
        when(courseService.getCourseById(anyLong())).thenReturn(courseDto);

        ResponseEntity<CourseDto> responseEntity = courseController.getCourseById(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(courseDto, responseEntity.getBody());
    }

    @Test
    void updateCourse() {
        CourseDto courseDto = new CourseDto();
        when(courseService.updateCourse(anyLong(), any(CourseDto.class))).thenReturn(courseDto);

        ResponseEntity<?> responseEntity = courseController.updateCourse(1L, courseDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(courseDto, responseEntity.getBody());
    }

    @Test
    void updateCourseNotFound() {
        when(courseService.updateCourse(anyLong(), any(CourseDto.class))).thenReturn(null);

        ResponseEntity<?> responseEntity = courseController.updateCourse(1L, new CourseDto());

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void deleteCourse() {
        ResponseEntity<Void> responseEntity = courseController.deleteCourse(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
