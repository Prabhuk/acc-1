package com.acc.parser;

import com.acc.data.*;
import com.acc.util.AuxiliaryFunctions;
import com.acc.util.Tokenizer;

/**
 * Created by prabhuk on 1/24/2015.
 */
public class Expression {

    private Code code;
    private Tokenizer tokenizer;

    public Expression(Code code, Tokenizer tokenizer) {
        this.code = code;
        this.tokenizer = tokenizer;
    }

    public Result parse() {
        Result x, y;
        final Term term = new Term(code, tokenizer);
        x = term.parse();
        while (tokenizer.hasNext()) {
            final Token next = tokenizer.next();
            Operator nextOperator;
            while (next.tokenType() == TokenType.OPERATOR) {
                nextOperator = (Operator) next;
                int instructionCode;
                if (nextOperator.value().isPlus()) {
                    instructionCode = OperationCode.ADD;
                } else if (nextOperator.value().isMinus()) {
                    instructionCode = OperationCode.SUB;
                } else {
                    break;
                }
                y = term.parse();
                AuxiliaryFunctions.combine(code, instructionCode, x, y);
            }
        }
        return x;
    }
}
