package com.acc.parser;

import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.exception.SyntaxErrorException;
import com.acc.util.Tokenizer;

/**
 * Created by prabhuk on 2/12/2015.
 */
public class FunctionBody extends Parser {

    public FunctionBody(Code code, Tokenizer tokenizer) {
        super(code, tokenizer);
    }

    @Override
    public Result parse() {
        new VariableDeclaration(code, tokenizer).parse();
        Token next = tokenizer.next();
        if (!next.getToken().equals("{")) {
            throw new SyntaxErrorException("Expected \"{\". Found [" + next + "] instead");
        }
        final Result y = new StatSequence(code, tokenizer).parse();
        next = tokenizer.next();
        if (!next.getToken().equals("{")) {
            throw new SyntaxErrorException("Expected \"}\". Found [" + next + "] instead");
        }
        return y;
    }
}
