package com.test.studentmanagement.exception;

/**
 * Custom exception for duplicate course names
 */
public class DuplicateCourseNameException extends RuntimeException {

    public DuplicateCourseNameException(String name) {
        super("A course with name " + name + " already exists");
    }
}
