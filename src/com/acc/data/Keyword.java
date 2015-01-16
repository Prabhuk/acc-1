package com.acc.data;

/**
 * Created by Rumpy on 15-01-2015.
 */
public class Keyword implements Token{
    private String token;

    public Keyword(String token) {
        this.token = token;
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.Keyword;
    }
}
