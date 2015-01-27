package com.acc.parser;

import com.acc.constants.OperationCode;
import com.acc.data.*;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.Tokenizer;

/**
 * Created by prabhuk on 1/24/2015.
 */
public class Term {

    private Code code;
    private Tokenizer tokenizer;

    public Term(Code code, Tokenizer tokenizer) {
        this.code = code;
    }

    public Result parse() {
        Result x, y;
        final Factor factor = new Factor(code, tokenizer);
        x = factor.parse();
        while (tokenizer.hasNext()) {
            final Token next = tokenizer.next();
            Operator  nextOperator;
            while (next.tokenType() == TokenType.OPERATOR) {
                nextOperator = (Operator) next;
                int instructionCode;
                if(nextOperator.value().isMultiplication()) {
                    instructionCode = OperationCode.MUL;
                } else if(nextOperator.value().isDivision()) {
                    instructionCode = OperationCode.DIV;
                } else {
                    break;
                }
                y = factor.parse();
                AuxiliaryFunctions.combine(code, instructionCode, x, y);
            }
        }
        return x;
    }
}
