package com.acc.parser;

import com.acc.constants.Kind;
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
            int op;
            if (nextOperator.value().isMultiplication()) {
                op = OperationCode.mul;
            } else if (nextOperator.value().isDivision()) {
                op = OperationCode.div;
            } else {
                break;
            }
            y = new Factor(code, tokenizer).parse();

            if (x.kind().isConstant() && y.kind().isConstant()) {
                if (op == OperationCode.mul) {
                    x.value(x.value() * y.value());
                } else if (op == OperationCode.div) {
                    x.value(x.value() / y.value());
                } else {
                    throw new UnsupportedOperationException("Combine cannot process Operation code [" + op + "]");
                }
            } else {
                AuxiliaryFunctions.addInstruction(op, code, x, y, getSymbolTable());
                x = new Result(Kind.INTERMEDIATE);
                x.setIntermediateLoation(code.getPc() - 1);
            }
        }
        tokenizer.previous();
        return x;
    }
}
