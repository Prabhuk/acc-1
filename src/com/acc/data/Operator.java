package com.acc.data;

import com.acc.constants.Operators;
import com.acc.exception.UnknownOperatorException;

/**
 * Created by Rumpy on 15-01-2015.
 */
public class Operator extends Token {
    private Operators value;

    public Operator(String token) {
        super(token);
        assignValue();
    }

    private void assignValue() {
        if (token.equals("+")) {
            value = Operators.PLUS;
        } else if (token.equals("-")) {
            value = Operators.MINUS;
        } else if (token.equals("/")) {
            value = Operators.DIVISION;
        } else if (token.equals("*")) {
            value = Operators.PRODUCT;
        } else if (token.equals("(")) {
            value = Operators.OPEN_BRACKET;
        } else if (token.equals(")")) {
            value = Operators.CLOSE_BRACKET;
        } else {
            throw new UnknownOperatorException(token);
        }
    }

    @Override
    public TokenType tokenType() {
        return TokenType.OPERATOR;
    }

    public Operators value() {
        return value;
    }

}
