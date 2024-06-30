package com.example.yurnero.demo.service.admin;

import com.example.yurnero.demo.dto.BookCourseDto;

import java.util.List;

public interface RequestService {
    List<BookCourseDto> getBookings();
    boolean changeBookingStatus(Long id, String value);
}
