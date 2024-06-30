package com.example.yurnero.demo.repository;

import com.example.yurnero.demo.model.BookCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookCourseRepository extends JpaRepository<BookCourse, Long> {
    List<BookCourse> findAllByUserId(Long id);
}
