package com.example.yurnero.demo.service.admin;

import com.example.yurnero.demo.dto.CourseDto;
import com.example.yurnero.demo.model.Course;
import com.example.yurnero.demo.model.User;
import com.example.yurnero.demo.repository.BookCourseRepository;
import com.example.yurnero.demo.repository.CourseRepository;
import com.example.yurnero.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService{
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final BookCourseRepository bookCourseRepository;

    private static final Logger logger = Logger.getLogger(CourseServiceImpl.class.getName());

    @Override
    public CourseDto createCourse(CourseDto courseDto) {
        Optional<User> optionalUser = userRepository.findById(courseDto.getUserId());
        if (optionalUser.isPresent()) {
            Course course = new Course();
            course.setName(courseDto.getName());
            course.setTitle(courseDto.getTitle());
            course.setDescription(courseDto.getDescription());
            course.setUser(optionalUser.get());
            Course savedCourse = courseRepository.save(course);
            logger.log(Level.INFO, "Course created: {0}", savedCourse);
            return savedCourse.getCourseDto();
        } else {
            logger.log(Level.WARNING, "User not found for id: {0}", courseDto.getUserId());
            return null;
        }
    }

    @Override
    public List<CourseDto> getAllCourses() {
        List<CourseDto> courses = courseRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Course::getCreationDate).reversed())
                .map(Course::getCourseDto)
                .collect(Collectors.toList());
        logger.log(Level.INFO, "Retrieved all courses: {0}", courses.size());
        return courses;
    }

    @Override
    public void deleteCourseById(Long id) {
        courseRepository.deleteById(id);
        logger.log(Level.INFO, "Course deleted with id: {0}", id);
    }

    @Override
    public CourseDto getCourseById(Long id) {
        Optional<Course> courseOptional = courseRepository.findByIdWithMaterials(id);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            logger.log(Level.INFO, "Retrieved course by id: {0}", id);
            return course.getCourseDto();
        } else {
            logger.log(Level.WARNING, "Course not found for id: {0}", id);
            return null;
        }
    }

    @Override
    public CourseDto updateCourse(Long id, CourseDto courseDto) {
        Optional<User> optionalUser = userRepository.findById(courseDto.getUserId());
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {
            Course existingCourse = optionalCourse.get();
            existingCourse.setName(courseDto.getName());
            existingCourse.setTitle(courseDto.getTitle());
            existingCourse.setDescription(courseDto.getDescription());
            existingCourse.setUser(optionalUser.get());
            Course updatedCourse = courseRepository.save(existingCourse);
            logger.log(Level.INFO, "Course updated: {0}", updatedCourse);
            return updatedCourse.getCourseDto();
        } else {
            logger.log(Level.WARNING, "Course not found for id: {0}", id);
            return null;
        }
    }

    @Override
    public Course findById(Long id) {
        Optional<Course> courseOptional = courseRepository.findByIdWithMaterials(id);
        if (courseOptional.isPresent()) {
            logger.log(Level.INFO, "Retrieved course by id: {0}", id);
            return courseOptional.get();
        } else {
            logger.log(Level.WARNING, "Course not found for id: {0}", id);
            return null;
        }
    }

    @Override
    public List<Long> getRegisteredCourseIds(Long userId) {
        List<Long> registeredCourseIds = bookCourseRepository.findAllByUserId(userId)
                .stream()
                .map(bookCourse -> bookCourse.getCourse().getId())
                .collect(Collectors.toList());
        logger.log(Level.INFO, "Retrieved registered course ids for user: {0}, count: {1}", new Object[]{userId, registeredCourseIds.size()});
        return registeredCourseIds;
    }

    @Override
    public List<Course> getCoursesByUserId(Long userId) {
        List<Course> courses = courseRepository.findByUserId(userId);
        logger.log(Level.INFO, "Retrieved courses for user id: {0}, count: {1}", new Object[]{userId, courses.size()});
        return courses;
    }
}
