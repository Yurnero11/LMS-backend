package com.example.yurnero.demo.model;

import com.example.yurnero.demo.dto.CourseDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String title;
    private String description;

    private Date creationDate;

    @PrePersist
    protected void onCreate() {
        this.creationDate = new Date();
    }

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @ToString.Exclude
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Material> materials = new ArrayList<>();

    public CourseDto getCourseDto() {
        CourseDto courseDto = new CourseDto();
        courseDto.setId(id);
        courseDto.setName(name);
        courseDto.setTitle(title);
        courseDto.setDescription(description);
        courseDto.setUserName(user.getName());
        courseDto.setUserId(user.getId());
        courseDto.setCreationDate(creationDate);
        courseDto.setMaterials(materials.stream().map(Material::getMaterialDto).collect(Collectors.toList())); // добавлено поле
        return courseDto;
    }
}
