package com.acc.data;

/**
 * Created by Rumpy on 15-01-2015.
 */
public class Keyword extends Token {

    @Override
    public TokenType tokenType() {
        return TokenType.KEYWORD;
    }
}
