package com.test.studentmanagement.exception;

/**
 * Custom exception
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
