package com.acc.exception;

import com.acc.constants.KeywordType;
import com.acc.data.Token;

/**
 * Created by prabhuk on 1/25/2015.
 */
public class SyntaxErrorException extends RuntimeException {
    public SyntaxErrorException(String message) {
        super(message);
    }

    public SyntaxErrorException(KeywordType expected, Token found) {
        super("Expected [" + expected.getValue() + "]. Found [" + found.getToken() + "] instead");
    }
}
