package com.acc.data;

/**
 * Created by Rumpy on 15-01-2015.
 */
public class Separator extends Token {

    public Separator(String token) {
        super(token);
    }

    @Override
    public TokenType tokenType() {
        return TokenType.SEPARATOR;
    }

}
