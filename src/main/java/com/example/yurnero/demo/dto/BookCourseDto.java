package com.example.yurnero.demo.dto;

import com.example.yurnero.demo.enums.CourseBookStatus;
import lombok.Data;

@Data
public class BookCourseDto {
    private Long id;
    private CourseBookStatus courseBookStatus;
    private Long userId;
    private Long courseId;
    private String courseName;
    private String userName;
}
