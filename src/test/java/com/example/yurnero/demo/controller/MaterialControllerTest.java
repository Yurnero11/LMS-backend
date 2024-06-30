package com.example.yurnero.demo.controller;

import com.example.yurnero.demo.controller.admin.MaterialController;
import com.example.yurnero.demo.dto.MaterialDto;
import com.example.yurnero.demo.service.admin.MaterialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class MaterialControllerTest {
    @Mock
    private MaterialService materialService;

    @InjectMocks
    private MaterialController materialController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllMaterials() {
        List<MaterialDto> materials = Collections.singletonList(new MaterialDto());
        when(materialService.getAllMaterials()).thenReturn(materials);

        ResponseEntity<?> responseEntity = materialController.getAllMaterials();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(materials, responseEntity.getBody());
    }

    @Test
    void getMaterialById() {
        MaterialDto materialDto = new MaterialDto();
        when(materialService.getMaterialById(anyLong())).thenReturn(materialDto);

        ResponseEntity<MaterialDto> responseEntity = materialController.getMaterialById(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(materialDto, responseEntity.getBody());
    }

    @Test
    void deleteMaterial() {
        ResponseEntity<String> responseEntity = materialController.deleteMaterial(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Material deleted successfully", responseEntity.getBody());
    }

    @Test
    void deleteMaterialException() {
        doThrow(new RuntimeException("Unexpected error")).when(materialService).deleteMaterialById(anyLong());

        ResponseEntity<String> responseEntity = materialController.deleteMaterial(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Unexpected error occurred", responseEntity.getBody());
    }

    @Test
    void uploadMaterial() throws IOException {
        MultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());
        when(materialService.uploadMaterial(anyString(), any(MultipartFile.class), any(MaterialDto.class))).thenReturn("Material uploaded");

        ResponseEntity<String> responseEntity = materialController.uploadMaterial("test", file, 1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Material uploaded", responseEntity.getBody());
    }

    @Test
    void uploadMaterialConflict() throws IOException {
        MultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());
        when(materialService.materialExistsByName(anyString())).thenReturn(true);

        ResponseEntity<String> responseEntity = materialController.uploadMaterial("test", file, 1L);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("A material with this name already exists.", responseEntity.getBody());
    }

    @Test
    void uploadMaterialIOException() throws IOException {
        MultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());
        when(materialService.uploadMaterial(anyString(), any(MultipartFile.class), any(MaterialDto.class))).thenThrow(IOException.class);

        ResponseEntity<String> responseEntity = materialController.uploadMaterial("test", file, 1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Failed to upload material", responseEntity.getBody());
    }

    @Test
    void downloadMaterial() throws IOException {
        Resource resource = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes()).getResource();
        when(materialService.downloadMaterialByName(anyString())).thenReturn(resource);

        ResponseEntity<Resource> responseEntity = materialController.downloadMaterial("filename.txt");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(resource, responseEntity.getBody());
    }

    @Test
    void downloadMaterialIllegalArgumentException() throws IOException {
        when(materialService.downloadMaterialByName(anyString())).thenThrow(new IllegalArgumentException("Invalid argument"));

        ResponseEntity<Resource> responseEntity = materialController.downloadMaterial("filename.txt");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }

    @Test
    void downloadMaterialException() throws IOException {
        when(materialService.downloadMaterialByName(anyString())).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<Resource> responseEntity = materialController.downloadMaterial("filename.txt");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }

    @Test
    void downloadMaterialIOException() throws IOException {
        when(materialService.downloadMaterialByName(anyString())).thenThrow(new IOException("Failed to download"));

        ResponseEntity<Resource> responseEntity = materialController.downloadMaterial("filename.txt");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }

    @Test
    void uploadMaterial_Success() throws IOException {
        MultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());
        when(materialService.uploadMaterial(anyString(), any(MultipartFile.class), any(MaterialDto.class))).thenReturn("Material uploaded successfully");

        ResponseEntity<String> responseEntity = materialController.uploadMaterial("testName", file, 1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Material uploaded successfully", responseEntity.getBody());
    }

    @Test
    void uploadMaterial_Conflict() throws IOException {
        MultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());
        when(materialService.materialExistsByName(anyString())).thenReturn(true);

        ResponseEntity<String> responseEntity = materialController.uploadMaterial("testName", file, 1L);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("A material with this name already exists.", responseEntity.getBody());
    }

    @Test
    void uploadMaterial_IOException() throws IOException {
        MultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());
        when(materialService.uploadMaterial(anyString(), any(MultipartFile.class), any(MaterialDto.class))).thenThrow(new IOException("Failed to upload"));

        ResponseEntity<String> responseEntity = materialController.uploadMaterial("testName", file, 1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Failed to upload material", responseEntity.getBody());
    }

    @Test
    void uploadMaterial_IllegalArgumentException() throws IOException {
        MultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());
        when(materialService.uploadMaterial(anyString(), any(MultipartFile.class), any(MaterialDto.class))).thenThrow(new IllegalArgumentException("Invalid argument"));

        ResponseEntity<String> responseEntity = materialController.uploadMaterial("testName", file, 1L);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid argument", responseEntity.getBody());
    }

    @Test
    void uploadMaterial_Exception() throws IOException {
        MultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some content".getBytes());
        when(materialService.uploadMaterial(anyString(), any(MultipartFile.class), any(MaterialDto.class))).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<String> responseEntity = materialController.uploadMaterial("testName", file, 1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Unexpected error occurred", responseEntity.getBody());
    }
}
