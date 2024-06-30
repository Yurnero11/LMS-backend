package com.example.yurnero.demo.model;

import com.example.yurnero.demo.dto.CourseDto;
import com.example.yurnero.demo.dto.MaterialDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "materials")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "file_path")
    private String filePath;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Course course;

    public MaterialDto getMaterialDto() {
        MaterialDto materialDto = new MaterialDto();
        materialDto.setId(id);
        materialDto.setName(name);
        materialDto.setFilePath(filePath);
        materialDto.setCourseId(course.getId());
        materialDto.setCourseName(course.getName()); // Установка courseName
        return materialDto;
    }
}
