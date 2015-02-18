package com.acc.data;

/**
 * Created by prabhuk on 2/18/2015.
 */
public class ArrayIdentifier extends Token {

    public ArrayIdentifier(String token) {
        super(token);
    }

    @Override
    public TokenType tokenType() {
        return TokenType.ARRAY_IDENTIFIER;
    }
}
