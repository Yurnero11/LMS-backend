package com.example.yurnero.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CourseDto {
    private Long id;
    @NotNull(message = "Name cannot be null")
    @Size(max=50)
    private String name;
    @NotNull(message = "Title cannot be null")
    private String title;
    @NotNull(message = "Description cannot be null")
    @Size(min = 10, max = 60, message
            = "Description must be between 10 and 200 characters")
    private String description;
    @NotNull(message = "Date cannot be null")
    private Date creationDate;
    private String userName;
    private Long userId;
    private List<MaterialDto> materials;
}
