package com.acc.parser;

import com.acc.data.*;
import com.acc.exception.InvalidTokenException;
import com.acc.util.Tokenizer;

/**
 * Created by prabhuk on 1/25/2015.
 */
public class Factor {
    private final Code code;
    private final Tokenizer tokenizer;

    public Factor(Code code, Tokenizer tokenizer) {
        this.code = code;
        this.tokenizer = tokenizer;
    }

    public Result parse() {
        Result x = null;
        while (tokenizer.hasNext()) {
            final Token next = tokenizer.next();
            if (next.tokenType() == TokenType.OPERATOR && next.getToken().equals("(")) {
                x = new Expression(code, tokenizer).parse();
            } else if (next.tokenType() == TokenType.CONSTANT) {
                Constant nextConstant = (Constant) next;
                x = new Result(Kind.CONST, nextConstant.value(), null, null, Condition.NONE, null);
            } else if(next.tokenType() == TokenType.IDENTIFIER) {
                //$TODO$ implement lookup and set the address instead of 0
                x = new Result(Kind.VAR, null, null, 0, Condition.NONE, null);
            } else {
                break;
            }
        }
        return x;
    }
}
