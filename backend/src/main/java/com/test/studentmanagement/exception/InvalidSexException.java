package com.test.studentmanagement.exception;

/**
 * Custom exception when the sex value is not one of the allowed values
 */
public class InvalidSexException extends RuntimeException {

    public InvalidSexException(String sex) {
        super("Sex " + sex + " is not valid, allowed values are Male, Female or Other");
    }
}
