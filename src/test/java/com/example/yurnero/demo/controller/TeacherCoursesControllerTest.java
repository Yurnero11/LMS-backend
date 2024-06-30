package com.example.yurnero.demo.controller;

import com.example.yurnero.demo.controller.teacher.TeacherCoursesController;
import com.example.yurnero.demo.controller.user.UserCourseController;
import com.example.yurnero.demo.dto.CourseDto;
import com.example.yurnero.demo.dto.MaterialDto;
import com.example.yurnero.demo.model.Course;
import com.example.yurnero.demo.model.Material;

import com.example.yurnero.demo.service.admin.MaterialService;
import com.example.yurnero.demo.service.teacher.TeacherCoursesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.when;

public class TeacherCoursesControllerTest {
    @Mock
    private TeacherCoursesService teacherCoursesService;

    @InjectMocks
    private UserCourseController userCourseController;



    @Mock
    private MaterialService materialService;

    @InjectMocks
    private TeacherCoursesController teacherCoursesController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCoursesByUserId() {
        // Mock data
        Long userId = 1L;
        List<Course> courses = Arrays.asList(new Course(), new Course());
        when(teacherCoursesService.getCoursesByUserId(userId)).thenReturn(courses);

        // Call controller method
        ResponseEntity<List<Course>> response = teacherCoursesController.getCoursesByUserId(userId);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(courses.size(), response.getBody().size());
    }

    @Test
    public void testGetMaterialsByUserId() {
        // Mock data
        Long userId = 1L;
        List<Material> materials = Arrays.asList(new Material(), new Material());
        when(teacherCoursesService.getMaterialsByUserId(userId)).thenReturn(materials);

        // Call controller method
        ResponseEntity<List<Material>> response = teacherCoursesController.getMaterialsByUserId(userId);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(materials.size(), response.getBody().size());
    }

    @Test
    public void testUploadMaterial() throws IOException {
        // Mock data
        String name = "Test Material";
        MultipartFile file = mock(MultipartFile.class);
        Long courseId = 1L;
        String responseMessage = "Material uploaded successfully";
        when(materialService.uploadMaterial(eq(name), any(MultipartFile.class), any(MaterialDto.class))).thenReturn(responseMessage);

        // Call controller method
        ResponseEntity<String> response = teacherCoursesController.uploadMaterial(name, file, courseId);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseMessage, response.getBody());
    }

    @Test
    public void testGetMaterialById() {
        // Mock data
        Long materialId = 1L;
        MaterialDto materialDto = new MaterialDto();
        materialDto.setId(materialId);
        when(materialService.getMaterialById(materialId)).thenReturn(materialDto);

        // Call controller method
        ResponseEntity<MaterialDto> response = teacherCoursesController.getMaterialById(materialId);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(materialId, response.getBody().getId());
    }

    @Test
    public void testDeleteMaterial() {
        // Mock data
        Long materialId = 1L;

        // Call controller method
        ResponseEntity<String> response = teacherCoursesController.deleteMaterial(materialId);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Material deleted successfully", response.getBody());
        verify(materialService, times(1)).deleteMaterialById(materialId);
    }

    @Test
    public void testUploadMaterialIOException() throws IOException {
        // Mock data
        String name = "Test Material";
        MultipartFile file = mock(MultipartFile.class);
        Long courseId = 1L;
        IOException exception = new IOException("Test IO Exception");
        doThrow(exception).when(materialService).uploadMaterial(eq(name), any(MultipartFile.class), any(MaterialDto.class));

        // Call controller method
        ResponseEntity<String> response = teacherCoursesController.uploadMaterial(name, file, courseId);

        // Verify
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to upload material", response.getBody());
    }

    @Test
    public void testUploadMaterialIllegalArgumentException() throws IOException {
        // Mock data
        String name = "Test Material";
        MultipartFile file = mock(MultipartFile.class);
        Long courseId = 1L;
        IllegalArgumentException exception = new IllegalArgumentException("Test Illegal Argument Exception");
        doThrow(exception).when(materialService).uploadMaterial(eq(name), any(MultipartFile.class), any(MaterialDto.class));

        // Call controller method
        ResponseEntity<String> response = teacherCoursesController.uploadMaterial(name, file, courseId);

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Test Illegal Argument Exception", response.getBody());
    }

    @Test
    public void testUploadMaterialUnexpectedException() throws IOException {
        // Mock data
        String name = "Test Material";
        MultipartFile file = mock(MultipartFile.class);
        Long courseId = 1L;
        RuntimeException exception = new RuntimeException("Test Runtime Exception");
        doThrow(exception).when(materialService).uploadMaterial(eq(name), any(MultipartFile.class), any(MaterialDto.class));

        // Call controller method
        ResponseEntity<String> response = teacherCoursesController.uploadMaterial(name, file, courseId);

        // Verify
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected error occurred", response.getBody());
    }



    @Test
    public void testDeleteMaterialUnexpectedException() {
        // Mock data
        Long materialId = 1L;
        RuntimeException exception = new RuntimeException("Test Runtime Exception");
        doThrow(exception).when(materialService).deleteMaterialById(materialId);

        // Call controller method
        ResponseEntity<String> response = teacherCoursesController.deleteMaterial(materialId);

        // Verify
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected error occurred", response.getBody());
    }

    @Test
    public void testDownloadMaterial() throws IOException {
        // Mock data
        String fileName = "test-file.txt";
        byte[] fileContent = "Test file content".getBytes();
        Resource mockResource = mock(Resource.class);
        when(mockResource.getInputStream()).thenReturn(new ByteArrayInputStream(fileContent));
        when(mockResource.getFilename()).thenReturn(fileName);
        when(materialService.downloadMaterialByName(fileName)).thenReturn(mockResource);

        // Call controller method
        ResponseEntity<Resource> response = teacherCoursesController.downloadMaterial(fileName);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fileName, response.getBody().getFilename());
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getHeaders().getContentType());
        assertEquals("attachment; filename=\"" + fileName + "\"", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
    }

    @Test
    public void testDownloadMaterialIOException() throws IOException {
        // Mock data
        String fileName = "test-file.txt";
        IOException exception = new IOException("Test IO Exception");
        when(materialService.downloadMaterialByName(fileName)).thenThrow(exception);

        // Call controller method
        ResponseEntity<Resource> response = teacherCoursesController.downloadMaterial(fileName);

        // Verify
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testDownloadMaterialIllegalArgumentException() throws IOException {
        // Mock data
        String fileName = "test-file.txt";
        IllegalArgumentException exception = new IllegalArgumentException("Test Illegal Argument Exception");
        when(materialService.downloadMaterialByName(fileName)).thenThrow(exception);

        // Call controller method
        ResponseEntity<Resource> response = teacherCoursesController.downloadMaterial(fileName);

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testDownloadMaterialUnexpectedException() throws IOException {
        // Mock data
        String fileName = "test-file.txt";
        RuntimeException exception = new RuntimeException("Test Runtime Exception");
        when(materialService.downloadMaterialByName(fileName)).thenThrow(exception);

        // Call controller method
        ResponseEntity<Resource> response = teacherCoursesController.downloadMaterial(fileName);

        // Verify
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(null, response.getBody());
    }
}
