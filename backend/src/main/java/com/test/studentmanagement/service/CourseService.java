package com.test.studentmanagement.service;

import com.test.studentmanagement.exception.CourseNotFoundException;
import com.test.studentmanagement.exception.DuplicateCourseNameException;
import com.test.studentmanagement.model.Course;
import com.test.studentmanagement.model.Student;
import com.test.studentmanagement.repository.CourseRepository;
import com.test.studentmanagement.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CourseService
 */
@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public CourseService(CourseRepository courseRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(id));
    }

    public Course createCourse(Course course) {
        if (courseRepository.existsByNameIgnoreCase(course.getName())) {
            throw new DuplicateCourseNameException(course.getName());
        }
        course.setId(null);
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course updated) {
        Course course = getCourse(id);
        if (courseRepository.existsByNameIgnoreCaseAndIdNot(updated.getName(), id)) {
            throw new DuplicateCourseNameException(updated.getName());
        }
        course.setName(updated.getName());
        course.setDescription(updated.getDescription());
        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        Course course = getCourse(id);
       for (Student student : studentRepository.findByCoursesId(id)) {
            student.getCourses().removeIf(c -> c.getId().equals(id));
            studentRepository.save(student);
        }
        courseRepository.delete(course);
    }
}
