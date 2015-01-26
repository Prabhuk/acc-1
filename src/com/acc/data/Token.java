package com.acc.data;

import com.acc.exception.InvalidTokenException;

/**
 * Created by Rumpy on 15-01-2015.
 */
public abstract class Token {
    private String token;

    public Token() {
        throw new InvalidTokenException("Token cannot be empty");
    }

    public Token(String token) {
        this.token = token;
    }

    public abstract TokenType tokenType();

    public String getToken() {
        return token;
    }
}
