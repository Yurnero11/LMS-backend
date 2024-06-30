package com.example.yurnero.demo.service.teacher;

import com.example.yurnero.demo.dto.CourseDto;
import com.example.yurnero.demo.dto.MaterialDto;
import com.example.yurnero.demo.model.Course;
import com.example.yurnero.demo.model.Material;

import java.util.List;

public interface TeacherCoursesService {

    List<Course> getCoursesByUserId(Long userId);

    List<Material> getMaterialsByUserId(Long userId);
}
