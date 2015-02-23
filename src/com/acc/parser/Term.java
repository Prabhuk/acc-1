package com.acc.parser;

import com.acc.constants.OperationCode;
import com.acc.constants.SSAOpCodes;
import com.acc.data.*;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.Tokenizer;

/**
 * Created by prabhuk on 1/24/2015.
 */
public class Term extends Parser {

    public Term(Code code, Tokenizer tokenizer, SSACode ssaCode) {
        super(code, tokenizer, ssaCode);
    }

    public Result parse() {
        Result x, y;
        x = new Factor(code, tokenizer, ssaCode).parse();

        final Token next = tokenizer.next();
        Operator nextOperator;
        while (next.tokenType() == TokenType.OPERATOR) {
            nextOperator = (Operator) next;
            int instructionCode;
            String ssaIns;
            if (nextOperator.value().isMultiplication()) {
                instructionCode = OperationCode.MUL;
                ssaIns = SSAOpCodes.mul;
            } else if (nextOperator.value().isDivision()) {
                instructionCode = OperationCode.DIV;
                ssaIns = SSAOpCodes.div;
            } else {
                break;
            }
            y = new Factor(code, tokenizer, ssaCode).parse();
            AuxiliaryFunctions.generateSSA(ssaCode, ssaIns, x, y);
            AuxiliaryFunctions.combine(code, instructionCode, x, y);
        }
        tokenizer.previous();
        return x;
    }
}
