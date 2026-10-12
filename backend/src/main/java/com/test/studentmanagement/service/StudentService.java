package com.test.studentmanagement.service;

import com.test.studentmanagement.exception.CourseNotFoundException;
import com.test.studentmanagement.exception.DuplicateEmailException;
import com.test.studentmanagement.exception.InvalidDateOfBirthException;
import com.test.studentmanagement.exception.InvalidSexException;
import com.test.studentmanagement.exception.StudentNotFoundException;
import com.test.studentmanagement.model.Course;
import com.test.studentmanagement.model.Student;
import com.test.studentmanagement.repository.CourseRepository;
import com.test.studentmanagement.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * StudentService
 */
@Service
@Transactional
public class StudentService {

    private static final Set<String> SEX_VALUES = Set.of("Male", "Female", "Other");

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public StudentService(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public List<Student> searchStudents(Student student) {
        return studentRepository.searchStudents(
                blankToNull(student.getFirstName()),
                blankToNull(student.getLastName()),
                blankToNull(student.getEmail()),
                blankToNull(student.getSex()),
                student.getDob());
    }

    public Student getStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    public Student createStudent(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new DuplicateEmailException(student.getEmail());
        }
        validateStudent(student);
        student.setId(null);
        student.setCourses(findCourses(student.getCourses()));
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student updated) {
        Student student = getStudent(id);
        if (studentRepository.existsByEmailAndIdNot(updated.getEmail(), id)) {
            throw new DuplicateEmailException(updated.getEmail());
        }
        validateStudent(updated);
        student.setFirstName(updated.getFirstName());
        student.setMiddleName(updated.getMiddleName());
        student.setLastName(updated.getLastName());
        student.setSuffix(updated.getSuffix());
        student.setSex(updated.getSex());
        student.setDob(updated.getDob());
        student.setEmail(updated.getEmail());
        student.setCourses(findCourses(updated.getCourses()));
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        Student student = getStudent(id);
        studentRepository.delete(student);
    }

    // dob can not be in the future and sex has to be one of the known values
    private void validateStudent(Student student) {
        if (student.getDob() != null && student.getDob().after(new Date())) {
            throw new InvalidDateOfBirthException(student.getDob());
        }
        if (student.getSex() != null && !SEX_VALUES.contains(student.getSex())) {
            throw new InvalidSexException(student.getSex());
        }
    }

    // an empty search field means "do not filter on it"
    private String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }

    // the request only has course ids, so load the real courses from the database
    private Set<Course> findCourses(Set<Course> requested) {
        Set<Course> courses = new HashSet<>();
        if (requested == null) {
            return courses;
        }
        for (Course course : requested) {
            Course courseFound = courseRepository.findById(course.getId())
                    .orElseThrow(() -> new CourseNotFoundException(course.getId()));
            courses.add(courseFound);
        }
        return courses;
    }
}
