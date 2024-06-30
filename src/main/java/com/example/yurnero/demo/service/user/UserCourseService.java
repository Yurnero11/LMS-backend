package com.example.yurnero.demo.service.user;

import com.example.yurnero.demo.dto.BookCourseDto;
import com.example.yurnero.demo.dto.CourseDto;

import java.util.List;

public interface UserCourseService {
    List<CourseDto> getAllCourse();
    boolean bookCourse(BookCourseDto bookCourseDto);
    CourseDto getCourseById(Long id);
    List<BookCourseDto> getBookCourseByUserId(Long id);
}
