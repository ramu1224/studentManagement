package com.test.studentmanagement.service;

import com.test.studentmanagement.exception.DuplicateCourseNameException;
import com.test.studentmanagement.exception.ResourceNotFoundException;
import com.test.studentmanagement.model.Course;
import com.test.studentmanagement.model.Student;
import com.test.studentmanagement.repository.CourseRepository;
import com.test.studentmanagement.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private CourseService courseService;

    @Test
    void createCourse_savesTheCourse() {
        Course course = new Course("Java", "Basics");
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Course saved = courseService.createCourse(course);

        assertEquals("Java", saved.getName());
        verify(courseRepository).save(course);
    }

    @Test
    void getCourse_whenMissing_throwsNotFound() {
        when(courseRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courseService.getCourse(3L));
    }

    @Test
    void updateCourse_changesNameAndDescription() {
        Course existing = new Course("Old", "Old description");
        existing.setId(1L);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Course result = courseService.updateCourse(1L, new Course("New", "New description"));

        assertEquals("New", result.getName());
        assertEquals("New description", result.getDescription());
    }

    @Test
    void deleteCourse_removesItFromStudentsFirst() {
        Course course = new Course("Java", "Basics");
        course.setId(1L);
        Student student = new Student("Ada", "Lovelace", "King", "Jr.", "Female", new Date(0), "ada@test.com");
        student.getCourses().add(course);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.findByCoursesId(1L)).thenReturn(List.of(student));

        courseService.deleteCourse(1L);

        assertTrue(student.getCourses().isEmpty());
        verify(studentRepository).save(student);
        verify(courseRepository).delete(course);
    }

    @Test
    void createCourse_withExistingName_throwsDuplicateCourseName() {
        when(courseRepository.existsByNameIgnoreCase("Java")).thenReturn(true);

        assertThrows(DuplicateCourseNameException.class, () -> courseService.createCourse(new Course("Java", "Basics")));
    }
}
