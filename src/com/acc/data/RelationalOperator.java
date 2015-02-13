package com.acc.data;

import com.acc.constants.Condition;
import com.acc.constants.OperationCode;
import com.acc.constants.Operators;
import com.acc.exception.UnknownOperatorException;
import com.acc.constants.RelationalOperators;

/**
* Created by Rumpy on 26-01-2015.
*/
public class RelationalOperator extends Token {
    private Condition value;

    public RelationalOperator(String token) {
        super(token);
        assignValue();
    }

    @Override
    public TokenType tokenType() {
        return TokenType.RELATIONAL_OPERATOR;
    }

    private void assignValue() {

        if (token.equals(">")) {
            value = new Condition(OperationCode.BGT);
        } else if (token.equals(">=")) {
            value = new Condition(OperationCode.BGE);
        } else if (token.equals("<")) {
            value = new Condition(OperationCode.BLT);
        } else if (token.equals("<=")) {
            value = new Condition(OperationCode.BLE);
        } else if (token.equals("==")) {
            value = new Condition(OperationCode.BEQ);
        } else if (token.equals("!=")) {
            value = new Condition(OperationCode.BNE);
        } else {
            throw new UnknownOperatorException(token);
        }
    }

    public Condition value() {
        return value;
    }

}
