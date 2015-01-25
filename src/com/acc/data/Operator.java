package com.acc.data;

import com.acc.exception.UnknownOperatorException;

/**
 * Created by Rumpy on 15-01-2015.
 */
public class Operator implements Token {
    private String token;
    private Operators value;

    public Operator(String token) {
        this.token = token;
        assignValue();
    }

    private void assignValue() {
        if(token.equals("+")) {
            value = Operators.PLUS;
        } else if(token.equals("-")) {
            value = Operators.MINUS;
        } else if(token.equals("/")) {
            value = Operators.DIVISION;
        } else if(token.equals("*")) {
            value = Operators.PRODUCT;
        }
        throw new UnknownOperatorException(token);
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.OPERATOR;
    }

    public Operators value() {
        return value;
    }

}
