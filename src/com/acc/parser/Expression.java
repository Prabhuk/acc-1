package com.acc.parser;

import com.acc.constants.OperationCode;
import com.acc.data.Code;
import com.acc.data.Operator;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.Tokenizer;

/**
 * Created by prabhuk on 1/24/2015.
 */
public class Expression extends Parser {

    public Expression(Code code, Tokenizer tokenizer) {
        super(code, tokenizer);
    }

    @Override
    public Result parse() {
        Result x, y;
        x = new Term(code, tokenizer).parse();
        Token next = tokenizer.next();
        Operator nextOperator;
        while (next.isOperator() && (((Operator) next).value().isPlus() || ((Operator) next).value().isMinus())) {
            nextOperator = ((Operator) next);
            int instructionCode;
            if (nextOperator.value().isPlus()) {
                instructionCode = OperationCode.ADD;
            } else { //Minus
                instructionCode = OperationCode.SUB;
            }
            y = new Term(code, tokenizer).parse();
            AuxiliaryFunctions.combine(code, instructionCode, x, y);
            next = tokenizer.next();
        }
        tokenizer.previous(); //Rolling back the token which broke the while loop
        return x;
    }
}
