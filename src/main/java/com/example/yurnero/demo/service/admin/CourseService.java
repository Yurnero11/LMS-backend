package com.example.yurnero.demo.service.admin;

import com.example.yurnero.demo.dto.CourseDto;
import com.example.yurnero.demo.model.Course;

import java.util.List;

public interface CourseService {
    CourseDto createCourse(CourseDto courseDto);
    List<CourseDto> getAllCourses();
    void deleteCourseById(Long id);
    CourseDto getCourseById(Long id);
    CourseDto updateCourse(Long id, CourseDto courseDto);
    Course findById(Long id);
    List<Long> getRegisteredCourseIds(Long userId);

    List<Course> getCoursesByUserId(Long userId);
}
