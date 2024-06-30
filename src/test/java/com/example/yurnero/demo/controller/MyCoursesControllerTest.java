package com.example.yurnero.demo.controller;
import com.example.yurnero.demo.controller.user.MyCoursesController;
import com.example.yurnero.demo.dto.BookCourseDto;
import com.example.yurnero.demo.dto.CourseDto;
import com.example.yurnero.demo.service.admin.CourseService;
import com.example.yurnero.demo.service.admin.MaterialService;
import com.example.yurnero.demo.service.user.UserCourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MyCoursesControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserCourseService userCourseService;

    @Mock
    private CourseService courseService;

    @Mock
    private MaterialService materialService;

    @InjectMocks
    private MyCoursesController myCoursesController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(myCoursesController).build();
    }

    @Test
    public void testGetBookCourseByUserId() throws Exception {
        // Prepare mock data
        Long userId = 1L;
        BookCourseDto bookCourseDto = new BookCourseDto();
        bookCourseDto.setId(1L);
        bookCourseDto.setCourseName("Course 1");

        List<BookCourseDto> bookCourseDtos = Arrays.asList(bookCourseDto);

        when(userCourseService.getBookCourseByUserId(userId)).thenReturn(bookCourseDtos);

        mockMvc.perform(get("/user/my_courses/bookings/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(bookCourseDto.getId().intValue()))
                .andExpect(jsonPath("$[0].courseName").value(bookCourseDto.getCourseName()));

        verify(userCourseService, times(1)).getBookCourseByUserId(userId);
        verifyNoMoreInteractions(userCourseService);
    }

    @Test
    public void testGetCourseByIdFound() throws Exception {
        // Prepare mock data
        Long courseId = 1L;
        CourseDto courseDto = new CourseDto();
        courseDto.setId(courseId);
        courseDto.setName("Course 1");

        // Mock service method
        when(courseService.getCourseById(courseId)).thenReturn(courseDto);

        // Perform GET request and verify results
        mockMvc.perform(get("/user/my_courses/course/{id}", courseId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(courseDto.getId().intValue()))
                .andExpect(jsonPath("$.name").value(courseDto.getName()));

        // Verify service method was called
        verify(courseService, times(1)).getCourseById(courseId);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    public void testGetCourseByIdNotFound() throws Exception {
        // Prepare mock data
        Long courseId = 1L;

        // Mock service method returning null
        when(courseService.getCourseById(courseId)).thenReturn(null);

        // Perform GET request and verify results
        mockMvc.perform(get("/user/my_courses/course/{id}", courseId))
                .andExpect(status().isNotFound());

        // Verify service method was called
        verify(courseService, times(1)).getCourseById(courseId);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    public void testDownloadMaterial() throws Exception {
        // Prepare mock data
        String fileName = "test.txt";
        byte[] mockContent = "test content".getBytes(StandardCharsets.UTF_8);
        Resource mockResource = mock(Resource.class);

        // Mock getFilename() to return the expected file name
        when(mockResource.getFilename()).thenReturn(fileName);
        when(mockResource.getInputStream()).thenReturn(new ByteArrayInputStream(mockContent));

        // Mock service method
        when(materialService.downloadMaterialByName(fileName)).thenReturn(mockResource);

        // Perform GET request and verify results
        mockMvc.perform(get("/user/my_courses/download/{fileName}", fileName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\""))
                .andExpect(content().bytes(mockContent));

        // Verify service method was called
        verify(materialService, times(1)).downloadMaterialByName(fileName);
        verifyNoMoreInteractions(materialService);
    }

    @Test
    public void testDownloadMaterial_IOException() throws Exception {
        // Prepare mock data
        String fileName = "test.txt";

        // Mock service method to throw IOException
        when(materialService.downloadMaterialByName(fileName)).thenThrow(new IOException("File not found"));

        // Perform GET request and verify results
        mockMvc.perform(get("/user/my_courses/download/{fileName}", fileName))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(isEmptyOrNullString()));

        // Verify service method was called
        verify(materialService, times(1)).downloadMaterialByName(fileName);
        verifyNoMoreInteractions(materialService);
    }

    @Test
    public void testDownloadMaterial_IllegalArgumentException() throws Exception {
        // Prepare mock data
        String fileName = "test.txt";

        // Mock service method to throw IllegalArgumentException
        when(materialService.downloadMaterialByName(fileName)).thenThrow(new IllegalArgumentException("Invalid file name"));

        // Perform GET request and verify results
        mockMvc.perform(get("/user/my_courses/download/{fileName}", fileName))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(isEmptyOrNullString()));

        // Verify service method was called
        verify(materialService, times(1)).downloadMaterialByName(fileName);
        verifyNoMoreInteractions(materialService);
    }

    @Test
    public void testDownloadMaterial_UnexpectedException() throws Exception {
        // Prepare mock data
        String fileName = "test.txt";

        // Mock service method to throw unexpected exception
        when(materialService.downloadMaterialByName(fileName)).thenThrow(new RuntimeException("Unexpected error"));

        // Perform GET request and verify results
        mockMvc.perform(get("/user/my_courses/download/{fileName}", fileName))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(isEmptyOrNullString()));

        // Verify service method was called
        verify(materialService, times(1)).downloadMaterialByName(fileName);
        verifyNoMoreInteractions(materialService);
    }
}
