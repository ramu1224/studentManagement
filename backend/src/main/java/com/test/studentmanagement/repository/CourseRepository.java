package com.test.studentmanagement.repository;

import com.test.studentmanagement.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CourseRepository
 */
public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
