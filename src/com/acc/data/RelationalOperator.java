package com.acc.data;

import com.acc.constants.Condition;
import com.acc.constants.OperationCode;
import com.acc.exception.UnknownOperatorException;

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
            value = new Condition(OperationCode.bgt);
        } else if (token.equals(">=")) {
            value = new Condition(OperationCode.bge);
        } else if (token.equals("<")) {
            value = new Condition(OperationCode.blt);
        } else if (token.equals("<=")) {
            value = new Condition(OperationCode.ble);
        } else if (token.equals("==")) {
            value = new Condition(OperationCode.beq);
        } else if (token.equals("!=")) {
            value = new Condition(OperationCode.bne);
        } else {
            throw new UnknownOperatorException(token);
        }
    }

    public Condition value() {
        return value;
    }

}
