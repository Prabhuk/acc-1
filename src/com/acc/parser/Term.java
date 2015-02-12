package com.acc.parser;

import com.acc.constants.OperationCode;
import com.acc.data.*;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.Tokenizer;

/**
 * Created by prabhuk on 1/24/2015.
 */
public class Term extends Parser {

    public Term(Code code, Tokenizer tokenizer) {
        super(code, tokenizer);
    }

    public Result parse() {
        Result x, y;
        x = new Factor(code, tokenizer).parse();

        final Token next = tokenizer.next();
        Operator nextOperator;
        while (next.tokenType() == TokenType.OPERATOR) {
            nextOperator = (Operator) next;
            int instructionCode;
            if (nextOperator.value().isMultiplication()) {
                instructionCode = OperationCode.MUL;
            } else if (nextOperator.value().isDivision()) {
                instructionCode = OperationCode.DIV;
            } else {
                break;
            }
            y = new Factor(code, tokenizer).parse();
            AuxiliaryFunctions.combine(code, instructionCode, x, y);
        }
        tokenizer.previous();
        return x;
    }
}
