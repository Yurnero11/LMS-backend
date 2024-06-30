package com.example.yurnero.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class MaterialDto {
    private Long id;
    @NotNull(message = "Name cannot be null")
    private String name;
    @NotNull(message = "File path cannot be null")
    private String filePath;
    private Long courseId;
    private String courseName;
}
