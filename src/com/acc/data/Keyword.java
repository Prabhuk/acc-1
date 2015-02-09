package com.acc.data;

import com.acc.constants.KeywordType;

/**
 * Created by Rumpy on 15-01-2015.
 */
public class Keyword extends Token {

    private KeywordType type;

    public Keyword(String token, KeywordType type) {
        super(token);
        this.type = type;
    }

    @Override
    public TokenType tokenType() {
        return TokenType.KEYWORD;
    }

    public KeywordType type() {
        return type;
    }


}
