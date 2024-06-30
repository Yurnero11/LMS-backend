package com.example.yurnero.demo.controller.user;

import com.example.yurnero.demo.controller.admin.MaterialController;
import com.example.yurnero.demo.dto.BookCourseDto;
import com.example.yurnero.demo.dto.CourseDto;
import com.example.yurnero.demo.service.admin.CourseService;
import com.example.yurnero.demo.service.admin.MaterialService;
import com.example.yurnero.demo.service.user.UserCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@CrossOrigin("*")
@RestController
@RequestMapping("/user/my_courses")
@RequiredArgsConstructor
public class MyCoursesController {
    private final UserCourseService userCourseService;
    private final CourseService courseService;
    private final MaterialService materialService;

    private static final Logger LOGGER = Logger.getLogger(MaterialController.class.getName());

    @GetMapping("/bookings/{id}")
    public ResponseEntity<List<BookCourseDto>> getBookCourseByUserId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userCourseService.getBookCourseByUserId(id));
    }

    @GetMapping("/course/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable("id") Long id) {
        CourseDto course = courseService.getCourseById(id);
        if (course != null) {
            return ResponseEntity.ok(course);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadMaterial(@PathVariable("fileName") String fileName) {
        try {
            Resource file = materialService.downloadMaterialByName(fileName);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(file);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IOException during file download", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "IllegalArgumentException: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
