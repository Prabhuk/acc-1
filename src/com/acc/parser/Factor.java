package com.acc.parser;

import com.acc.constants.Condition;
import com.acc.constants.Kind;
import com.acc.data.*;
import com.acc.util.Tokenizer;

/**
 * Created by prabhuk on 1/25/2015.
 */
public class Factor extends Parser{

    public Factor(Code code, Tokenizer tokenizer) {
        super(code, tokenizer);
    }

    @Override
    public Result parse() {
        Result x = null;
        while (tokenizer.hasNext()) {
            final Token next = tokenizer.next();
            if (next.tokenType().isOperator() && next.getToken().equals("(")) {
                x = new Expression(code, tokenizer).parse();
            } else if (next.tokenType().isConstant()) {
                Constant nextConstant = (Constant) next;
                x = new Result(Kind.CONST, nextConstant.value(), null, null, Condition.NONE, null);
            } else if(next.tokenType().isIdentifier()) {
                //$TODO$ implement lookup and set the address instead of 0
                x = new Result(Kind.VAR, null, null, 0, Condition.NONE, null);
            } else {
                break;
            }
        }
        return x;
    }
}
