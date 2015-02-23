package com.acc.parser;

import com.acc.constants.Kind;
import com.acc.data.*;
import com.acc.util.Tokenizer;

/**
 * Created by prabhuk on 1/25/2015.
 */
public class Factor extends Parser {

    public Factor(Code code, Tokenizer tokenizer, SSACode ssaCode) {
        super(code, tokenizer, ssaCode);
    }

    @Override
    public Result parse() {
        Result x = null;
        final Token next = tokenizer.next();
        if (next.isOperator() && next.getToken().equals("(")) {
            x = new Expression(code, tokenizer, ssaCode).parse();
        } else if (next.isConstant()) {
            Constant nextConstant = (Constant) next;
            x = new Result(Kind.CONSTANT, null, nextConstant.value(), null, null, null);
        } else if (next.isDesignator()) {
            //$TODO$ implement lookup and set the address instead of 0
            x = new Result(Kind.VAR, null, null, 0, null, null);
            x.setVariableName(next.getToken());
        } else if (next.isKeyword() && ((Keyword) next).isCall()) {
            x = new FunctionCall(code, tokenizer, ssaCode).parse();
        }
        return x;
    }
}
