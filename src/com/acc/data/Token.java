package com.acc.data;

import com.acc.exception.SyntaxErrorException;

/**
 * Created by Rumpy on 15-01-2015.
 */
public abstract class Token {
    protected String token;

    public Token() {
        throw new SyntaxErrorException("Token cannot be empty");
    }

    public Token(String token) {
        this.token = token;
    }

    public abstract TokenType tokenType();

    public String getToken() {
        return token;
    }

    public boolean isSemicolon() {
        return token.equals(";");
    }
}
