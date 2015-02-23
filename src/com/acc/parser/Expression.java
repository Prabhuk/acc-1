package com.acc.parser;

import com.acc.constants.Kind;
import com.acc.constants.OperationCode;
import com.acc.data.*;
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
            int op;
            if (nextOperator.value().isPlus()) {
                op = OperationCode.add;
            } else { //Minus
                op = OperationCode.sub;
            }
            y = new Term(code, tokenizer).parse();
            if (x.kind().isConstant() && y.kind().isConstant()) {
                if (op == OperationCode.add) {
                    x.value(x.value() + y.value());
                } else if (op == OperationCode.sub) {
                    x.value(x.value() - y.value());
                } else {
                    throw new UnsupportedOperationException("Combine cannot process Operation code [" + op + "]");
                }
            } else {
                x.setIntermediateLoation(code.getPc() - 1);
                AuxiliaryFunctions.addInstruction(op, code, x, y, getSymbolTable());
                x.kind(Kind.INTERMEDIATE);
            }

            next = tokenizer.next();
        }
//        if(x.kind().isVariable()) {
//            x.address(getSymbolTable().getRecentOccurence(x.getVariableName())
//        }
        tokenizer.previous(); //Rolling back the token which broke the while loop
        return x;
    }
}
