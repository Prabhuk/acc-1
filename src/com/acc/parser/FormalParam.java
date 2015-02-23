package com.acc.parser;

import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.data.SSACode;
import com.acc.data.Token;
import com.acc.exception.SyntaxErrorException;
import com.acc.util.Tokenizer;

/**
 * Created by Rumpy on 12-02-2015.
 */
public class FormalParam extends Parser {

    public FormalParam(Code code, Tokenizer tokenizer, SSACode ssaCode) {
        super(code, tokenizer, ssaCode);
    }

    @Override
    public Result parse() {
        Token next = tokenizer.next();
        if (!isOpenParanthesis(next)) {
            throw new SyntaxErrorException("Expected \"(\". Found [" + next.getToken() + "] instead");
        }
        next = tokenizer.next();
        while (!isClosedParanthesis(next)) {
            if (next.getToken().equals(",")) {
                next = tokenizer.next();
            }
            //$TODO$ next is identifier at this point of time. Set symbol table and Use assign symbol to populate.
            //do something for: ident{"," ident}
            next = tokenizer.next();
        }

        return null;
    }

    private boolean isClosedParanthesis(Token next) {
        return next.getToken().equals(")");
    }

    private boolean isOpenParanthesis(Token next) {
        return next.getToken().equals("(");
    }
}
