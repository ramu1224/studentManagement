package com.test.studentmanagement.exception;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Custom exception when the date of birth is not a valid one (for example a date in the future)
 */
public class InvalidDateOfBirthException extends RuntimeException {

    public InvalidDateOfBirthException(Date dob) {
        super("Date of birth " + new SimpleDateFormat("yyyy-MM-dd").format(dob) + " can not be in the future");
    }
}
