package com.example.yurnero.demo.service.user;

import com.example.yurnero.demo.dto.BookCourseDto;
import com.example.yurnero.demo.dto.CourseDto;
import com.example.yurnero.demo.enums.CourseBookStatus;
import com.example.yurnero.demo.model.BookCourse;
import com.example.yurnero.demo.model.Course;
import com.example.yurnero.demo.model.User;
import com.example.yurnero.demo.repository.BookCourseRepository;
import com.example.yurnero.demo.repository.CourseRepository;
import com.example.yurnero.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserCourseServiceImpl implements UserCourseService{
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final BookCourseRepository bookCourseRepository;

    private static final Logger logger = Logger.getLogger(UserCourseServiceImpl.class.getName());

    @Override
    public List<CourseDto> getAllCourse() {
        List<CourseDto> courses = courseRepository.findAll()
                .stream()
                .map(Course::getCourseDto)
                .collect(Collectors.toList());
        logger.log(Level.INFO, "Retrieved {0} courses", courses.size());
        return courses;
    }

    @Override
    public boolean bookCourse(BookCourseDto bookCourseDto) {
        Optional<Course> optionalCourse = courseRepository.findById(bookCourseDto.getCourseId());
        Optional<User> optionalUser = userRepository.findById(bookCourseDto.getUserId());

        if (optionalCourse.isPresent() && optionalUser.isPresent()) {
            BookCourse bookCourse = new BookCourse();
            bookCourse.setUser(optionalUser.get());
            bookCourse.setCourse(optionalCourse.get());
            bookCourse.setCourseBookStatus(CourseBookStatus.PENDING);
            bookCourseRepository.save(bookCourse);
            logger.log(Level.INFO, "Booked course {0} for user {1}", new Object[]{bookCourseDto.getCourseId(), bookCourseDto.getUserId()});
            return true;
        }
        return false;
    }

    @Override
    public CourseDto getCourseById(Long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        return optionalCourse.map(Course::getCourseDto).orElse(null);
    }

    @Override
    public List<BookCourseDto> getBookCourseByUserId(Long id) {
        List<BookCourseDto> bookCourses = bookCourseRepository.findAllByUserId(id)
                .stream()
                .map(BookCourse::getBookCourseDto)
                .collect(Collectors.toList());
        logger.log(Level.INFO, "Retrieved {0} booked courses for user {1}", new Object[]{bookCourses.size(), id});
        return bookCourses;
    }
}
