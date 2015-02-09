package com.acc.parser;

import com.acc.data.Code;
import com.acc.data.Result;
import com.acc.data.Token;
import com.acc.util.Tokenizer;

/**
 * Created by Rumpy on 05-02-2015.
 */
public class Computation extends Parser {
    public Computation(Code code, Tokenizer tokenizer) {
        super(code, tokenizer);
    }

    @Override
    public Result parse() {
        Result x;
        Token next = tokenizer.next();
        if (next.getToken() != "main") {
            return null;
        }
        next = tokenizer.next();
        while (next.getToken() != "function" || next.getToken() != "procedure") {
            x = new VarDeclr(code, tokenizer).parse();
            next = tokenizer.next();
        }
        while (next.getToken() != "{") {
//            x = new FunDeclr(code, tokenizer).parse(); $TODO$ Nik - forgot checking in FunDecl?
            next = tokenizer.next();
        }
        next = tokenizer.next();
        x = new StatSequence(code, tokenizer).parse();
        // }. not included => syntactic sugar
        return x;
    }
}
