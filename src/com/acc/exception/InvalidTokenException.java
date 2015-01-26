package com.acc.exception;

/**
 * Created by prabhuk on 1/25/2015.
 */
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
