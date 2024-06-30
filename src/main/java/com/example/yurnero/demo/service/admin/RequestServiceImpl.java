package com.example.yurnero.demo.service.admin;

import com.example.yurnero.demo.dto.BookCourseDto;
import com.example.yurnero.demo.enums.CourseBookStatus;
import com.example.yurnero.demo.model.BookCourse;
import com.example.yurnero.demo.repository.BookCourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService{
    private final BookCourseRepository bookCourseRepository;

    private static final Logger logger = Logger.getLogger(RequestServiceImpl.class.getName());

    @Override
    public List<BookCourseDto> getBookings() {
        List<BookCourse> bookings = bookCourseRepository.findAll();
        return bookings.stream()
                .map(bookCourse -> {
                    BookCourseDto bookCourseDto = bookCourse.getBookCourseDto();
                    bookCourseDto.setUserId(bookCourse.getUser().getId());
                    bookCourseDto.setCourseId(bookCourse.getCourse().getId());
                    return bookCourseDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean changeBookingStatus(Long id, String value) {
        Optional<BookCourse> optionalBookCourse = bookCourseRepository.findById(id);
        if (optionalBookCourse.isPresent()) {
            BookCourse existingBookCourse = optionalBookCourse.get();
            if (Objects.equals(value, "APPROVED")) {
                existingBookCourse.setCourseBookStatus(CourseBookStatus.APPROVED);
            } else {
                existingBookCourse.setCourseBookStatus(CourseBookStatus.REJECTED);
            }
            bookCourseRepository.save(existingBookCourse);
            logger.log(Level.INFO, "Booking status changed for booking id {0} to {1}", new Object[]{id, value});
            return true;
        } else {
            logger.log(Level.WARNING, "Booking not found for id {0}", id);
            return false;
        }
    }
}
