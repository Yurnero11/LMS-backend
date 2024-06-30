package com.example.yurnero.demo.service.teacher;

import com.example.yurnero.demo.dto.CourseDto;
import com.example.yurnero.demo.model.Course;
import com.example.yurnero.demo.model.Material;
import com.example.yurnero.demo.repository.CourseRepository;
import com.example.yurnero.demo.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class TeacherCoursesServiceImpl implements TeacherCoursesService{
    private final CourseRepository courseRepository;
    private final MaterialRepository materialRepository;

    private static final Logger logger = Logger.getLogger(TeacherCoursesService.class.getName());

    public List<Course> getCoursesByUserId(Long userId) {
        List<Course> courses = courseRepository.findByUserId(userId);
        logger.log(Level.INFO, "Retrieved {0} courses for user with id: {1}", new Object[]{courses.size(), userId});
        return courses;
    }

    public List<Material> getMaterialsByUserId(Long userId) {
        List<Course> courses = courseRepository.findByUserId(userId);
        List<Material> materials = materialRepository.findByCourseIn(courses);
        logger.log(Level.INFO, "Retrieved {0} materials for user with id: {1}", new Object[]{materials.size(), userId});
        return materials;
    }
}
