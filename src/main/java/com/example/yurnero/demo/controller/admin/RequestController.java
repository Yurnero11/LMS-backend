package com.example.yurnero.demo.controller.admin;

import com.example.yurnero.demo.dto.BookCourseDto;
import com.example.yurnero.demo.service.admin.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/admin/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @GetMapping
    public ResponseEntity<List<BookCourseDto>> getBookings() {
        return ResponseEntity.ok(requestService.getBookings());
    }

    @GetMapping("/{bookId}/{status}")
    public ResponseEntity<?> changeBookingStatus(@PathVariable("bookId") Long id, @PathVariable("status") String status) {
        boolean success = requestService.changeBookingStatus(id, status);
        if (success) return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }
}
