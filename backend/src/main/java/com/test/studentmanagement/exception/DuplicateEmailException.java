package com.test.studentmanagement.exception;

/**
 * Custom exception for duplicate emails
 */
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super("A student with email " + email + " already exists");
    }
}
