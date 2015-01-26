package com.acc.data;

/**
 * Created by Rumpy on 26-01-2015.
 */
public class RelOp extends Token {

    public RelOp(String token) {
        super(token);
    }

    @Override
    public TokenType tokenType() {
        return TokenType.RELATIONAL_OPERATOR;
    }
}
