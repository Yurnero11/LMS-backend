package com.example.yurnero.demo.controller.teacher;

import com.example.yurnero.demo.controller.admin.MaterialController;
import com.example.yurnero.demo.dto.CourseDto;
import com.example.yurnero.demo.dto.MaterialDto;
import com.example.yurnero.demo.model.Course;
import com.example.yurnero.demo.model.Material;
import com.example.yurnero.demo.service.admin.MaterialService;
import com.example.yurnero.demo.service.teacher.TeacherCoursesService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@CrossOrigin("*")
@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherCoursesController {
    private final TeacherCoursesService teacherCoursesService;
    private final MaterialService materialService;
    private static final Logger LOGGER = Logger.getLogger(MaterialController.class.getName());


    @GetMapping("/teacher_courses/{userId}")
    public ResponseEntity<List<Course>> getCoursesByUserId(@PathVariable Long userId) {
        List<Course> courses = teacherCoursesService.getCoursesByUserId(userId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/teacher_materials/{userId}")
    public ResponseEntity<List<Material>> getMaterialsByUserId(@PathVariable Long userId) {
        List<Material> materials = teacherCoursesService.getMaterialsByUserId(userId);
        return ResponseEntity.ok(materials);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadMaterial(@RequestParam("name") String name,
                                                 @RequestParam("file") MultipartFile file,
                                                 @RequestParam("courseId") Long courseId) {
        try {
            MaterialDto materialDto = new MaterialDto();
            materialDto.setCourseId(courseId);
            String response = materialService.uploadMaterial(name, file, materialDto);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IOException during file upload", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload material");
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "IllegalArgumentException: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialDto> getMaterialById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(materialService.getMaterialById(id));
    }

    @DeleteMapping("/teacher_materials/delete/{id}")
    public ResponseEntity<String> deleteMaterial(@PathVariable("id") Long id) {
        try {
            materialService.deleteMaterialById(id);
            return ResponseEntity.ok("Material deleted successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error during material deletion", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred");
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
