package com.test.studentmanagement.exception;

/**
 * Custom exception when a course can not be found
 */
public class CourseNotFoundException extends ResourceNotFoundException {

    public CourseNotFoundException(Long id) {
        super("Course not found with id " + id);
    }
}
