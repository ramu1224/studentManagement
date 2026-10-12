package com.test.studentmanagement.service;

import com.test.studentmanagement.exception.DuplicateEmailException;
import com.test.studentmanagement.exception.InvalidDateOfBirthException;
import com.test.studentmanagement.exception.InvalidSexException;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private StudentService studentService;

    private Student newStudent(String email) {
        return new Student("Ada", "Lovelace", "King", "Jr.", "Female", new Date(0), email);
    }

    @Test
    void getAllStudents_returnsEverything() {
        when(studentRepository.findAll()).thenReturn(List.of(newStudent("a@test.com"), newStudent("b@test.com")));

        assertEquals(2, studentService.getAllStudents().size());
    }

    @Test
    void getStudent_whenMissing_throwsNotFound() {
        when(studentRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudent(5L));
    }

    @Test
    void createStudent_savesStudentWithCourses() {
        Course course = new Course("Java", "Basics");
        course.setId(1L);

        Student student = newStudent("ada@test.com");
        Course requested = new Course();
        requested.setId(1L);
        student.setCourses(new HashSet<>(List.of(requested)));

        when(studentRepository.existsByEmail("ada@test.com")).thenReturn(false);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student saved = studentService.createStudent(student);

        assertEquals(1, saved.getCourses().size());
        assertEquals("Java", saved.getCourses().iterator().next().getName());
        verify(studentRepository).save(student);
    }

    @Test
    void createStudent_withDuplicateEmail_throwsAndDoesNotSave() {
        when(studentRepository.existsByEmail("ada@test.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> studentService.createStudent(newStudent("ada@test.com")));

        verify(studentRepository, never()).save(any());
    }

    @Test
    void createStudent_withUnknownCourse_throwsNotFound() {
        Student student = newStudent("ada@test.com");
        Course requested = new Course();
        requested.setId(99L);
        student.setCourses(new HashSet<>(List.of(requested)));

        when(studentRepository.existsByEmail("ada@test.com")).thenReturn(false);
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.createStudent(student));
    }

    @Test
    void updateStudent_changesTheFields() {
        Student existing = newStudent("old@test.com");
        existing.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(studentRepository.existsByEmailAndIdNot("new@test.com", 1L)).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student updated = new Student("Grace", "Hopper", "Brewster", "Sr.", "Female", new Date(0), "new@test.com");
        Student result = studentService.updateStudent(1L, updated);

        assertEquals("Grace", result.getFirstName());
        assertEquals("Hopper", result.getLastName());
        assertEquals("new@test.com", result.getEmail());
    }

    @Test
    void updateStudent_withEmailOfAnotherStudent_throws() {
        Student existing = newStudent("old@test.com");
        existing.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(studentRepository.existsByEmailAndIdNot("taken@test.com", 1L)).thenReturn(true);

        assertThrows(DuplicateEmailException.class,
                () -> studentService.updateStudent(1L, newStudent("taken@test.com")));
    }

    @Test
    void deleteStudent_removesTheStudent() {
        Student existing = newStudent("ada@test.com");
        existing.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));

        studentService.deleteStudent(1L);

        verify(studentRepository).delete(existing);
    }

    @Test
    void deleteStudent_whenMissing_throwsNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.deleteStudent(1L));
    }

    @Test
    void createStudent_withFutureDob_throwsInvalidDateOfBirth() {
        Student student = newStudent("future@test.com");
        student.setDob(new Date(System.currentTimeMillis() + 86_400_000L));
        when(studentRepository.existsByEmail("future@test.com")).thenReturn(false);

        assertThrows(InvalidDateOfBirthException.class, () -> studentService.createStudent(student));
    }

    @Test
    void createStudent_withUnknownSex_throwsInvalidSex() {
        Student student = newStudent("sex@test.com");
        student.setSex("Robot");
        when(studentRepository.existsByEmail("sex@test.com")).thenReturn(false);

        assertThrows(InvalidSexException.class, () -> studentService.createStudent(student));
    }

    @Test
    void updateStudent_copiesTheNewFields() {
        Student existing = newStudent("old@test.com");
        existing.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(studentRepository.existsByEmailAndIdNot("new@test.com", 1L)).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student updated = new Student("Grace", "Hopper", "Brewster", "Sr.", "Other", new Date(0), "new@test.com");
        Student result = studentService.updateStudent(1L, updated);

        assertEquals("Brewster", result.getMiddleName());
        assertEquals("Sr.", result.getSuffix());
        assertEquals("Other", result.getSex());
        assertEquals(new Date(0), result.getDob());
    }
}
