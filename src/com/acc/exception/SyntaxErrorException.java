package com.acc.exception;

import com.acc.constants.KeywordType;
import com.acc.data.Keyword;
import com.acc.data.Token;
import com.acc.data.TokenType;

/**
 * Created by prabhuk on 1/25/2015.
 */
public class SyntaxErrorException extends RuntimeException {
    public SyntaxErrorException(String message) {
        super(message);
    }

    public SyntaxErrorException(TokenType expected, TokenType type) {
        super("Unexpected token type [" + expected.name().toLowerCase()+"]. Expected ["+ type.name().toLowerCase()+"] instead");
    }

    public SyntaxErrorException(KeywordType expected, Token found) {
        super("Expected [" + expected.getValue() + "]. Found [" + found.getToken() + "] instead");
    }
}
