package com.example.yurnero.demo.model;

import com.example.yurnero.demo.dto.BookCourseDto;
import com.example.yurnero.demo.enums.CourseBookStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book_course")
public class BookCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private CourseBookStatus courseBookStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Course course;

    public BookCourseDto getBookCourseDto() {
        BookCourseDto bookCourseDto = new BookCourseDto();
        bookCourseDto.setId(id);
        bookCourseDto.setCourseBookStatus(courseBookStatus);
        bookCourseDto.setCourseId(user.getId());
        bookCourseDto.setUserId(course.getId());
        bookCourseDto.setCourseName(course.getName());
        bookCourseDto.setUserName(user.getName());
        return bookCourseDto;
    }
}
