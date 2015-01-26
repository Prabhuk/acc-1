package com.acc.data;

/**
 * Created by Rumpy on 26-01-2015.
 */
public class AssignmentOperator extends Token{
    public AssignmentOperator(String token) {
        super(token);
    }

    @Override
    public TokenType tokenType() {
        return TokenType.ASSIGNMENT_OPERATOR;
    }
}
