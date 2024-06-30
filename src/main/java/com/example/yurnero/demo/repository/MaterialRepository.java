package com.example.yurnero.demo.repository;

import com.example.yurnero.demo.model.Course;
import com.example.yurnero.demo.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    Optional<Material> findByFilePath(String filePath);
    Optional<Material> findByName(String name);
    List<Material> findByCourseIn(List<Course> courses);
}
