package com.example.yurnero.demo.controller;

import com.example.yurnero.demo.controller.admin.RequestController;
import com.example.yurnero.demo.dto.BookCourseDto;
import com.example.yurnero.demo.service.admin.RequestService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class RequestControllerTest {
    @Mock
    private RequestService requestService;

    @InjectMocks
    private RequestController requestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getBookings_Success() {
        List<BookCourseDto> bookings = Collections.singletonList(new BookCourseDto());
        when(requestService.getBookings()).thenReturn(bookings);

        ResponseEntity<List<BookCourseDto>> responseEntity = requestController.getBookings();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(bookings, responseEntity.getBody());
    }

    @Test
    void changeBookingStatus_Success() {
        when(requestService.changeBookingStatus(anyLong(), anyString())).thenReturn(true);

        ResponseEntity<?> responseEntity = requestController.changeBookingStatus(1L, "approved");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void changeBookingStatus_NotFound() {
        when(requestService.changeBookingStatus(anyLong(), anyString())).thenReturn(false);

        ResponseEntity<?> responseEntity = requestController.changeBookingStatus(1L, "approved");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }


    @Test
    void getBookings_EmptyList() {
        when(requestService.getBookings()).thenReturn(Collections.emptyList());

        ResponseEntity<List<BookCourseDto>> responseEntity = requestController.getBookings();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(0, responseEntity.getBody().size());
    }

}
