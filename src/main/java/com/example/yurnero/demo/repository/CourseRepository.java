package com.example.yurnero.demo.repository;

import com.example.yurnero.demo.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.materials WHERE c.id = :id")
    Optional<Course> findByIdWithMaterials(@Param("id") Long id);

    List<Course> findByUserId(Long userId);
}
