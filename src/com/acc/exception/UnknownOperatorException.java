package com.acc.exception;

/**
 * Created by prabhuk on 1/24/2015.
 */
public class UnknownOperatorException extends RuntimeException {
    public UnknownOperatorException(String message) {
        super(message + " is not a valid operator");
    }
}
