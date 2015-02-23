package com.acc.parser;

import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.data.SSACode;
import com.acc.data.Token;
import com.acc.exception.SyntaxErrorException;
import com.acc.util.Tokenizer;

/**
 * Created by Rumpy on 05-02-2015.
 */
public class Computation extends Parser {
    public Computation(Code code, Tokenizer tokenizer, SSACode ssaCode) {
        super(code, tokenizer, ssaCode);
    }

    @Override
    public Result parse() {
        Token next = tokenizer.next();
        if (!next.getToken().equals("main")) {
            throw new SyntaxErrorException("Your program must begin with keyword [main]");
        }

        new VariableDeclaration(code, tokenizer, ssaCode).parse();
        new FunctionDeclaration(code, tokenizer, ssaCode).parse();

        next = tokenizer.next();
        if (!next.getToken().equals("{")) {
            throw new SyntaxErrorException("Expected \"{\". Found [" + next + "] instead");
        }
        final Result s = new StatSequence(code, tokenizer, ssaCode).parse();

        next = tokenizer.next();
        if (!next.getToken().equals("}")) {
            throw new SyntaxErrorException("Expected \"}\". Found [" + next + "] instead");
        }
        next = tokenizer.next();
        if (!next.getToken().equals(".")) {
            throw new SyntaxErrorException("Your program should end with a dot [.] Found [" + next + "] instead");
        }
        return s;
    }
}
