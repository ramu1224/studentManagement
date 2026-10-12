package com.test.studentmanagement.exception;

/**
 * Custom exception when a student can not be found
 */
public class StudentNotFoundException extends ResourceNotFoundException {

    public StudentNotFoundException(Long id) {
        super("Student not found with id " + id);
    }
}
